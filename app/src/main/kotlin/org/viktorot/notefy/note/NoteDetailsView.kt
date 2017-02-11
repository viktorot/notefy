package org.viktorot.notefy.note

import org.viktorot.notefy.base.BaseView

interface NoteDetailsView: BaseView {
    fun setTitle(title: String)
    fun setPinned(pinned: Boolean)

    fun showSaveSuccess()
    fun showSaveError()
}