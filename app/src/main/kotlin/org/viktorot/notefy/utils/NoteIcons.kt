package org.viktorot.notefy.utils

import android.support.annotation.DrawableRes
import org.viktorot.notefy.R

object NoteIcons {

    private val ICONS: Map<Int, Int> = mapOf(
            Pair(R.drawable.ic_bugdroid_vector, 0),
            Pair(R.drawable.ic_check_vector, 1),
            Pair(R.drawable.ic_paperclip_vector, 2),
            Pair(R.drawable.ic_paint_brush_vector, 3)
    )

    val DEFAULT_ID: Int = 0

    val DEFAULT_RES_ID: Int = R.drawable.ic_bugdroid_vector

    val iconResList: List<Int> get() = ICONS.keys.toList()

    fun getId(iconResId: Int): Int {
        return ICONS.getOrElse(iconResId, { return DEFAULT_ID })
    }

    @DrawableRes
    fun getResId(iconId: Int): Int {
        for ((key, value) in ICONS) {
            if (value == iconId) return key
        }

        return DEFAULT_RES_ID
    }

}