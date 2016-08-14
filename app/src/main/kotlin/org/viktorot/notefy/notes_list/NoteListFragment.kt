package org.viktorot.notefy.notes_list

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.viktorot.notefy.R
import org.viktorot.notefy.note.NoteFragment

class NoteListFragment : Fragment() {

    companion object {
        @JvmStatic
        val TAG:String = NoteListFragment::class.java.simpleName.toString()

        fun newInstance() : NoteListFragment {
            return NoteListFragment()
        }
    }

        override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if(inflater != null) {
            val view = inflater.inflate(R.layout.notes_list, container, false)
            return view
        }
        else {
            Log.w(TAG, "inflater is null")
            return null
        }
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}