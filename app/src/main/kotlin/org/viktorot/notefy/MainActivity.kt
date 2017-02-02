package org.viktorot.notefy

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.onClick
import org.viktorot.notefy.base.MainActivityCallback
import org.viktorot.notefy.controllers.HomeController
import org.viktorot.notefy.controllers.NoteDetailsController

class MainActivity : AppCompatActivity(), MainActivityCallback {

    companion object {
        @JvmStatic
        val TAG: String = MainActivity::class.java.simpleName
    }

    lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        router = Conductor.attachRouter(this, controller_container, savedInstanceState)
        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(HomeController()))
        }

        fab.onClick {
            openNote()
        }

        showFab()
    }

    override fun onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed()
        }
    }

    private fun openNote() {
        val args: Bundle = Bundle()
        args.putInt("size", 101)

        router.pushController(RouterTransaction.with(NoteDetailsController(args)))

        showFab(false)
    }

    private fun showFab() {
        showFab(true)
    }

    override fun showFab(show: Boolean) {
        when (show) {
            true -> fab.visibility = View.VISIBLE
            false -> fab.visibility = View.GONE
        }
    }
}
