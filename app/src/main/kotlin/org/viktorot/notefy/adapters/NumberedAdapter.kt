package org.viktorot.notefy.adapters

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.viktorot.notefy.BR
import org.viktorot.notefy.R
import org.viktorot.notefy.models.TaskModel

import java.util.ArrayList

class NumberedAdapter(count: Int) : RecyclerView.Adapter<TextViewHolder>() {
    private val labels: MutableList<TaskModel>

    companion object {
        @JvmStatic
        private val TAG: String = NumberedAdapter::class.java.simpleName
    }

    init {
        labels = ArrayList<TaskModel>(count)
        for (i in 0..count - 1) {
            val task = TaskModel()
            task.duration = i

            labels.add(task)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.icon_grid_item, parent, false)
        val itemView = TextViewHolder(inflatedView)
        return itemView
    }

    override fun onBindViewHolder(holder: TextViewHolder, position: Int) {
        val model: TaskModel = labels.get(position)
        holder.binding.setVariable(BR.task, model)
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return labels.size
    }
}

class TextViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var binding: ViewDataBinding

    init {
        binding = DataBindingUtil.bind(itemView)
    }
}