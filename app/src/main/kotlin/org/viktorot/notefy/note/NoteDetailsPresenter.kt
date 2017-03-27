package org.viktorot.notefy.note

import android.support.annotation.DrawableRes
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.viktorot.notefy.base.BasePresenter
import org.viktorot.notefy.models.NoteModel
import org.viktorot.notefy.repo.NoteRepository
import org.viktorot.notefy.utils.NotificationUtils

class NoteDetailsPresenter(private val repo: NoteRepository, private val view: NoteDetailsView) : BasePresenter(repo, view) {

    companion object {
        @JvmStatic val TAG: String = NoteDetailsPresenter::class.java.simpleName
    }

    var isNew: Boolean = true
    lateinit var note: NoteModel

    /*
        PUBLIC INTERFACE
    */

    fun init(note: NoteModel = NoteModel.empty) {
        this.note = note
        this.isNew = (this.note == NoteModel.empty)

        if (!this.isNew) {
            view.setIcon(this.note.icon)
            view.setTitle(this.note.title)
            view.setContent(this.note.content)
        }
    }

    fun saveChanges() {
        when (this.isNew) {
            true -> saveNote()
            false -> updateNote()
        }
    }

    fun updateMenuState() {
        view.setPinned(note.pinned)
        updateCanSaveState()
    }

    fun onTitleUpdate(newValue: String) {
        this.note.title = newValue
        updateCanSaveState()
    }

    fun onContentUpdate(newValue: String) {
        this.note.content = newValue
        updateCanSaveState()
    }

    fun onIconUpdate(@DrawableRes iconResId: Int) {
        this.note.icon = iconResId
        view.setIcon(this.note.icon)
    }

    fun onPinnedStateToggled() {
        togglePinned()
        if (!isNew) savePinnedStateUpdate(note.pinned)
    }




    private fun saveNote() {
        repo.saveNote(this.note.title, this.note.content, this.note.icon, this.note.pinned)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { id: Int ->
                            note.id = id
                            isNew = false

                            view.showSaveSuccess("[Note saved]")
                            updateNotification()
                        },
                        { error ->
                            Log.e(TAG, error.toString())
                            view.showSaveError()
                        }
                )
    }

    private fun updateNote() {
        repo.updateNote(this.note.id, this.note.title, this.note.content, this.note.icon, this.note.pinned)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            view.showSaveSuccess("[Note updated]")
                            updateNotification()
                        },
                        { error ->
                            Log.e(TAG, error.toString())
                            view.showSaveError()
                        }
                )
    }

    private fun savePinnedStateUpdate(newState: Boolean) {
        repo.setPinned(note.id, newState)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            Log.d(TAG, "pinned state updated. new state => $note.pinned")
                            updateNotification()
                        },
                        { error ->
                            Log.e(TAG, "set pinned error => $error")
                            view.showSaveError()
                        })
    }

    private fun updateCanSaveState() {
        view.enableSaving(isNoteValid())
    }

    private fun togglePinned() {
        note.pinned = !note.pinned
        view.setPinned(this.note.pinned)
    }


    private fun updateNotification() {
        NotificationUtils.notify(note)
    }

    private fun isNoteValid(): Boolean {
        return !this.note.title.isEmpty()
    }

    override fun cleanUp() {
    }
}