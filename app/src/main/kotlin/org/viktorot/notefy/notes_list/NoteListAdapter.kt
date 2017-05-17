package org.viktorot.notefy.notes_list

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import org.jetbrains.anko.*
import org.jetbrains.anko.collections.asSequence
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.viktorot.notefy.R
import org.viktorot.notefy.models.NoteModel
import timber.log.Timber

class NoteListAdapter(
        val itemClickCallback: (id: Int) -> Unit,
        val pinnedToggleCallback: (note: NoteModel, pinned: Boolean) -> Unit,
        val longPressCallback: (id: Int, pos: Int) -> Unit) : RecyclerView.Adapter<NoteViewHolder>() {

    private val items: MutableList<NoteModel> = mutableListOf()

    private val selections: SparseBooleanArray = SparseBooleanArray()

    init {
    }

    fun setItems(newItems: List<NoteModel>) {
        items.clear()
        items.addAll(newItems)

        notifyDataSetChanged()
    }

    fun updateItem(note: NoteModel) {
        val index: Int = items.indexOf(note)
        if (index != -1) {
            // TODO: payload
            items[index] = note
            notifyItemChanged(index)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        val itemView = NoteViewHolder(inflatedView)
        return itemView
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val ctx = holder.rootCardView.context
        val note: NoteModel = items[position]

        holder.icon.imageResource = note.icon
        holder.title.text = note.title

        val date = DateTime(note.timestamp * 1000L)
        holder.timestamp.text = date.toString(DateTimeFormat.forPattern("d MMMM, yyyy"))

        holder.setPinnedColor(note.pinned)
        holder.pin.onClick {
            val newState = !note.pinned
            pinnedToggleCallback(note, newState)
        }

        holder.rootCardView.onClick {
            itemClickCallback(note.id)
        }

        holder.rootCardView.onLongClick {
            Timber.v("long press on => %d", note.id)
            //longPressCallback(note.id, position)

            toggleSelectedState(position)
            notifyItemChanged(position)

            true
        }

        when (isSelected(position)) {
            true -> {
                holder.rootCardView.setCardBackgroundColor(Color.LTGRAY)
            }
            false -> {
                holder.rootCardView.setCardBackgroundColor(Color.WHITE)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    val selectedCount: Int get() {
//        return selections.asSequence<Boolean>().fold(0, {acc, selected ->
//            Timber.v("----- %b", selected)
//            when(selected) {
//                true -> return@fold acc + 1
//                false -> return@fold acc
//            }
//        })
        var res: Int = 0;
        for (i in 0 .. selections.size()) {
            if (selections.get(i)) res ++
        }
        return res
    }

    /*
           ITEM SELECTION
     */

    private fun toggleSelectedState(position: Int) {
        val selected = isSelected(position)
        selections.put(position, !selected)
    }

    private fun isSelected(position: Int): Boolean {
        return selections.get(position, false)
    }

    fun clearSelection() {
        if (selections.size() == 0) return

        for (i in 0 .. selections.size()) {
            selections.put(i, false)
        }
    }
}

class NoteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val rootCardView: CardView = itemView.findViewById(R.id.root_view) as CardView
    val icon: ImageView = itemView.findViewById(R.id.icon) as ImageView
    val title: TextView = itemView.findViewById(R.id.title) as TextView
    val timestamp: TextView = itemView.findViewById(R.id.timestamp) as TextView
    val pin: ImageButton = itemView.findViewById(R.id.pin) as ImageButton

    fun setPinnedColor(pinned: Boolean) {
        val drawable = pin.backgroundDrawable ?: return
        val mutableDrawable = drawable.mutate()

        when(pinned) {
            true -> {
                val color = ContextCompat.getColor(itemView.context, R.color.colorAccent)
                mutableDrawable.run {
                    DrawableCompat.setTint(mutableDrawable, color)
                    pin.backgroundDrawable = drawable
                }
            }
            false -> {
                mutableDrawable.run {
                    DrawableCompat.setTint(mutableDrawable, Color.BLACK)
                    pin.backgroundDrawable = drawable
                }
            }
        }
    }
}