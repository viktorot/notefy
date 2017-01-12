package org.viktorot.notefy.dialogs

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.viktorot.notefy.R

class ImagePickerDialog : BottomSheetDialogFragment() {

    companion object {
        @JvmStatic
        val TAG: String = ImagePickerDialog::class.java.simpleName

        @JvmStatic
        fun newInstance():  ImagePickerDialog {
            return ImagePickerDialog()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_image_list, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}
