package org.viktorot.notefy.note

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.*
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.controller_note_details.view.*
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.onClick
import org.viktorot.notefy.R
import org.viktorot.notefy.base.BaseController
import org.viktorot.notefy.base.MainActivityCallback
import org.viktorot.notefy.dialogs.IconPickerDialog
import org.viktorot.notefy.repo.NotesRepository
import org.viktorot.notefy.utils.TextViewTextObservable

class NoteDetailsController(args: Bundle) : BaseController(args), NoteDetailsView {

    companion object {
        @JvmStatic val TAG: String = NoteDetailsController::class.java.simpleName

        @JvmStatic val IS_NEW_ARG: String = "IS_NEW"
        @JvmStatic val NOTE: String = "NOTE"
    }

    lateinit var callback: MainActivityCallback
    lateinit var presenter: NoteDetailsPresenter

    lateinit var pinMenuItem: MenuItem

    lateinit var titleSubscription: Disposable
    lateinit var contentSubscription: Disposable

    init {
        setHasOptionsMenu(true)
    }

    private fun attachCallbacks() {
        try {
            callback = activity as MainActivityCallback
        } catch (ex: ClassCastException) {
            throw ClassCastException("$activity must implement MainActivityCallback")
        }
    }

    override fun inflateView(inflater: LayoutInflater, parent: ViewGroup): View {
        return inflater.inflate(R.layout.controller_note_details, parent, false)
    }

    override fun onViewCreated(view: View) {
        super.onViewCreated(view)

        attachCallbacks()

        val isNew: Boolean = args.getBoolean(IS_NEW_ARG, false)
        Log.d(TAG, "new => $isNew")

        initToolbar()

        presenter = NoteDetailsPresenter(NotesRepository(view.context), this)
        presenter.init()

        titleSubscription = TextViewTextObservable(view.title)
                .subscribe { value: CharSequence -> presenter.onTitleUpdate(value.toString()) }

        contentSubscription = TextViewTextObservable(view.content)
                .subscribe { value: CharSequence -> presenter.onContentUpdate(value.toString()) }

        view.image_btn.onClick { showIconPopup() }
    }

    override fun onDestroyView(view: View) {
        callback.showFab(true)
        callback.showBackArrow(false)
        callback.resetTitle()

        titleSubscription.dispose()
        contentSubscription.dispose()

        super.onDestroyView(view)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.note, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)

        pinMenuItem = menu.getItem(0)
        presenter.updatePinnedState()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_pin_note -> {
                presenter.togglePinned()
            }
            R.id.action_save_note -> {
                presenter.printModel()
            }
            else -> return false
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initToolbar() {
        actionBar?.title = "[New note]"
        callback.showBackArrow(true)
    }

    private fun showIconPopup() {
        val popup = IconPickerDialog.newInstance()
        popup.onIconSelected = { iconResId: Int ->
            presenter.onIconUpdate(iconResId)
        }

        popup.show(fragmentManager, IconPickerDialog.TAG)
    }

    override fun setTitle(title: String) {
        view?.title?.setText(title)
    }

    override fun setPinned(pinned: Boolean) {
        val pinDrawable: Drawable = pinMenuItem.icon
        when (pinned) {
            true -> pinDrawable.setTint(Color.RED)
            false -> pinDrawable.setTint(Color.BLACK)
        }
    }

    override fun setIcon(iconResId: Int) {
        view?.image_btn?.imageResource = iconResId
    }

    override fun showSaveSuccess() {
        val v: View = view ?: return
        Snackbar.make(v, "[Note saved]", Snackbar.LENGTH_SHORT).show()
    }

    override fun showSaveError() {
        val v: View = view ?: return
        Snackbar.make(v, "[Error saving note]", Snackbar.LENGTH_SHORT).show()
    }
}