package org.viktorot.notefy.note

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.*
import kotlinx.android.synthetic.main.fragment_note_details.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.onClick
import org.jetbrains.anko.uiThread
import org.viktorot.notefy.R
import org.viktorot.notefy.base.MainActivityCallback
import org.viktorot.notefy.database
import org.viktorot.notefy.dialogs.IconPickerDialog
import org.viktorot.notefy.timestamp

class NoteDetailsFragment : Fragment() {

    companion object {
        @JvmStatic
        val TAG:String = NoteDetailsFragment::class.java.simpleName

        @JvmStatic
        fun newInstance(): NoteDetailsFragment {
            val fragment = NoteDetailsFragment()

            val args: Bundle = Bundle()
            fragment.arguments = args

            return fragment
        }
    }

    var isNew: Boolean = true
    var iconResId: Int = R.drawable.ic_bugdroid_vector
    var isPinned: Boolean = false

    lateinit var listener: MainActivityCallback

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        try {
            listener = activity as MainActivityCallback
        }
        catch (ex: ClassCastException) {
            throw ClassCastException("$activity must implement base.MainActivityCallback")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.note, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)

        val pinItem: MenuItem? = menu?.getItem(0)
        setPinnedIconState(pinItem)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_note_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isPinned = false

        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        toolbar.title = "[New note]"
        toolbar.navigationIcon = ContextCompat.getDrawable(context, R.drawable.ic_arrow_back)

        image_btn.onClick { showIconPopup() }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_pin_note -> {
                isPinned = !isPinned
                setPinnedIconState(item)
            }
            R.id.action_save_note -> {
                saveNote()
            }
            else -> return false
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveNote () {
        addNote(title.text.toString(), content.text.toString(), iconResId, isPinned)
    }

    private fun setPinnedIconState(item: MenuItem?) {
        val pinDrawable: Drawable? = item?.icon
        when (isPinned) {
            true -> pinDrawable?.setTint(Color.RED)
            false -> pinDrawable?.setTint(Color.BLACK)
        }
    }

    private fun showIconPopup() {
        val popup = IconPickerDialog.newInstance()
        popup.onIconSelected = { imgResId: Int ->
            iconResId = imgResId
            image_btn.imageResource = iconResId
        }

        popup.show(childFragmentManager, IconPickerDialog.TAG)
    }

    private fun addNote(title: String, content: String, image: Int, pinned: Boolean) {
        doAsync {
            val result = context.database.add(title, content, image, pinned, context.timestamp)
            uiThread {
                Log.d(TAG, "note added. result => $result")
                Snackbar.make(root_view, "[Note added]", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}