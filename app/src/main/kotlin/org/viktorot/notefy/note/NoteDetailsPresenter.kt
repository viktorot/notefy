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

    private fun saveNote() {
        repo.saveNote(this.note.title, this.note.content, this.note.icon, this.note.pinned)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { success ->
                            when (success) {
                                true -> {
                                    view.showSaveSuccess("[Note saved]")
                                    updateNotification()
                                }
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
                            run {
                                view.showSaveSuccess("[Note updated]")
                                updateNotification()
                            }
                        },
                        { error ->
                            Log.e(TAG, error.toString())
                            view.showSaveError()
                        }
                )
    }


    fun onTitleUpdate(newValue: String) {
        this.note.title = newValue
        updateCanSaveState()
    }

    fun onContentUpdate(newValue: String) {
        this.note.content = newValue
        updateCanSaveState()
    }

    fun updateMenuState() {
        view.setPinned(this.note.pinned)
        updateCanSaveState()
    }

    private fun updateCanSaveState() {
        view.enableSaving(isNoteValid())
    }

    fun togglePinned() {
        this.note.pinned = !this.note.pinned
        view.setPinned(this.note.pinned)
    }

    fun onIconUpdate(@DrawableRes iconResId: Int) {
        this.note.icon = iconResId
        view.setIcon(this.note.icon)
    }

    private fun updateNotification() {
        when (note.pinned) {
            true -> NotificationUtils.displayNotification(note)
            false -> NotificationUtils.removeNotification(note.id)
        }
    }

    private fun isNoteValid(): Boolean {
        return !this.note.title.isEmpty() && !this.note.content.isEmpty()
    }


    fun printModel() {
        Log.d(TAG, this.note.toString())
    }

    override fun cleanUp() {
    }


}