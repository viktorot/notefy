package org.viktorot.notefy.utils

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationManagerCompat
import android.support.v7.app.NotificationCompat
import android.text.TextUtils
import org.viktorot.notefy.NotefyApplication
import org.viktorot.notefy.R;
import org.viktorot.notefy.models.NoteModel
import org.viktorot.notefy.view.DeletePopupActivity

object NotificationUtils {

    fun displayNotification(note: NoteModel) {
        val ctx = NotefyApplication.ctx

        val resultIntent = Intent(ctx, DeletePopupActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(ctx, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(ctx)
        builder.setSmallIcon(R.mipmap.ic_launcher)
        builder.setOngoing(true)
        builder.setContentTitle(note.title)
        if (!note.content.isEmpty()) {
            builder.setContentText(note.content)
        }
        //builder.setContentIntent(pendingIntent)

        NotificationManagerCompat.from(ctx)
                .notify(note.id, builder.build())
    }

    fun removeNotification(id: Int) {
        val ctx = NotefyApplication.ctx

        NotificationManagerCompat.from(ctx)
                .cancel(id)
    }

}
