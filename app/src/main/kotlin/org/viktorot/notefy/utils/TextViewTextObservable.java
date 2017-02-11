package org.viktorot.notefy.utils;

import io.reactivex.Observable;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

public final class TextViewTextObservable extends Observable<CharSequence> {
    private final TextView view;

    public TextViewTextObservable(TextView view) {
        this.view = view;
    }

    private void subscribeListener(Observer<? super CharSequence> observer) {
        Listener listener = new Listener(view, observer);
        observer.onSubscribe(listener);
        view.addTextChangedListener(listener);
    }

    private CharSequence getInitialValue() {
        return view.getText();
    }

    @Override
    protected void subscribeActual(Observer<? super CharSequence> observer) {
        subscribeListener(observer);
        observer.onNext(getInitialValue());
    }

    private final static class Listener extends MainThreadDisposable implements TextWatcher {
        private final TextView view;
        private final Observer<? super CharSequence> observer;

        Listener(TextView view, Observer<? super CharSequence> observer) {
            this.view = view;
            this.observer = observer;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!isDisposed()) {
                observer.onNext(s);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }

        @Override
        protected void onDispose() {
            view.removeTextChangedListener(this);
        }
    }
}

