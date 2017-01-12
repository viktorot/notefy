package org.viktorot.notefy.db

object NoteDbContract {

    @JvmField
    val VERSION: Int = 1

    @JvmField
    val TABLE_NAME: String = "NoteDatabase"
    @JvmField
    val PRIMARY_KEY: String = "_id"
    @JvmField
    val TITLE: String = "title"
    @JvmField
    val CONTENT: String = "content"
    @JvmField
    val TIMESTAMP: String = "timestamp"
//    @JvmField
//    val IMAGE: String = "image"

}