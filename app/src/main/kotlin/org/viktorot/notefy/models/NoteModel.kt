package org.viktorot.notefy.models

import android.os.Parcel
import android.os.Parcelable
import android.support.annotation.DrawableRes
import org.viktorot.notefy.empty
import org.viktorot.notefy.utils.NoteIcons

class NoteModel: Parcelable {

    companion object {
        @JvmStatic val CREATOR: Parcelable.Creator<NoteModel> = object: Parcelable.Creator<NoteModel> {
            override fun newArray(size: Int): Array<out NoteModel> {
                return newArray(size)
            }

            override fun createFromParcel(source: Parcel): NoteModel {
                return NoteModel(source)
            }
        }

        val empty: NoteModel
            get() = NoteModel(-1, String.empty, String.empty, NoteIcons.DEFAULT_ID, false, -1)
    }

    var id: Int = -1
    var title: String = String.empty
    var content: String = String.empty
    @DrawableRes var icon: Int = -1
    var pinned: Boolean = false
    var timestamp: Int = -1

    constructor(id: Int, title: String, content: String, @DrawableRes icon: Int, pinned: Boolean, timestamp: Int) {
        this.id = id
        this.title = title
        this.content = content
        this.icon = icon
        this.pinned = pinned
        this.timestamp = timestamp
    }

    private constructor(parcel: Parcel) {
        this.id = parcel.readInt()
        this.title = parcel.readString()
        this.content = parcel.readString()
        this.icon = parcel.readInt()
        this.pinned = parcel.readInt() == 1
        this.timestamp = parcel.readInt()
    }

    override fun toString(): String {
        return "Note: id => $id, title => $title, content => $content, icon => $icon, pinned => $pinned"
    }

    override fun equals(other: Any?): Boolean {
        if (other is NoteModel) {
            return other.id == this.id
        }

        return false
    }

    override fun hashCode(): Int {
        return this.id
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.run {
            writeInt(id)
            writeString(title)
            writeString(content)
            writeInt(icon)
            writeInt(if(pinned) 1 else 0)
            writeInt(timestamp)
        }
    }

    override fun describeContents(): Int {
        return this.id
    }
}