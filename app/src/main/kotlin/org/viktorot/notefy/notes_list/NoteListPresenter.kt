package org.viktorot.notefy.notes_list

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.viktorot.notefy.base.BasePresenter
import org.viktorot.notefy.exceptions.NoteDeleteException
import org.viktorot.notefy.models.NoteModel
import org.viktorot.notefy.repo.NoteRepository
import org.viktorot.notefy.utils.NotificationUtils
import timber.log.Timber

class NoteListPresenter(private val repo: NoteRepository, private val view: NotesListView) : BasePresenter(repo, view) {

    private var notes: MutableList<NoteModel> = emptyList<NoteModel>().toMutableList()

    private var pinnedUpdateDisposable: Disposable? = null
    private var notesDisposable: Disposable? = null
    private var deleteDisposable: Disposable? = null

    private lateinit var notesChangedDisposable: Disposable

    fun init() {
        notesChangedDisposable = repo.getNotesChangedObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { changed ->
                            Timber.w("noted changed => $changed")
                            if (changed) updateNotes()
                        },
                        { error ->
                            Timber.e("notes changed => $error")
                        }
                )
    }

    fun getNotes() {
        view.showLoadingView()

        if (!notes.isEmpty()) {
            retrieveNotes()
        }
        else {
            view.showNotes(notes)
        }
    }

    private fun updateNotes() {
        notesDisposable?.dispose()
        retrieveNotes()
    }

    fun onNoteClick(id: Int): Unit {
        val note: NoteModel? = this.notes.find { note: NoteModel -> note.id == id }

        if (note != null) {
            view.navigateToNote(note)
        } else {
            Timber.w("note id => $id not found. this should not happen")
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

                            Timber.d("note => ${note.id}, pinned => ${note.pinned}")

                            view.updateNote(note)
                            NotificationUtils.notify(note)
                        },
                        { error ->
                            Timber.e("error updating pinned state => $error")
                        })
    }

    fun deleteNote(positions: List<Int>) {
        Observable.fromIterable(positions)
                .subscribeOn(Schedulers.io())
                .map { pos: Int -> notes[pos].id }
                .flatMap { id -> repo.deleteNote(id).toObservable() }
                .doOnError { error -> Timber.e(error) }
                .subscribe { id: Int ->
                    Timber.d("note id => %d deleted", id)
                }
    }

    private fun retrieveNotes() {
        notesDisposable = repo.getNotes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { notes ->
                            this.notes = notes.toMutableList()

                            if (!notes.isEmpty()) {
                                view.showNotes(notes)
                            }
                            else {
                                view.showEmptyView()
                            }
                        },
                        { error ->
                            Timber.w("error getting notes => $error")
                            view.showError()
                        }
                )
    }

    override fun cleanUp() {
        notesDisposable?.dispose()
        pinnedUpdateDisposable?.dispose()
        notesChangedDisposable.dispose()
        deleteDisposable?.dispose()
    }

}