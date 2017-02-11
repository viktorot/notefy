package org.viktorot.notefy.repo

import android.content.Context
import io.reactivex.Single
import org.viktorot.notefy.database
import org.viktorot.notefy.models.NoteDbModel
import org.viktorot.notefy.timestamp

class NotesRepository(val ctx: Context) {

    fun getNotes(): Single<List<NoteDbModel>> {
        val db = ctx.database
        return Single.fromCallable {
            db.getAll()
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