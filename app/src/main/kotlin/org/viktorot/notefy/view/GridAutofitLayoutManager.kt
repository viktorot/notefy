package org.viktorot.notefy.view

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue

class GridAutofitLayoutManager: GridLayoutManager {

    private var columnWidth: Int = 0
    private var columnWidthChanged: Boolean = true

    constructor(context: Context?, columnWidth: Int) : super(context, 1) {
        setColumnWidth(checkColumnWidth(context, columnWidth))
    }

    constructor(context: Context?, columnWidth: Int, orientation: Int, reverseLayout: Boolean) : super(context, 1, orientation, reverseLayout) {
        setColumnWidth(checkColumnWidth(context, columnWidth))
    }

    private fun checkColumnWidth(context: Context?, columnWidth: Int): Int {
        val ctx: Context = context ?: return columnWidth

        if (columnWidth <= 0) {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48f, ctx.resources.displayMetrics).toInt()
        }

        return columnWidth
    }

    fun setColumnWidth(newWidth: Int) {
        if (newWidth > 0 && newWidth != this.columnWidth) {
            this.columnWidth = newWidth
            this.columnWidthChanged = true
        }
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        if (this.columnWidthChanged && this.columnWidth > 0 && width > 0 && height > 0) {
            var totalSpace: Int
            if (orientation == LinearLayoutManager.VERTICAL) {
                totalSpace = width - paddingRight - paddingLeft
            }
            else {
                totalSpace = height - paddingTop - paddingBottom
            }

            val spanCount = Math.max(1, totalSpace / this.columnWidth)
            setSpanCount(spanCount)
            columnWidthChanged = false
        }

        super.onLayoutChildren(recycler, state)
    }

}