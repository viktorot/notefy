package org.viktorot.notefy.repo

import android.content.Context
import android.support.annotation.DrawableRes
import io.reactivex.Completable
import io.reactivex.Single
import org.viktorot.notefy.database
import org.viktorot.notefy.exceptions.NoteSaveException
import org.viktorot.notefy.exceptions.NoteUpdateException
import org.viktorot.notefy.exceptions.PinnedStateUpdateException
import org.viktorot.notefy.models.NoteDbModel
import org.viktorot.notefy.models.NoteModel
import org.viktorot.notefy.timestamp
import org.viktorot.notefy.utils.NoteIcons

class NoteRepository(val ctx: Context) {

    fun getNotes(): Single<List<NoteModel>> {
        val db = ctx.database
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
        }
    }

    fun updateNote(id: Int, title: String, content: String, @DrawableRes iconResId: Int, pinned: Boolean): Completable {
        val db = ctx.database
        return Completable.fromCallable {
            val iconId: Int = NoteIcons.getId(iconResId)
            val res: Int = db.update(id, title, content, iconId, pinned, ctx.timestamp)

            when (res > -1) {
                true -> true
                false -> throw NoteUpdateException()
            }
        }
    }

    fun setPinned(id: Int, pinned: Boolean): Completable {
        val db = ctx.database
        return Completable.fromCallable {
            val res: Int = db.setPinned(id, pinned)

            when (res > -1) {
                true -> true
                false -> throw PinnedStateUpdateException()
            }
        }
    }

}