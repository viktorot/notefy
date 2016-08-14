package org.viktorot.notefy.note

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.viktorot.notefy.R

class NoteFragment : Fragment() {

    companion object {
        @JvmStatic
        val TAG:String = NoteFragment::class.java.simpleName.toString()

        fun newInstance() : NoteFragment {
            val fragment = NoteFragment()

            val args = Bundle()
            fragment.arguments = args

            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if(inflater != null) {
            val layout = inflater.inflate(R.layout.new_note_fragment, container, false)
            return layout
        }
        else {
            Log.w(TAG, "inflater is null")
            return null
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}