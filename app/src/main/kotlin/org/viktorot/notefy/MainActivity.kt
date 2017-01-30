package org.viktorot.notefy

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import org.viktorot.notefy.base.ViewCallbacks
import org.viktorot.notefy.note.NoteDetailsFragment

import kotlinx.android.synthetic.main.activity_main.*
import org.viktorot.notefy.notes_list.NoteListFragment

class MainActivity : AppCompatActivity(), ViewCallbacks {

    companion object {
        @JvmStatic
        val TAG:String = MainActivity::class.java.simpleName
    }

    val isBackstackEmpty: Boolean
        get() = supportFragmentManager.backStackEntryCount == 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        fab.setOnClickListener { view -> navigateToNote() }

        navigateToNoteList()
    }

    override fun onResume() {
        super.onResume()
        setToolbar()
    }

    override fun onBackPressed() {
        if (isBackstackEmpty) {
            finish()
        }
        else {
            supportFragmentManager.popBackStackImmediate()
            updateFabVisibility()
        }
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

    private fun setToolbar() {
        toolbar.title = "[Notefy]"
        setSupportActionBar(toolbar)
    }

    private fun navigateToNoteList() {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_holder, NoteListFragment.newInstance())
                .commit()
    }

    private fun navigateToNote() {
        supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_right_out, R.anim.slide_right_in, R.anim.slide_right_out)
                .replace(R.id.fullscreen_container, NoteDetailsFragment.newInstance())
                .addToBackStack(NoteDetailsFragment.TAG)
                .commit()

        showFab(false)
    }


    override fun closeFragment() {
        onBackPressed()
    }

    private fun updateFabVisibility() {
        showFab(isBackstackEmpty)
    }

    fun showFab(show: Boolean = true) {
        when (show) {
            true -> fab.visibility = View.VISIBLE
            false -> fab.visibility = View.GONE
        }
    }
}
