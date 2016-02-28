package org.viktorot.notefy.util

import android.app.PendingIntent
import android.content.Intent
import android.support.v4.app.NotificationManagerCompat
import android.support.v7.app.NotificationCompat
import org.viktorot.notefy.NotefyApplication
import org.viktorot.notefy.R;
import org.viktorot.notefy.view.DeletePopupActivity

object NotificationFactory {

    fun displayNormalNotification() {
        val resultIntent = Intent(NotefyApplication.context, DeletePopupActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(NotefyApplication.context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(NotefyApplication.context)
        builder.setSmallIcon(R.mipmap.ic_launcher)
        builder.setContentTitle("Notify")
        builder.setContentText("This is just a tribute!")
        builder.setContentIntent(pendingIntent)

        val manager = NotificationManagerCompat.from(NotefyApplication.context)
        manager.notify(101, builder.build())
    }

}
