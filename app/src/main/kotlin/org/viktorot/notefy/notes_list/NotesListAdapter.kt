package org.viktorot.notefy.notes_list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.viktorot.notefy.R
import org.viktorot.notefy.models.TaskModel

class NotesListAdapter : RecyclerView.Adapter<NoteViewHolder>() {
    private val items: MutableList<TaskModel>

    init {
        items = mutableListOf<TaskModel>()

        for(i in 1..5) {
            val task = TaskModel()
            task.duration = i

            items.add(task)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.icon_grid_item, parent, false)
        val itemView = NoteViewHolder(inflatedView)
        return itemView
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val model: TaskModel = items.get(position)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    init {

    }

}