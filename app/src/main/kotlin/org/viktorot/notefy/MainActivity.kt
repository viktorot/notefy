package org.viktorot.notefy

import android.content.Intent
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.onClick
import org.viktorot.notefy.base.MainActivityCallback
import org.viktorot.notefy.note.NoteDetailsController
import org.viktorot.notefy.notes_list.NoteListController
import org.viktorot.notefy.repo.NoteRepository
import org.viktorot.notefy.utils.Constants
import timber.log.Timber

class MainActivity : AppCompatActivity(), MainActivityCallback {

    lateinit var router: Router

    var changeTypeDisposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate")
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        supportActionBar?.title = "[Notify]"

        router = Conductor.attachRouter(this, controller_container, savedInstanceState)
        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(NoteListController()))
        }

        changeTypeDisposable = applicationContext!!.repository.changeTypeObservable()
                .subscribe({ type: Int ->
                    when (type) {
                        NoteRepository.CHANGE_SAVE -> showUpdateSnackbar("[Note saved]")
                        NoteRepository.CHANGE_UPDATE -> showUpdateSnackbar("[Note updated]")
                        else -> Timber.w("unknown update state => $type")
                    }
                    Timber.v("change type => $type")
                })

        fab.onClick { newNote() }

        showFab(true)

        initToolbar()

        val data: Int = intent.getIntExtra(Constants.NOTE_ID, -1)
        Timber.v("--- id => $data")
        if (data > -1) {
            // TODO: open note
            Timber.v("--- backstack size => ${router.backstackSize}")
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Timber.d("onNewIntent")
    }

    override fun onDestroy() {
        Timber.d("onDestroy")
        changeTypeDisposable?.dispose()
        super.onDestroy()
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


    private fun showUpdateSnackbar(text: String) {
        Snackbar.make(root_view, text, Snackbar.LENGTH_SHORT).show()
    }

    private fun newNote() {
        val args: Bundle = Bundle()
        args.putBoolean(NoteDetailsController.IS_NEW_ARG, true)

        router.pushController(RouterTransaction.with(NoteDetailsController(args)))

        showFab(false)
    }

    private fun openNote(id: Int) {

    }

    override fun showDarkStatusBar(show: Boolean) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        val statusBarColor: Int
        val toolBarColor: Int

        when (show) {
            true ->  {
                statusBarColor = ContextCompat.getColor(this, R.color.gray_900)
                toolBarColor = ContextCompat.getColor(this, R.color.gray_dark)
            }
            false -> {
                statusBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)
                toolBarColor = ContextCompat.getColor(this, R.color.colorPrimary)
            }
        }

        window.statusBarColor = statusBarColor
        toolbar.backgroundColor = toolBarColor
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
