package org.viktorot.notefy.utils

import android.content.Context
import org.viktorot.notefy.db.NoteDbHelper
import org.viktorot.notefy.repo.NoteRepository

val String.Companion.empty: String
    get() = ""

val Context.database: NoteDbHelper
    get() = NoteDbHelper.getInstance(this)

val Context.repository: NoteRepository
    get() = NoteRepository.instance(this)

val Context.timestamp: Int
    get() = (System.currentTimeMillis() / 1000).toInt()