package org.viktorot.notefy.notes_list

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.RouterTransaction
import kotlinx.android.synthetic.main.controller_note_list.view.*
import org.viktorot.notefy.R
import org.viktorot.notefy.base.BaseController
import org.viktorot.notefy.models.NoteDbModel
import org.viktorot.notefy.models.NoteModel
import org.viktorot.notefy.note.NoteDetailsController
import org.viktorot.notefy.repo.NotesRepository

class NoteListController : BaseController(), NotesListView {

    companion object {
        @JvmStatic
        val TAG: String = NoteListController::class.java.simpleName
    }

    private lateinit var adapter: NoteListAdapter
    private lateinit var presenter: NoteListPresenter

    override fun inflateView(inflater: LayoutInflater, parent: ViewGroup): View {
        return inflater.inflate(R.layout.controller_note_list, parent, false)
    }

    override fun onViewCreated(view: View) {
        super.onViewCreated(view)

        initRecyclerView(view)

        presenter = NoteListPresenter(NotesRepository(view.context), this)
        presenter.getNotes()
    }

    override fun onDestroyView(view: View) {
        presenter.cleanUp()
        super.onDestroyView(view)
    }

    private fun initRecyclerView(v: View) {
        v.note_list_recycler.layoutManager = GridLayoutManager(applicationContext, 2)

        adapter = NoteListAdapter({ id -> presenter.onNoteClick(id) })
        v.note_list_recycler.adapter = adapter
    }

    override fun showNotes(notes: List<NoteModel>) {
        val v: View = view ?: return

        v.error_view.visibility = View.GONE
        v.loading_view.visibility = View.GONE
        v.empty_view.visibility = View.GONE
        v.note_list_recycler.visibility =  View.VISIBLE
        adapter.setItems(notes)
    }

    override fun showLoadingView() {
        val v: View = view ?: return

        v.error_view.visibility = View.GONE
        v.loading_view.visibility = View.VISIBLE
        v.empty_view.visibility = View.GONE
        v.note_list_recycler.visibility =  View.GONE
    }

    override fun showEmptyView() {
        val v: View = view ?: return

        v.error_view.visibility = View.GONE
        v.loading_view.visibility = View.GONE
        v.empty_view.visibility = View.VISIBLE
        v.note_list_recycler.visibility =  View.GONE
    }

    override fun showError() {
        val v: View = view ?: return

        v.error_view.visibility = View.VISIBLE
        v.loading_view.visibility = View.GONE
        v.empty_view.visibility = View.GONE
        v.note_list_recycler.visibility =  View.GONE
    }

    override fun navigateToNote(note: NoteModel) {
        val args = Bundle()
        args.putParcelable(NoteDetailsController.NOTE, note)
        router?.pushController(RouterTransaction.with(NoteDetailsController(args)))
    }

}