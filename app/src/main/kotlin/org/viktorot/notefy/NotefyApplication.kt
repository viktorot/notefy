package org.viktorot.notefy

import android.app.Application
import android.content.Context
import com.facebook.stetho.Stetho
import net.danlew.android.joda.JodaTimeAndroid
import org.viktorot.notefy.db.NoteDbHelper
import org.viktorot.notefy.repo.NoteRepository
import timber.log.Timber

class NotefyApplication: Application() {

    companion object {
        lateinit var ctx: Context
    }

    override fun onCreate() {
        super.onCreate()

        NotefyApplication.ctx = applicationContext

        Timber.plant(Timber.DebugTree())

        JodaTimeAndroid.init(this)

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }
    }
}