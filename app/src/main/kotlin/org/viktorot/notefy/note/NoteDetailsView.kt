package org.viktorot.notefy.note

import org.viktorot.notefy.base.BaseView

interface NoteDetailsView: BaseView {
    fun loadNote(id: Int)
    fun saveNote()
    fun showSaveSuccess()
    fun showSaveError()
}