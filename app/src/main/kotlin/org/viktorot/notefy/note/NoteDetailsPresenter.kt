package org.viktorot.notefy.note

import android.support.annotation.DrawableRes
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.viktorot.notefy.base.BasePresenter
import org.viktorot.notefy.data.NoteModel
import org.viktorot.notefy.repo.NoteRepository
import org.viktorot.notefy.utils.NotificationUtils
import timber.log.Timber

class NoteDetailsPresenter(private val repo: NoteRepository, private val view: NoteDetailsView) : BasePresenter(repo, view) {

    var isNew: Boolean = true
    lateinit var note: NoteModel
    lateinit var originalNote: NoteModel

    /*
        PUBLIC INTERFACE
    */

    fun init(note: NoteModel = NoteModel.empty) {
        this.note = note
        originalNote = note.copy()

        isNew = (note == NoteModel.empty)

        if (!isNew) {
            view.setIcon(this.note.icon)
            view.setTitle(this.note.title)
            view.setContent(this.note.content)
        }
    }

    fun saveChanges() {
        if (!hasChanges()) return

        when (isNew) {
            true -> saveNote()
            false -> updateNote()
        }
    }

    fun updateMenuState() {
        view.setPinned(note.pinned)
        updateCanSaveState()
    }

    fun onTitleUpdate(newValue: String) {
        note.title = newValue
        updateCanSaveState()
    }

    fun onContentUpdate(newValue: String) {
        note.content = newValue
        updateCanSaveState()
    }

    fun onIconUpdate(@DrawableRes iconResId: Int) {
        note.icon = iconResId
        view.setIcon(note.icon)
        updateCanSaveState()
    }

    fun onPinnedStateToggled() {
        togglePinned()
        if (!isNew) savePinnedStateUpdate(note.pinned)
    }


    private fun saveNote() {
        repo.saveNote(this.note.title, this.note.content, this.note.icon, this.note.pinned)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ id: Int ->
                            note.id = id
                            isNew = false

                            //view.showSaveSuccess("[Note saved]")
                            updateNotification()
                        },{ error ->
                            Timber.e("saving => $error")
                            view.showSaveError()
                        }
                )
    }

    private fun updateNote() {
        repo.updateNote(this.note.id, this.note.title, this.note.content, this.note.icon, this.note.pinned)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                            //view.showSaveSuccess("[Note updated]")
                            updateNotification()
                        },{ error ->
                            Timber.e("updating => $error")
                            view.showSaveError()
                        }
                )
    }

    private fun savePinnedStateUpdate(newState: Boolean) {
        repo.setPinned(note.id, newState)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                            Timber.d("pinned state updated. new state => $note.pinned")
                            updateNotification()
                        },{ error ->
                            Timber.e("update pinned state => $error")
                            view.showSaveError()
                        })
    }

    private fun hasChanges(): Boolean {
        return isNoteValid() && note != originalNote
    }

    private fun updateCanSaveState() {
        view.showSaveIcon(hasChanges())
    }

    private fun togglePinned() {
        note.pinned = !note.pinned
        view.setPinned(this.note.pinned)
    }

    private fun updateNotification() {
        NotificationUtils.notify(note)
    }

    private fun isNoteValid(): Boolean {
        return !note.title.isEmpty()
    }

    override fun cleanUp() {
    }
}