package org.viktorot.notefy.repo

import android.content.Context
import android.support.annotation.DrawableRes
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import org.viktorot.notefy.utils.database
import org.viktorot.notefy.exceptions.NoteDeleteException
import org.viktorot.notefy.exceptions.NoteSaveException
import org.viktorot.notefy.exceptions.NoteUpdateException
import org.viktorot.notefy.exceptions.PinnedStateUpdateException
import org.viktorot.notefy.data.NoteDbModel
import org.viktorot.notefy.data.NoteModel
import org.viktorot.notefy.utils.timestamp
import org.viktorot.notefy.utils.NoteIcons
import timber.log.Timber

class NoteRepository(val ctx: Context) {

    companion object {
        @JvmStatic
        private var _instance: NoteRepository? = null

        @JvmStatic
        fun instance(ctx: Context): NoteRepository {
            if (_instance == null) {
                _instance = NoteRepository(ctx)
            }
            return _instance as NoteRepository
        }

        val CHANGE_SAVE: Int = 0
        val CHANGE_UPDATE: Int = 1
    }

    private val notesChangedRelay: BehaviorRelay<Boolean> = BehaviorRelay.createDefault(true)
    fun getNotesChangedObservable(): Observable<Boolean> {
        return notesChangedRelay.doOnNext { changed ->
            Timber.v("new changes state accepted => $changed")
        }
    }

    private val changeTypeRelay: PublishRelay<Int> = PublishRelay.create()
    fun changeTypeObservable(): Observable<Int> {
        return changeTypeRelay.doOnNext { type ->
            Timber.v("change accepted => $type")
        }
    }


    fun getNotes(): Single<List<NoteModel>> {
        val db = ctx.database
        notesChangedRelay.accept(false)

        return Single.fromCallable {
            db.getAll().map { dbModel: NoteDbModel ->
                @DrawableRes val iconResId: Int = NoteIcons.getResId(dbModel.image)
                NoteModel(dbModel.id, dbModel.title, dbModel.content, iconResId, dbModel.pinned, dbModel.timestamp)
            }
        }
    }

    fun saveNote(title: String, content: String, @DrawableRes iconResId: Int, pinned: Boolean): Single<Int> {
        val db = ctx.database
        return Single.fromCallable {
            val iconId: Int = NoteIcons.getId(iconResId)
            val res: Int = db.add(title, content, iconId, pinned, ctx.timestamp).toInt()

            when (res > -1) {
                true -> res
                false -> throw NoteSaveException()
            }
        }.map { id ->
            notesChangedRelay.accept(true)
            changeTypeRelay.accept(CHANGE_SAVE)
            id
        }
    }

    fun updateNote(id: Int, title: String, content: String, @DrawableRes iconResId: Int, pinned: Boolean): Completable {
        val db = ctx.database
        return Single.fromCallable {
            val iconId: Int = NoteIcons.getId(iconResId)
            val res: Int = db.update(id, title, content, iconId, pinned, ctx.timestamp)

            when (res > -1) {
                true -> true
                false -> throw NoteUpdateException()
            }
        }.map { success ->
            notesChangedRelay.accept(success)
            changeTypeRelay.accept(CHANGE_UPDATE)
            success
        }.toCompletable()
    }

    fun deleteNote(id: Int): Single<Int> {
        val db = ctx.database
        return Single.fromCallable {
            val res: Int = db.delete(id)

            when (res > 0) {
                true -> id
                false -> throw NoteDeleteException("error deleting note id => $id")
            }
        }.map { id ->
            notesChangedRelay.accept(true)
            id
        }

    }

//    fun deleteNote(id: Int): Boolean {
//        val db = ctx.database
//        //val res: Int = db.delete(id)
//        //return res > 0
//        throw NoteDeleteException("error deleting note id => $id")
//    }

    fun setPinned(id: Int, pinned: Boolean): Completable {
        val db = ctx.database
        return Single.fromCallable {
            val res: Int = db.setPinned(id, pinned)

            when (res > -1) {
                true -> true
                false -> throw PinnedStateUpdateException()
            }
        }.map { success ->
            notesChangedRelay.accept(success)
            success
        }.toCompletable()
    }

}