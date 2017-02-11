package org.viktorot.notefy.utils

import org.viktorot.notefy.R

object NoteIcons {

    val ICONS: Map<Int, Int> = mapOf(
            Pair(R.drawable.ic_bugdroid_vector, 0),
            Pair(R.drawable.ic_check_vector, 1),
            Pair(R.drawable.ic_paperclip_vector, 2),
            Pair(R.drawable.ic_paint_brush_vector, 3)
    )

    val DEFAULT_ID: Int = 0

    fun getId(iconResId: Int): Int {
        return ICONS.getOrElse(iconResId, { return DEFAULT_ID })
    }

}