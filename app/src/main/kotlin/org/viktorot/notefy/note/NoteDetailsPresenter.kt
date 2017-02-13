package org.viktorot.notefy.note

import android.support.annotation.DrawableRes
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.viktorot.notefy.base.BasePresenter
import org.viktorot.notefy.models.NoteModel
import org.viktorot.notefy.repo.NotesRepository
import org.viktorot.notefy.utils.NoteIcons

class NoteDetailsPresenter(private val repo: NotesRepository, private val view: NoteDetailsView): BasePresenter(repo, view) {

    companion object {
        @JvmStatic val TAG: String = NoteDetailsPresenter::class.java.simpleName
    }

    var isNew: Boolean = true
    lateinit var note: NoteModel

    fun init(note: NoteModel = NoteModel.empty) {
        this.note = note
        isNew = (this.note == NoteModel.empty)
    }

    fun saveNote() {
        repo.saveNote(this.note.title, this.note.content, this.note.icon, this.note.pinned)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { success ->
                            when (success) {
                                true -> view.showSaveSuccess()
                                false -> view.showSaveError()
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