package org.viktorot.notefy.note

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.ActionBar
import android.util.Log
import android.view.*
import kotlinx.android.synthetic.main.controller_note_details.view.*
import org.jetbrains.anko.onClick
import org.viktorot.notefy.R
import org.viktorot.notefy.base.BaseController
import org.viktorot.notefy.base.MainActivityCallback
import org.viktorot.notefy.dialogs.IconPickerDialog
import org.viktorot.notefy.models.NoteDbModel
import org.viktorot.notefy.repo.NotesRepository

class NoteDetailsController(args: Bundle): BaseController(args), NoteDetailsView {

    companion object {
        @JvmStatic val  TAG: String = NoteDetailsController::class.java.simpleName

        @JvmStatic val IS_NEW_ARG: String = "IS_NEW"
        @JvmStatic val NOTE: String = "NOTE"
    }

    lateinit var callback: MainActivityCallback
    lateinit var presenter: NoteDetailsPresenter

    var iconResId: Int = R.drawable.ic_bugdroid_vector
    var isPinned: Boolean = false

    init {
        setHasOptionsMenu(true)
    }

    private fun attachCallbacks() {
        try {
            callback = activity as MainActivityCallback
        }
        catch (ex: ClassCastException) {
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
        val note: NoteDbModel = NoteDbModel.default
        Log.d(TAG, "new => $isNew")

        presenter = NoteDetailsPresenter(NotesRepository(view.context), this)
        presenter.init()

        initToolbar()

        view.image_btn.onClick {
            showIconPopup()
        }
    }

    override fun onDestroyView(view: View) {
        callback.showFab(true)
        callback.showBackArrow(false)
        callback.resetTitle()

        super.onDestroyView(view)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.note, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)

        val pinItem: MenuItem = menu.getItem(0)
        setPinnedIconState(pinItem)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_pin_note -> {
                isPinned = !isPinned
                setPinnedIconState(item)
            }
            R.id.action_save_note -> {
                presenter.saveNote("111", "222", 0, true)
            }
            else -> return false
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initToolbar() {
        actionBar?.title = "[New note]"
        callback.showBackArrow(true)
    }

    private fun setPinnedIconState(item: MenuItem) {
        val pinDrawable: Drawable = item.icon
        when (isPinned) {
            true -> pinDrawable.setTint(Color.RED)
            false -> pinDrawable.setTint(Color.BLACK)
        }
    }

    private fun showIconPopup() {
        val popup = IconPickerDialog.newInstance()
        popup.onIconSelected = { imgResId: Int ->
            iconResId = imgResId
            //imageBtn.imageResource = iconResId
        }

        popup.show(fragmentManager, IconPickerDialog.TAG)
    }

    override fun loadNote(id: Int) {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun saveNote() {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showSaveSuccess() {
        val v:View = view ?: return
        Snackbar.make(v, "[Note saved]", Snackbar.LENGTH_SHORT).show()
    }

    override fun showSaveError() {
        val v:View = view ?: return
        Snackbar.make(v, "[Error saving note]", Snackbar.LENGTH_SHORT).show()
    }
}