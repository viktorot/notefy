package org.viktorot.notefy

import android.app.Application
import android.content.Context
import com.facebook.stetho.Stetho
import org.viktorot.notefy.db.NoteDbHelper

class NotefyApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }
    }
}

val String.Companion.empty: String
    get() = ""

val Context.database: NoteDbHelper
    get() = NoteDbHelper.getInstance(this)

val Context.timestamp: Int
    get() = (System.currentTimeMillis() / 1000).toInt()