package org.viktorot.notefy.notes_list

import org.viktorot.notefy.base.BaseView
import org.viktorot.notefy.data.NoteModel

interface NotesListView: BaseView {
    fun showEmptyView()
    fun showLoadingView()
    fun showError()
    fun showNotes(notes: List<NoteModel>)

    fun onNotesDeleted(positions: List<Int>)

    fun updateNote(note: NoteModel)
    fun navigateToNote(note: NoteModel)
}