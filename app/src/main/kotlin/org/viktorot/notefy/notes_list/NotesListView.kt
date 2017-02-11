package org.viktorot.notefy.notes_list

import org.viktorot.notefy.base.BaseView
import org.viktorot.notefy.models.NoteDbModel

interface NotesListView: BaseView {
    fun showEmptyView()
    fun showLoadingView()
    fun showError()
    fun showNotes(notes: List<NoteDbModel>)

    fun navigateToNote(note: NoteDbModel)
}