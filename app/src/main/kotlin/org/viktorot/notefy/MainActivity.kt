package org.viktorot.notefy

import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.onClick
import org.viktorot.notefy.base.MainActivityCallback
import org.viktorot.notefy.notes_list.NoteListController
import org.viktorot.notefy.note.NoteDetailsController
import timber.log.Timber

class MainActivity : AppCompatActivity(), MainActivityCallback {

    lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        supportActionBar?.title = "[Notify]"

        router = Conductor.attachRouter(this, controller_container, savedInstanceState)
        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(NoteListController()))
        }

        fab.onClick { openNote() }

        showFab(true)
        initToolbar()
    }

    override fun onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed()
        }
    }

    private fun initToolbar() {
        showBackArrow(false)
        toolbar.setNavigationOnClickListener {
            router.popCurrentController()
        }
    }


    private fun openNote() {
        val args: Bundle = Bundle()
        args.putBoolean(NoteDetailsController.IS_NEW_ARG, true)

        router.pushController(RouterTransaction.with(NoteDetailsController(args)))

        showFab(false)
    }

    override fun showFab(show: Boolean) {
        when (show) {
            true -> fab.visibility = View.VISIBLE
            false -> fab.visibility = View.GONE
        }
    }

    override fun showBackArrow(show: Boolean) {
        when (show) {
            true -> toolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back)
            false -> toolbar.navigationIcon = null
        }
    }

    override fun setBackIcon(@DrawableRes iconResId: Int) {
        toolbar.navigationIcon = ContextCompat.getDrawable(this, iconResId)
    }

    override fun resetTitle() {
        supportActionBar?.title = "[Notify]"
    }
}
