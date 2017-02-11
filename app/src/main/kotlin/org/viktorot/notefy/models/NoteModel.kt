package org.viktorot.notefy.models

import org.viktorot.notefy.empty
import org.viktorot.notefy.utils.NoteIcons

class NoteModel(var id: Int, var title: String, var content: String, var icon: Int, var pinned: Boolean, var timestamp: Int) {


    companion object {
        val empty: NoteModel
            get() = NoteModel(-1, String.empty, String.empty, NoteIcons.DEFAULT_ID, false, -1)
    }

    override fun toString(): String {
        return "Note: id => $id, title => $title, content => $content, icon => $icon, pinned => $pinned"
    }

    override fun equals(other: Any?): Boolean {
        if (other is NoteModel) {
            return other.id == this.id
        }

        return false
    }

    override fun hashCode(): Int {
        return id
    }
}