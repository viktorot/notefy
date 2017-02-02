package org.viktorot.notefy.controllers

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.*
import android.widget.ImageButton
import android.widget.ImageView
import kotlinx.android.synthetic.main.fragment_note_details.*
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.onClick
import org.viktorot.notefy.R
import org.viktorot.notefy.base.BaseController
import org.viktorot.notefy.base.MainActivityCallback
import org.viktorot.notefy.dialogs.IconPickerDialog

class NoteDetailsController(args: Bundle): BaseController(args) {

    lateinit var toolbar: Toolbar
    lateinit var imageBtn: ImageButton
    lateinit var callback: MainActivityCallback

    var isNew: Boolean = true
    var iconResId: Int = R.drawable.ic_bugdroid_vector
    var isPinned: Boolean = false

    override fun inflateView(inflater: LayoutInflater, parent: ViewGroup): View {
        return inflater.inflate(R.layout.fragment_note_details, parent, false)
    }

    override fun onAttach(view: View) {
        super.onAttach(view)

        try {
            callback = activity as MainActivityCallback
        }
        catch (ex: ClassCastException) {
            throw ClassCastException("$activity must implement MainActivityCallback")
        }
    }

    override fun onViewCreated(view: View) {
        super.onViewCreated(view)

        toolbar = view.findViewById(R.id.toolbar) as Toolbar
        setupToolbar()

        imageBtn = view.findViewById(R.id.image_btn) as ImageButton
        imageBtn.onClick {
            showIconPopup()
        }
    }

    override fun onDestroyView(view: View) {
        callback.showFab(true)
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

    private fun setupToolbar() {
        toolbar.title = "[New note]"
        toolbar.navigationIcon = ContextCompat.getDrawable(applicationContext, R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener {
            router.popCurrentController()
        }
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
            imageBtn.imageResource = iconResId
        }

        popup.show(fragmentManager, IconPickerDialog.TAG)
    }
}