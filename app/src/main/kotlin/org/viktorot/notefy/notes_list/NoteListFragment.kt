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
import org.viktorot.notefy.database
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
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
        loadNotes()
    }

    override fun onPause() {
        Log.d(TAG, "onPause")
        super.onPause()
    }

    override fun onStop() {
        Log.d(TAG, "onStop")
        super.onStop()
    }

    private fun loadNotes() {
        doAsync {
            val result = context.database.getAll()
            uiThread {
                adapter.setItems(result)
            }
        }
    }
}