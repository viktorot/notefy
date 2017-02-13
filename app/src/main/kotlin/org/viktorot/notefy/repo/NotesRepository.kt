package org.viktorot.notefy.repo

import android.content.Context
import android.support.annotation.DrawableRes
import io.reactivex.Single
import org.viktorot.notefy.database
import org.viktorot.notefy.models.NoteDbModel
import org.viktorot.notefy.models.NoteModel
import org.viktorot.notefy.timestamp
import org.viktorot.notefy.utils.NoteIcons

class NotesRepository(val ctx: Context) {

    fun getNotes(): Single<List<NoteModel>> {
        val db = ctx.database
        return Single.fromCallable {
            db.getAll().map { dbModel: NoteDbModel ->
                @DrawableRes val iconResId: Int = NoteIcons.getResId(dbModel.image)
                NoteModel(dbModel.id, dbModel.title, dbModel.content, iconResId, dbModel.pinned, dbModel.timestamp)
            }
        }
    }

    fun saveNote(title: String, content: String, icon: Int, pinned: Boolean): Single<Boolean> {
        val db = ctx.database
        return Single.fromCallable {
            val res: Long = db.add(title, content, icon, pinned, ctx.timestamp)
            res.toInt() != -1
        }
    }

}