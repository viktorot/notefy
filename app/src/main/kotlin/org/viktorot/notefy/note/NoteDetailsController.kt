package org.viktorot.notefy.note

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.view.*
import android.widget.ImageButton
import android.widget.TextView
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.controller_note_details.view.*
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.onClick
import org.viktorot.notefy.R
import org.viktorot.notefy.base.BaseController
import org.viktorot.notefy.base.MainActivityCallback
import org.viktorot.notefy.dialogs.IconPickerDialog
import org.viktorot.notefy.models.NoteModel
import org.viktorot.notefy.repo.NoteRepository
import org.viktorot.notefy.utils.TextViewTextObservable

class NoteDetailsController(args: Bundle) : BaseController(args), NoteDetailsView {

    companion object {
        @JvmStatic val TAG: String = NoteDetailsController::class.java.simpleName

        @JvmStatic val IS_NEW_ARG: String = "IS_NEW"
        @JvmStatic val NOTE: String = "NOTE"
    }

    lateinit var icon: ImageButton
    lateinit var title: TextView
    lateinit var content: TextView

    lateinit var callback: MainActivityCallback
    lateinit var presenter: NoteDetailsPresenter

    lateinit var pinMenuItem: MenuItem
    lateinit var saveMenuItem: MenuItem

    lateinit var titleSubscription: Disposable
    lateinit var contentSubscription: Disposable

    var menuInflated: Boolean = false

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

    override fun bindViews(view: View) {
        icon = view.findViewById(R.id.icon) as ImageButton
        title = view.findViewById(R.id.title) as TextView
        content = view.findViewById(R.id.content) as TextView
    }

    override fun onViewCreated(view: View) {
        super.onViewCreated(view)

        attachCallbacks()

        val note: NoteModel? = args.getParcelable<NoteModel?>(NOTE)

        initToolbar()

        this.presenter = NoteDetailsPresenter(NoteRepository(view.context), this)
        when (note == null) {
            true -> this.presenter.init()
            false -> this.presenter.init(note as NoteModel)
        }

        this.titleSubscription = TextViewTextObservable(view.title)
                .subscribe { value: CharSequence -> this.presenter.onTitleUpdate(value.toString()) }

        this.contentSubscription = TextViewTextObservable(view.content)
                .subscribe { value: CharSequence -> this.presenter.onContentUpdate(value.toString()) }

        this.icon.onClick { showIconPopup() }
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
        saveMenuItem = menu.getItem(1)

        menuInflated = true

        presenter.updateMenuState()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_pin_note -> {
                presenter.togglePinned()
            }
            R.id.action_save_note -> {
                presenter.saveChanges()
            }
            else -> return false
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initToolbar() {
        this.actionBar?.title = "[New note]"
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
        this.title.text = title
    }

    override fun setContent(title: String) {
        this.content.text = title
    }

    override fun setPinned(pinned: Boolean) {
        if (!this.menuInflated) return

        // TODO: compat drawable
        val pinDrawable: Drawable = pinMenuItem.icon.mutate()
        when (pinned) {
            true -> pinDrawable.setTint(ContextCompat.getColor(applicationContext, R.color.colorAccent))
            false -> pinDrawable.setTint(Color.BLACK)
        }
    }

    override fun enableSaving(enable: Boolean) {
        if (!this.menuInflated) return

        saveMenuItem.isEnabled = enable

        // TODO: compat drawable
        val saveDrawable: Drawable = saveMenuItem.icon.mutate()
        when (enable) {
            true -> saveDrawable.setTint(Color.BLACK)
            false -> saveDrawable.setTint(Color.LTGRAY)
        }
    }

    override fun setIcon(iconResId: Int) {
        this.icon.imageResource = iconResId
    }

    override fun showSaveSuccess(message: String) {
        val v: View = view ?: return
        Snackbar.make(v, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun showSaveError() {
        val v: View = view ?: return
        Snackbar.make(v, "[Error saving note]", Snackbar.LENGTH_SHORT).show()
    }
}