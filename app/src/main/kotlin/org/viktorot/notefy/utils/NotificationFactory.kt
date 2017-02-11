package org.viktorot.notefy.utils

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationManagerCompat
import android.support.v7.app.NotificationCompat
import org.viktorot.notefy.NotefyApplication
import org.viktorot.notefy.R;
import org.viktorot.notefy.view.DeletePopupActivity

object NotificationFactory {

    fun displayNormalNotification(ctx: Context) {
        val resultIntent = Intent(ctx, DeletePopupActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(ctx, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(ctx)
        builder.setSmallIcon(R.mipmap.ic_launcher)
        builder.setContentTitle("Notify")
        builder.setContentText("This is just a tribute!")
        builder.setContentIntent(pendingIntent)

        val manager = NotificationManagerCompat.from(ctx)
        manager.notify(101, builder.build())
    }

}
