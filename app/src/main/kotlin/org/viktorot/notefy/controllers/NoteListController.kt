package org.viktorot.notefy.controllers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller
import org.viktorot.notefy.R

class NoteListController: Controller() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view: View = inflater.inflate(R.layout.fragment_notes_list, container, false)
        return view
    }


}