package org.viktorot.notefy.dialogs

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.viktorot.notefy.R


/**
 * Created by viktorot on 11/14/16.
 */

class ImagePickerDialog : BottomSheetDialogFragment() {

    companion object {
        @JvmStatic
        val TAG: String = ImagePickerDialog::class.java.simpleName

        @JvmStatic
        fun newInstance():  ImagePickerDialog {
            return ImagePickerDialog()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (inflater != null) {
            val v: View = inflater.inflate(R.layout.dialog_image_list, container, false)
            return v
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
