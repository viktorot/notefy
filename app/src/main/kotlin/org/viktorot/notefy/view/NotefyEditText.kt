package org.viktorot.notefy.view

import android.content.Context
import android.util.AttributeSet
import android.widget.EditText
import org.viktorot.notefy.utils.empty
import timber.log.Timber

class NotefyEditText: EditText {

    var cachedHint: String = String.empty

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    private fun init() {
        cachedHint = hint.toString()

        setOnFocusChangeListener { _, hasFocus  ->
            when (hasFocus) {
                true -> hint = String.empty
                false -> hint = cachedHint
            }
        }
    }

}