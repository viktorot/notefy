package org.viktorot.notefy.notes_list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.onClick
import org.viktorot.notefy.R
import org.viktorot.notefy.models.NoteModel

class NoteListAdapter(val itemClickCallback: (id: Int) -> Unit) : RecyclerView.Adapter<NoteViewHolder>() {
    private val items: MutableList<NoteModel> = mutableListOf()

    init {
    }

    fun setItems(newItems: List<NoteModel>) {
        items.clear()
        items.addAll(newItems)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        val itemView = NoteViewHolder(inflatedView)
        return itemView
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note: NoteModel = items[position]

        holder.icon.imageResource = note.icon
        holder.title.text = note.title
        holder.timestamp.text = note.timestamp.toString()

        holder.rootView.onClick { itemClickCallback(note.id) }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val rootView: View = itemView.findViewById(R.id.root_view)
    val icon: ImageView = itemView.findViewById(R.id.icon) as ImageView
    val title: TextView = itemView.findViewById(R.id.title) as TextView
    val timestamp: TextView = itemView.findViewById(R.id.timestamp) as TextView
}