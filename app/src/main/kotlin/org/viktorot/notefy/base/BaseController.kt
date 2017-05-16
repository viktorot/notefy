package org.viktorot.notefy.base

import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller
import org.viktorot.notefy.NotefyApplication

abstract class BaseController: Controller {

    companion object {
        @JvmStatic
        val TAG: String = BaseController::class.java.simpleName
    }

    constructor(): super()

    constructor(args: Bundle): super(args)

    abstract fun inflateView(inflater: LayoutInflater, parent: ViewGroup): View

    open fun onViewCreated(view: View) { }

    open fun bindViews(view: View) { }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view: View = inflateView(inflater, container)

        bindViews(view)
        onViewCreated(view)
        return view
    }

    override fun onDestroyView(view: View) {
        super.onDestroyView(view)
    }

    val appCompatActivity: AppCompatActivity?
        get() {
            if (activity != null) {
                try {
                    return activity as AppCompatActivity
                }
                catch (ex: ClassCastException) {
                    Log.e(TAG, ex.toString())
                    return null
                }
            }
            else {
                return null
            }
        }

    val notefyApp: NotefyApplication
        get() = applicationContext as NotefyApplication

    val actionBar: ActionBar?
        get() = appCompatActivity!!.supportActionBar

    val fragmentManager: FragmentManager?
        get() = appCompatActivity!!.supportFragmentManager

}