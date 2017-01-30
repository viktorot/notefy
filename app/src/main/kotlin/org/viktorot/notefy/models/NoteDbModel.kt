package org.viktorot.notefy.models

class NoteDbModel(
        val id: Int,
        val title: String,
        val content: String,
        val image: Int,
        val pinned: Boolean,
        val timestamp: Int) {

    override fun toString(): String {
        return "Note: id => $id, title => $title, content => $content, pinned = $pinned, timestamp => $timestamp"
    }
}