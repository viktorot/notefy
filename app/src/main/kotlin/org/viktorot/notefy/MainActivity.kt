package org.viktorot.notefy

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import org.viktorot.notefy.base.ViewCallbacks
import org.viktorot.notefy.note.NoteFragment

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ViewCallbacks {

    companion object {
        @JvmStatic
        val TAG:String = MainActivity::class.java.simpleName
    }

    var toolbar:Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val fab = findViewById(R.id.fab) as FloatingActionButton

        toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view -> navigateToNote() }
    }

    override fun onBackPressed() {
        supportFragmentManager.popBackStackImmediate()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == android.R.id.home) {
            supportFragmentManager.popBackStackImmediate()
            return true
        }
        else {
            return super.onOptionsItemSelected(item)
        }
    }

    private fun navigateToNote() {
        supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_right_out, R.anim.slide_right_in, R.anim.slide_right_out)
                .replace(R.id.container, NoteFragment.newInstance())
                .addToBackStack(NoteFragment.TAG)
                .commit()

        showFab(false)
    }

    override fun closeFragment() {
        supportFragmentManager.popBackStack()
        showFab()
    }

    fun showFab(show: Boolean = true) {
        when (show) {
            true -> fab.visibility = View.VISIBLE
            false -> fab.visibility = View.GONE
        }
    }

}
