package org.viktorot.notefy.util

import android.content.Context
import android.support.v4.app.NotificationCompat

object NotificationFactory {

    fun createNotification(ctx: Context) {
        val builder = NotificationCompat.Builder(ctx)
    }

	fun test(): String {
		return "cyka";
	}

}
