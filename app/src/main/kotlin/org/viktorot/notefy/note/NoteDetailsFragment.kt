package org.viktorot.notefy.note

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.onClick
import org.jetbrains.anko.uiThread
import org.viktorot.notefy.R
import org.viktorot.notefy.base.ViewCallbacks
import org.viktorot.notefy.db.NoteDbContract
import org.viktorot.notefy.db.NoteDbHelper

import kotlinx.android.synthetic.main.fragment_note_details.*
import org.jetbrains.anko.imageResource
import org.viktorot.notefy.dialogs.IconPickerDialog

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

    lateinit var listener: ViewCallbacks

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        try {
            listener = activity as ViewCallbacks
        }
        catch (ex: ClassCastException) {
            throw ClassCastException("$activity must implement base.ViewCallbacks")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_note_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.title = "[Just a test]"
        toolbar.navigationIcon = ContextCompat.getDrawable(context, R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener {
            listener.closeFragment()
        }

        image_btn.onClick {
            showIconPopup()
        }
    }

    private fun showIconPopup() {
        val popup = IconPickerDialog.newInstance()
        popup.onIconSelected = { iconResId: Int ->
            image_btn.imageResource = iconResId
        }

        popup.show(childFragmentManager, IconPickerDialog.TAG)
    }

    private fun addNote(title: String) {
        val noteDb: NoteDbHelper = NoteDbHelper.getInstance(context)

        doAsync {
            val result = noteDb.use {
                insert(NoteDbContract.TABLE_NAME,
                        NoteDbContract.TITLE to title,
                        NoteDbContract.CONTENT to "[placeholder]",
                        NoteDbContract.TIMESTAMP to 123)
            }
            uiThread {
                Log.d(TAG, "note added. result => $result")
            }
        }
    }

}