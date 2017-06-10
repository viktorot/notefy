package org.viktorot.notefy.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import org.jetbrains.anko.db.*
import org.viktorot.notefy.data.NoteDbModel

class NoteDbHelper(ctx: Context) : ManagedSQLiteOpenHelper(ctx, NoteDbContract.TABLE_NAME, null, NoteDbContract.VERSION) {

    companion object {
        @JvmField
        val TAG: String = NoteDbHelper::class.java.simpleName

        @JvmStatic
        private var instance: NoteDbHelper? = null

        @JvmStatic
        @Synchronized
        fun getInstance(ctx: Context): NoteDbHelper {
            if (instance == null) {
                instance = NoteDbHelper(ctx)
            }
            return instance!!
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        if (db == null) {
            Log.e(TAG, "db is null")
            return
        }

        db.createTable(NoteDbContract.TABLE_NAME, true,
                NoteDbContract.PRIMARY_KEY to INTEGER + PRIMARY_KEY + UNIQUE,
                NoteDbContract.TITLE to TEXT + NOT_NULL,
                NoteDbContract.CONTENT to TEXT + NOT_NULL,
                NoteDbContract.TIMESTAMP to INTEGER,
                NoteDbContract.IMAGE to INTEGER,
                NoteDbContract.PINNED to INTEGER
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (db == null) {
            Log.e(TAG, "db is null")
            return
        }

        db.dropTable(NoteDbContract.TABLE_NAME, true)
    }

    fun add(title: String, content: String, iconId: Int, pinned: Boolean, timestamp: Int): Long {
        return use {
            insert(NoteDbContract.TABLE_NAME,
                    NoteDbContract.TITLE to title,
                    NoteDbContract.CONTENT to content,
                    NoteDbContract.IMAGE to iconId,
                    NoteDbContract.PINNED to if(pinned) 1 else 0,
                    NoteDbContract.TIMESTAMP to timestamp)
        }
    }

    fun update(id: Int, title: String, content: String, iconId: Int, pinned: Boolean, timestamp: Int): Int {
        return use {
            update(NoteDbContract.TABLE_NAME,
                    NoteDbContract.TITLE to title,
                    NoteDbContract.CONTENT to content,
                    NoteDbContract.IMAGE to iconId,
                    NoteDbContract.PINNED to if(pinned) 1 else 0,
                    NoteDbContract.TIMESTAMP to timestamp)
                    .where("${NoteDbContract.PRIMARY_KEY} = {id}", "id" to id)
                    .exec()
        }
    }

    fun delete(id: Int): Int {
        return use {
            delete(NoteDbContract.TABLE_NAME, "${NoteDbContract.PRIMARY_KEY} = {id}", "id" to id)
        }
    }

    fun setPinned(id: Int, pinned: Boolean): Int {
        return use {
            update (NoteDbContract.TABLE_NAME, NoteDbContract.PINNED to if(pinned) 1 else 0)
                    .where("${NoteDbContract.PRIMARY_KEY} = {id}", "id" to id)
                    .exec()
        }
    }

    fun getAll(): List<NoteDbModel> {
        return use {
            select(NoteDbContract.TABLE_NAME).exec {
                parseList(rowParser { id: Int, title: String,  content: String, timestamp: Int, image: Int, pinned: Int  ->
                    NoteDbModel(id, title, content, image, pinned == 1, timestamp)
                })
            }
        }
    }
}