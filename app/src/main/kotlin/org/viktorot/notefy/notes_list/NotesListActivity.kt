package org.viktorot.notefy.notes_list

import android.graphics.Rect
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import org.viktorot.notefy.R
import org.viktorot.notefy.adapters.NumberedAdapter

class NotesListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.notes_list)

        var iconRecyclerView = findViewById(R.id.icon_grid) as RecyclerView
        iconRecyclerView.layoutManager = GridLayoutManager(this, 2)
        iconRecyclerView.adapter = NumberedAdapter(30)
    }

}

//private class MarginItemDecoration(size: Int) : RecyclerView.ItemDecorationString() {
//    private val margin: Int
//
//    init {
//        margin = size
//    }
//
//    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
//        outRect.set(margin, margin, margin, margin)
//    }
//}
