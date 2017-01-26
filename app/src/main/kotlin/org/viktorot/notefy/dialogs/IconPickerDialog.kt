package org.viktorot.notefy.dialogs

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.ImageView
import org.viktorot.notefy.R

import kotlinx.android.synthetic.main.dialog_icon_list.*
import org.jetbrains.anko.onClick

class IconPickerDialog : BottomSheetDialogFragment() {

    companion object {
        @JvmStatic
        val TAG: String = IconPickerDialog::class.java.simpleName

        @JvmStatic
        fun newInstance(): IconPickerDialog {
            return IconPickerDialog()
        }
    }

    val GRID_SIZE:Int = 2

    val iconList:List<Int> = listOf (
            R.drawable.ic_bugdroid_vector,
            R.drawable.ic_check_vector,
            R.drawable.ic_paperclip_vector,
            R.drawable.ic_paint_brush_vector
    )

    var clickAction: (Int) -> Unit = { Log.w(TAG, "click action not set") }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_icon_list, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var col:Int = 0
        var row: Int = 0

        val iconSize = context.resources.getDimension(R.dimen.icon_item_size).toInt()

        iconList.forEach { iconResId ->
            val button = ImageButton(context)
            val layoutParams: GridLayout.LayoutParams = GridLayout.LayoutParams()

            layoutParams.height = iconSize
            layoutParams.width = iconSize
            layoutParams.rowSpec = GridLayout.spec(row, GridLayout.CENTER)
            layoutParams.rowSpec = GridLayout.spec(col, GridLayout.CENTER)

            button.scaleType = ImageView.ScaleType.FIT_XY
            button.layoutParams = layoutParams
            button.setImageResource(iconResId)

            Log.d(TAG, "row => $row, col => $col")

            grid.addView(button)

            col++
            if (col == GRID_SIZE) {
                row++
                col = 0
            }
        }

    }

    fun doOnClick(action: (Int) -> Unit) {
        clickAction = action
    }
}
