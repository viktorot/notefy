package org.viktorot.notefy.base

import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.Unbinder
import com.bluelinelabs.conductor.Controller

abstract class BaseController: Controller {

    lateinit var unbinder: Unbinder

    constructor(): super()

    constructor(args: Bundle): super(args)

    abstract fun inflateView(inflater: LayoutInflater, parent: ViewGroup): View

    open fun onViewCreated(view: View) { }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view: View = inflateView(inflater, container)
        unbinder = ButterKnife.bind(this, view)
        onViewCreated(view)
        return view
    }

    override fun onDestroyView(view: View) {
        unbinder.unbind()
        super.onDestroyView(view)
    }

    private val appCompatActivity: AppCompatActivity?
        get() {
            if (activity != null) {
                try {
                    return activity as AppCompatActivity
                }
                catch (ex: ClassCastException) {
                    return null
                }
            }
            else {
                return null
            }
        }

    val fragmentManager: FragmentManager?
        get() {
            val activity: AppCompatActivity? = appCompatActivity
            if (appCompatActivity != null) {
                return appCompatActivity!!.supportFragmentManager
            }
            else {
                return null
            }
        }

}