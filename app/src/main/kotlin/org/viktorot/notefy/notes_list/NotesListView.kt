package org.viktorot.notefy.notes_list

import org.viktorot.notefy.base.BaseView
import org.viktorot.notefy.models.NoteDbModel
import org.viktorot.notefy.models.NoteModel

interface NotesListView: BaseView {
    fun showEmptyView()
    fun showLoadingView()
    fun showError()
    fun showNotes(notes: List<NoteModel>)

    fun navigateToNote(note: NoteModel)
}