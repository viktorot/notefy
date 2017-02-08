package org.viktorot.notefy.base

interface MainActivityCallback {
    fun resetTitle()
    fun showFab(show: Boolean)
    fun showBackArrow(show: Boolean)
}