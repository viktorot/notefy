package org.viktorot.notefy.notes_list

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import org.viktorot.notefy.R
import org.viktorot.notefy.new_note.NewNoteActivity

class NotesListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.notes_list)

        var iconRecyclerView = findViewById(R.id.noteList) as RecyclerView
        iconRecyclerView.layoutManager = LinearLayoutManager(this)
        iconRecyclerView.adapter = NotesListAdapter()

        var fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view ->
            val intent: Intent = Intent(this, NewNoteActivity::class.java)
            startActivity(intent)
        }

    }

}
