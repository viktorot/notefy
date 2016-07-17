package org.viktorot.notefy.adapters

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import org.viktorot.notefy.R

import java.util.ArrayList

class NumberedAdapter(count: Int) : RecyclerView.Adapter<TextViewHolder>() {
    private val labels: MutableList<String>

    companion object {
        private val TAG: String = NumberedAdapter::class.java.simpleName
    }

    init {
        labels = ArrayList<String>(count)
        for (i in 0..count - 1) {
            labels.add(i.toString())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.icon_grid_item, parent, false)
        return TextViewHolder(view)
    }

    override fun onBindViewHolder(holder: TextViewHolder, position: Int) {
        holder.rootLayout.setOnClickListener {
            holder.selected = !holder.selected

            if(holder.selected) {
//                holder.imageView.setColorFilter(Color.RED)
                holder.rootLayout.elevation = 10f
            }
            else {
//                holder.imageView.setColorFilter(Color.BLACK)
                holder.rootLayout.elevation = 0f
            }

//            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int {
        return labels.size
    }
}

class TextViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//    var textView: TextView
    var imageView: ImageView
    var rootLayout: FrameLayout
    var selected: Boolean

    init {
        rootLayout = itemView.findViewById(R.id.rootLayout) as FrameLayout
        imageView = itemView.findViewById(R.id.icon) as ImageView
        selected = false
    }
}