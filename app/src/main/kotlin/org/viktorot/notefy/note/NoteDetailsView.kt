package org.viktorot.notefy.note

import android.support.annotation.DrawableRes
import org.viktorot.notefy.base.BaseView

interface NoteDetailsView: BaseView {
    fun setTitle(title: String)
    fun setContent(title: String)
    fun setPinned(pinned: Boolean)
    fun setIcon(@DrawableRes iconResId: Int)

    fun showSaveSuccess()
    fun showSaveError()
}