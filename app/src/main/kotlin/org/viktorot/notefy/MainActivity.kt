package org.viktorot.notefy

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import org.viktorot.notefy.base.ViewCallbacks
import org.viktorot.notefy.note.NoteDetailsFragment

import kotlinx.android.synthetic.main.activity_main.*
import org.viktorot.notefy.controllers.NoteListController

class MainActivity : AppCompatActivity(), ViewCallbacks {

    companion object {
        @JvmStatic
        val TAG:String = MainActivity::class.java.simpleName
    }

    val isBackstackEmpty: Boolean
        get() = supportFragmentManager.backStackEntryCount == 0

        lateinit var router: Router

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            setContentView(R.layout.activity_main)

            fab.setOnClickListener { view ->
                navigateToNote()
            }

        router = Conductor.attachRouter(this, controller_container, savedInstanceState)
        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(NoteListController()))
        }

        setToolbar()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }

    override fun onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed()
        }

//        if (isBackstackEmpty) {
//            finish()
//        }
//        else {
//            supportFragmentManager.popBackStackImmediate()
//            showFab(isBackstackEmpty)
//        }
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

    fun showFab(show: Boolean = true) {
        when (show) {
            true -> fab.visibility = View.VISIBLE
            false -> fab.visibility = View.GONE
        }
    }
}
