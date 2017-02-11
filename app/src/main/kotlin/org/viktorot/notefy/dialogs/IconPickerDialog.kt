package org.viktorot.notefy.dialogs

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.dialog_icon_list.*
import org.viktorot.notefy.R
import org.viktorot.notefy.utils.NoteIcons

class IconPickerDialog : BottomSheetDialogFragment() {

    companion object {
        @JvmStatic
        val TAG: String = IconPickerDialog::class.java.simpleName

        @JvmStatic
        fun newInstance(): IconPickerDialog {
            return IconPickerDialog()
        }
    }

    lateinit var adapter: IconAdapter
    val GRID_SIZE:Int = 2

    var onIconSelected: (Int) -> Unit = { Log.w(TAG, "click action not set") }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_icon_list, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun initAdapter() {
        val icons: List<Int> = NoteIcons.ICONS.keys.toList()
        adapter = IconAdapter(context, icons, { resId -> onIconClick(resId) })
    }

    private fun initRecyclerView() {
        initAdapter()

        icon_grid.layoutManager = GridLayoutManager(context, GRID_SIZE)
        icon_grid.adapter = adapter
    }

    private fun onIconClick(iconResId: Int) {
        onIconSelected(iconResId)
        dismiss()
    }
}
