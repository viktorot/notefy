package org.viktorot.notefy.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import org.jetbrains.anko.db.*

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
                NoteDbContract.CONTENT to TEXT,
                NoteDbContract.TIMESTAMP to INTEGER)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (db == null) {
            Log.e(TAG, "db is null")
            return
        }

        db.dropTable(NoteDbContract.TABLE_NAME, true)
    }
}

//val Context.database: NoteDbHelper
//    get() = NoteDbHelper.getInstance(applicationContext)