package org.viktorot.notefy

import android.app.Application
import android.content.Context
import android.text.TextUtils
import com.facebook.stetho.Stetho
import org.viktorot.notefy.db.NoteDbHelper

class NotefyApplication: Application() {

    companion object {
        lateinit var ctx: Context
    }

    override fun onCreate() {
        super.onCreate()

        NotefyApplication.ctx = applicationContext

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