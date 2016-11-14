package org.viktorot.notefy.note

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.viktorot.notefy.R

import kotlinx.android.synthetic.main.fragment_new_note.*
import org.viktorot.notefy.base.ViewCallbacks

class NoteFragment : Fragment() {

    companion object {
        @JvmStatic
        val TAG:String = NoteFragment::class.java.simpleName

        fun newInstance() : NoteFragment {
            val fragment = NoteFragment()

            val args = Bundle()
            fragment.arguments = args

            return fragment
        }
    }

    lateinit var listener: ViewCallbacks

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        try {
            listener = activity as ViewCallbacks
        }
        catch (ex: ClassCastException) {
            throw ClassCastException("${activity} must implement base.ViewCallbacks")
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if(inflater != null) {
            val layout = inflater.inflate(R.layout.fragment_new_note, container, false)
            return layout
        }
        else {
            Log.e(TAG, "inflater is null")
            return null
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.title = "[Just a test]"
        toolbar.navigationIcon = ContextCompat.getDrawable(context, R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener {
            listener.closeFragment()
        }

    }

}