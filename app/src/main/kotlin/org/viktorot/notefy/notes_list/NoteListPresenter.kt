package org.viktorot.notefy.notes_list

import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.viktorot.notefy.base.BasePresenter
import org.viktorot.notefy.models.NoteModel
import org.viktorot.notefy.repo.NoteRepository
import org.viktorot.notefy.utils.NotificationUtils

class NoteListPresenter(private val repo: NoteRepository, private val view: NotesListView) : BasePresenter(repo, view) {

    companion object {
        @JvmStatic
        val TAG: String = NoteListPresenter::class.java.simpleName
    }

    lateinit private var notesDisposable: Disposable
    lateinit private var pinnedUpdateDisposable: Disposable
    lateinit private var notes: MutableList<NoteModel>

    fun getNotes() {
        view.showLoadingView()

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
            Log.w(TAG, "note id => $id not found. should not happen")
        }
    }

    fun onPinToggled(note: NoteModel, newState: Boolean) {
        val index: Int = notes.indexOf(note)
        if (index == -1) {
            return
        }

        pinnedUpdateDisposable = repo.setPinned(note.id, note.pinned)
                .subscribe(
                        {
                            Log.d(TAG, "note => ${note.title}, pinned => ${note.pinned}")

                            note.pinned = newState
                            notes[index] = note

                            view.updateNote(note)
                            NotificationUtils.notify(note)
                        },
                        { error ->
                            Log.e(TAG, "error updating pinned state => $error")
                        })
    }


    override fun cleanUp() {
        notesDisposable.dispose()

        // TODO:
        //pinnedUpdateDisposable.dispose()
    }

}