package org.viktorot.notefy

import android.app.Application
import android.content.Context
import com.facebook.stetho.Stetho
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import org.viktorot.notefy.db.NoteDbHelper

class NotefyApplication: Application() {

    companion object {
        lateinit var ctx: Context
    }

    lateinit var relay: BehaviorRelay<Boolean>

    override fun onCreate() {
        super.onCreate()

        NotefyApplication.ctx = applicationContext

        relay = BehaviorRelay.createDefault(false)

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }
    }

    val relayObservable: Observable<Boolean>
        get() = relay.doAfterNext { state -> if(state) relay.accept(false) }
}

val String.Companion.empty: String
    get() = ""

val Context.database: NoteDbHelper
    get() = NoteDbHelper.getInstance(this)

val Context.timestamp: Int
    get() = (System.currentTimeMillis() / 1000).toInt()