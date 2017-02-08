package org.viktorot.notefy.models

import org.viktorot.notefy.empty

class NoteDbModel(
        val id: Int,
        val title: String,
        val content: String,
        val image: Int,
        val pinned: Boolean,
        val timestamp: Int) {

    companion object {
        val default: NoteDbModel
                get() = NoteDbModel(-1, String.empty, String.empty, -1, false, -1)
    }

    override fun equals(other: Any?): Boolean {
        if (other is NoteDbModel) {
            return other.id == this.id
        }

        return false
    }

    override fun toString(): String {
        return "Note: id => $id, title => $title, content => $content, pinned = $pinned, timestamp => $timestamp"
    }
}