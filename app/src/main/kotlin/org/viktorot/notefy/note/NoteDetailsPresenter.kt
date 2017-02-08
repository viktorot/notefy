package org.viktorot.notefy.note

import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.viktorot.notefy.base.BasePresenter
import org.viktorot.notefy.models.NoteDbModel
import org.viktorot.notefy.repo.NotesRepository

class NoteDetailsPresenter(private val repo: NotesRepository, private val view: NoteDetailsView): BasePresenter(repo, view) {

    companion object {
        @JvmStatic val TAG: String = NoteDetailsPresenter::class.java.simpleName
    }

    var isNew: Boolean = true
    lateinit var note: NoteDbModel

    fun init(note: NoteDbModel = NoteDbModel.default) {
        this.note = note
        isNew = note == NoteDbModel.default
    }

    fun saveNote(title: String, content: String, icon: Int, pinned: Boolean) {
        repo.saveNote(title, content, icon, pinned)
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


    override fun cleanUp() {
    }


}