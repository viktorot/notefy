package org.viktorot.notefy.utils

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.app.TaskStackBuilder
import android.support.v7.app.NotificationCompat
import android.text.TextUtils
import org.viktorot.notefy.MainActivity
import org.viktorot.notefy.NotefyApplication
import org.viktorot.notefy.R;
import org.viktorot.notefy.models.NoteModel
import org.viktorot.notefy.view.DeletePopupActivity

object NotificationUtils {

    fun notify(note: NoteModel) {
        when (note.pinned) {
            true -> displayNotification(note)
            false -> removeNotification(note.id)
        }
    }

    private fun displayNotification(note: NoteModel) {
        val stackBuilder = TaskStackBuilder.create(NotefyApplication.ctx)
        stackBuilder.addParentStack(MainActivity::class.java)

        val intent = Intent(NotefyApplication.ctx, MainActivity::class.java)
        intent.action = System.currentTimeMillis().toString()
        intent.putExtra(Constants.NOTE_ID, note.id)

        stackBuilder.addNextIntent(intent)

        val pendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(NotefyApplication.ctx)
        builder.setSmallIcon(note.icon)
        builder.setOngoing(true)
        builder.setContentTitle(note.title)
        if (!note.content.isEmpty()) {
            builder.setContentText(note.content)
        }
        builder.setContentIntent(pendingIntent)

        NotificationManagerCompat.from(NotefyApplication.ctx)
                .notify(note.id, builder.build())
    }

    private fun removeNotification(id: Int) {
        NotificationManagerCompat.from(NotefyApplication.ctx)
                .cancel(id)
    }

}
