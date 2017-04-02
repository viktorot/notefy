package org.viktorot.notefy.base

import android.support.annotation.DrawableRes

interface MainActivityCallback {
    fun resetTitle()
    fun showFab(show: Boolean)
    fun showBackArrow(show: Boolean)
    fun setBackIcon(@DrawableRes iconResId: Int)
    fun showDarkStatusBar(show: Boolean)
}