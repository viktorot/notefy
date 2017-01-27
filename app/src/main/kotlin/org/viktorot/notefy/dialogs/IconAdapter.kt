package org.viktorot.notefy.dialogs

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.onClick
import org.viktorot.notefy.R
import java.util.ArrayList

class IconAdapter(ctx: Context, items: ArrayList<Int>, clickAction: (Int) -> Unit) : RecyclerView.Adapter<IconAdapter.ViewHolder>() {

    private val items: ArrayList<Int>
    private val ctx: Context
    private val clickAction: (Int) -> Unit

    init {
        this.items = items
        this.ctx = ctx
        this.clickAction = clickAction
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.item_icon, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val iconResId: Int = items[position]
        holder.iconBtn.imageResource = iconResId
        holder.iconBtn.onClick { clickAction(iconResId) }
    }

    override fun getItemCount(): Int {
        return items.size
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val iconBtn: ImageButton = view.findViewById(R.id.image_btn) as ImageButton
    }

}