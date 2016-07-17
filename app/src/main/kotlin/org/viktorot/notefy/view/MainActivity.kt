package org.viktorot.notefy.view

import android.graphics.Rect
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import org.viktorot.notefy.R
import org.viktorot.notefy.adapters.NumberedAdapter

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.notefy_activity_main)

//        var iconRecyclerView = findViewById(R.id.icon_grid) as RecyclerView
//        iconRecyclerView.addItemDecoration(MarginItemDecoration(10))
//        iconRecyclerView.layoutManager = GridLayoutManager(this, 2)
//        iconRecyclerView.adapter = NumberedAdapter(30)

//        val animator = DefaultItemAnimator()
//        animator.changeDuration = 1000
//
//        iconRecyclerView.itemAnimator = animator

        var btn = findViewById(R.id.btn) as ImageButton
        btn.setOnClickListener { view ->
            ViewAnimationUtils.createCircularReveal(view, view.width, view.height, 0f, view.height * 2f).start()
        }

        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view -> Toast.makeText(this.applicationContext, "Kwic", Toast.LENGTH_SHORT).show() }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.notefy_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        Snackbar.make(toolbar, "Jungle Boogie", Snackbar.LENGTH_LONG).show()

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}

private class MarginItemDecoration(size: Int) : RecyclerView.ItemDecoration() {
    private val margin: Int

    init {
        margin = size
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        outRect.set(margin, margin, margin, margin)
    }
}
