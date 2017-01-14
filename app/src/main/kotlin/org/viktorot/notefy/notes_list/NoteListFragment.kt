package org.viktorot.notefy.notes_list

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.viktorot.notefy.R

import kotlinx.android.synthetic.main.fragment_notes_list.*
import org.jetbrains.anko.db.parseList
import org.jetbrains.anko.db.rowParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.viktorot.notefy.db.NoteDbContract
import org.viktorot.notefy.db.NoteDbHelper
import org.viktorot.notefy.models.NoteDbModel

class NoteListFragment : Fragment() {

    companion object {
        @JvmStatic
        val TAG: String = NoteListFragment::class.java.simpleName.toString()

        fun newInstance(): NoteListFragment {
            return NoteListFragment()
        }
    }

    private lateinit var adapter: NoteListAdapter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (inflater != null) {
            val view = inflater.inflate(R.layout.fragment_notes_list, container, false)
            return view
        } else {
            Log.e(TAG, "inflater is null")
            return null
        }
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (view == null) return

        val layoutManger = LinearLayoutManager(view.context)

        note_list_recycler.layoutManager = layoutManger

        adapter = NoteListAdapter()

        note_list_recycler.adapter = adapter

        loadNotes()
    }

    private fun loadNotes() {
        val noteDb: NoteDbHelper = NoteDbHelper.getInstance(context)

        doAsync {
            val result = noteDb.use {
                select(NoteDbContract.TABLE_NAME).exec {
                    parseList(rowParser(::NoteDbModel))
                }
            }
            uiThread {
                adapter.setItems(result)
            }
        }
    }
}