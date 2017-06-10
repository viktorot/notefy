package org.viktorot.notefy.data

import android.util.Log

class TaskModel {

    companion object {
        @JvmStatic
        val TAG: String = TaskModel::class.java.simpleName.toString()
    }

    var duration: Int = 0
        get() = field
        set(value) {
            field = value
        }

    fun onClick() {
        duration += 1
        Log.d(TAG, duration.toString())
    }

}