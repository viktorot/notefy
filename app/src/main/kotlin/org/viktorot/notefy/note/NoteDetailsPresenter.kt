package org.viktorot.notefy.note

import android.support.annotation.DrawableRes
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.viktorot.notefy.base.BasePresenter
import org.viktorot.notefy.models.NoteModel
import org.viktorot.notefy.repo.NotesRepository
import org.viktorot.notefy.utils.NotificationUtils

class NoteDetailsPresenter(private val repo: NotesRepository, private val view: NoteDetailsView): BasePresenter(repo, view) {

    companion object {
        @JvmStatic val TAG: String = NoteDetailsPresenter::class.java.simpleName
    }

    var isNew: Boolean = true
    lateinit var note: NoteModel

    fun init(note: NoteModel = NoteModel.empty) {
        this.note = note
        isNew = (this.note == NoteModel.empty)

        if (!isNew) {
            view.setIcon(this.note.icon)
            view.setTitle(this.note.title)
            view.setContent(this.note.content)
        }
    }

    fun saveChanges() {
        if (isNew) {
            saveNote()
        }
        else {
            updateNote()
        }
    }

    private fun saveNote() {
        repo.saveNote(this.note.title, this.note.content, this.note.icon, this.note.pinned)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { success ->
                            when (success) {
                                true -> view.showSaveSuccess("[Note saved]")
                                false -> view.showSaveError()
                            }
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
                        { success ->
                            view.showSaveSuccess("[Note updated]")
                        },
                        { error ->
                            Log.e(TAG, error.toString())
                            view.showSaveError()
                        }
                )
    }


    fun onTitleUpdate(newValue: String) {
        this.note.title = newValue
    }

    fun onContentUpdate(newValue: String) {
        this.note.content = newValue
    }

    fun updatePinnedState() {
        view.setPinned(this.note.pinned)
    }

    fun togglePinned() {
        this.note.pinned = !this.note.pinned
        view.setPinned(this.note.pinned)

        if (this.note.pinned) {
            NotificationUtils.displayNotification(this.note)
        }
        else {
            NotificationUtils.removeNotification(this.note.id)
        }
    }

    fun onIconUpdate(@DrawableRes iconResId: Int) {
        this.note.icon = iconResId
        view.setIcon(this.note.icon)
    }




    fun printModel () {
        Log.d(TAG, this.note.toString())
    }

    override fun cleanUp() {
    }


}