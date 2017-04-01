package org.viktorot.notefy.notes_list

import android.util.Log
import io.reactivex.CompletableObserver
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.viktorot.notefy.base.BasePresenter
import org.viktorot.notefy.models.NoteModel
import org.viktorot.notefy.repo.NoteRepository
import org.viktorot.notefy.utils.NotificationUtils
import timber.log.Timber

class NoteListPresenter(private val repo: NoteRepository, private val view: NotesListView) : BasePresenter(repo, view) {

    companion object {
        @JvmStatic
        val TAG: String = NoteListPresenter::class.java.simpleName
    }

    lateinit private var notes: MutableList<NoteModel>

    private var pinnedUpdateDisposable: Disposable? = null
    private var notesDisposable: Disposable? = null

    private lateinit var notesChangedDisposable: Disposable

    fun init() {
        notesChangedDisposable = repo.getNotesChangedObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { changed ->
                            Timber.w("noted changed => $changed")
                            if (changed) getNotes()
                        },
                        { error ->
                            Timber.e("notes changed => $error")
                        }
                )
    }

    fun getNotes() {
        view.showLoadingView()

        notesDisposable?.dispose()

        notesDisposable = repo.getNotes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { notes ->
                            this.notes = notes.toMutableList()

                            if (!notes.isEmpty()) {
                                view.showNotes(notes)
                            } else {
                                view.showEmptyView()
                            }
                        },
                        { error ->
                            Log.d(TAG, error.toString())
                            view.showError()
                        }
                )
    }

    fun onNoteClick(id: Int): Unit {
        val note: NoteModel? = this.notes.find { note: NoteModel -> note.id == id }

        if (note != null) {
            view.navigateToNote(note)
        } else {
            Log.w(TAG, "note id => $id not found. this should not happen")
        }
    }

    fun onPinToggled(note: NoteModel, newState: Boolean) {
        val index: Int = notes.indexOf(note)
        if (index == -1) {
            return
        }

        pinnedUpdateDisposable = repo.setPinned(note.id, newState)
                .subscribe(
                        {
                            note.pinned = newState
                            notes[index] = note

                            Log.d(TAG, "note => ${note.id}, pinned => ${note.pinned}")

                            view.updateNote(note)
                            NotificationUtils.notify(note)
                        },
                        { error ->
                            Log.e(TAG, "error updating pinned state => $error")
                        })
    }


    override fun cleanUp() {
        notesDisposable?.dispose()
        pinnedUpdateDisposable?.dispose()
        notesChangedDisposable.dispose()
    }

}