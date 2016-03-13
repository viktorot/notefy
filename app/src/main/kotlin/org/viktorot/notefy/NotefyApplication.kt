package org.viktorot.notefy

import android.app.Application
import android.content.Context

class NotefyApplication: Application() {

    companion object Ctx {
        var context: Context? = null
    }

    override fun onCreate() {
        super.onCreate()

        NotefyApplication.context = this.applicationContext;
    }
}
