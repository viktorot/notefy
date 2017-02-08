package org.viktorot.notefy.notes_list

import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.viktorot.notefy.base.BasePresenter
import org.viktorot.notefy.models.NoteDbModel
import org.viktorot.notefy.repo.NotesRepository

class NoteListPresenter(private val repo: NotesRepository, private val view: NotesListView): BasePresenter(repo, view) {

    companion object {
        @JvmStatic
        val TAG: String = NoteListPresenter::class.java.simpleName
    }

    lateinit private var disposable: Disposable
    lateinit private var notes: List<NoteDbModel>

    fun getNotes() {
        view.showLoadingView()

        disposable = repo.getNotes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { notes ->
                            this.notes = notes

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
        val note: NoteDbModel? = this.notes.find { note: NoteDbModel -> note.id == id }

        if (note != null) {
            view.navigateToNote(note)
        }
        else {
            Log.w(TAG, "note id => $id not found. should not happen")
        }
    }

    override fun cleanUp() {
        disposable.dispose()
    }

}