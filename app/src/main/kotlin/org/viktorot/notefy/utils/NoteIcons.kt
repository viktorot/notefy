package org.viktorot.notefy.utils

import android.support.annotation.DrawableRes
import org.viktorot.notefy.R

object NoteIcons {

    private val ICONS: Map<Int, Int> = mapOf(
            R.drawable.ic_bugdroid_vector to 0,
            R.drawable.ic_check_vector to 1,
            R.drawable.ic_paperclip_vector to 2,
            R.drawable.ic_paint_brush_vector to 3,
            R.drawable.ic_comment_vector to 4,
            R.drawable.ic_error_vector to 5,
            R.drawable.ic_food_vector to 6,
            R.drawable.ic_heart_vector to 7,
            R.drawable.ic_note_vector to 8,
            R.drawable.ic_puzzle_vector to 9,
            R.drawable.ic_car_vector to 10,
            R.drawable.ic_train_vector to 11
    )

    val DEFAULT_ID: Int = 0

    val DEFAULT_RES_ID: Int = R.drawable.ic_bugdroid_vector

    val ICON_RES_LIST: List<Int> get() = ICONS.keys.toList()

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