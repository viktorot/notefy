package org.viktorot.notefy.data

import io.reactivex.Single
import java.util.*

object NoteListModel {

    var notes: List<NoteDbModel> = Collections.emptyList()

    fun getNotes(): Single<List<NoteDbModel>> {
        return Single.fromCallable {
            arrayOf(1, 2, 3, 4, 5).map { i ->
                NoteDbModel(i, "title $i", "content", 0, false, i)
            }
        }
    }

}