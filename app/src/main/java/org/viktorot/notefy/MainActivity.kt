package org.viktorot.notefy

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import org.viktorot.notefy.note.NoteFragment
import org.viktorot.notefy.notes_list.NoteListFragment

class MainActivity : AppCompatActivity() {

    val NOTE_FRAGMENT_TAG:String = "NoteFragment"
    var toolbar:Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_activity)

        var fab = findViewById(R.id.fab) as FloatingActionButton

        toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        supportFragmentManager.addOnBackStackChangedListener {
            updateToolbar()
        }

        navigateToFragment(NoteListFragment.newInstance(), true)

        fab.setOnClickListener { view ->
            navigateToFragment(NoteFragment.newInstance())
        }
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

    private fun isFragmentStackEmpty(): Boolean {
        return supportFragmentManager?.backStackEntryCount == 0
    }

    private fun navigateToFragment(fragment:Fragment, initial:Boolean = false) {
        if(initial) {
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.container, fragment)
                    .commit()
        }
        else {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, fragment)
                    .addToBackStack(fragment.toString())
                    .commit()
        }
    }

    private fun updateToolbar() {
        if(isFragmentStackEmpty()) {
            toolbar?.setBackgroundResource(R.color.colorPrimaryDark)
            supportActionBar?.setDefaultDisplayHomeAsUpEnabled(false)
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }
        else {
            toolbar?.setBackgroundResource(android.R.color.white)
            supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }
}
