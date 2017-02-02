package org.viktorot.notefy.controllers

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.viktorot.notefy.R
import org.viktorot.notefy.base.BaseController

class HomeController() : BaseController() {

    lateinit var overlayContainer: ViewGroup
    lateinit var toolbar: Toolbar

    companion object {
        @JvmStatic
        val TAG: String = HomeController::class.java.simpleName
    }

    override fun inflateView(inflater: LayoutInflater, parent: ViewGroup): View {
        return inflater.inflate(R.layout.controller_home, parent, false)
    }

    override fun onViewCreated(view: View) {
        super.onViewCreated(view)

        overlayContainer = view.findViewById(R.id.overlay_container) as ViewGroup
        toolbar = view.findViewById(R.id.toolbar) as Toolbar

        setupToolbar()
    }

    private fun setupToolbar() {
        toolbar.title = "[Notefy]"
    }
}