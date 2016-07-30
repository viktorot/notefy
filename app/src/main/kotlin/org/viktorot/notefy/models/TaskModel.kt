package org.viktorot.notefy.models

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.util.Log
import org.viktorot.notefy.BR

class TaskModel : BaseObservable() {

    companion object {
        @JvmStatic
        val TAG: String = TaskModel::class.java.simpleName.toString()
    }

    @get:Bindable
    var duration: Int = 0
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.duration)
        }

    fun onClick() {
        duration += 1
        Log.d(TAG, duration.toString())
    }

}