package org.viktorot.notefy.notes_list

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.RouterTransaction
import kotlinx.android.synthetic.main.controller_note_list.view.*
import org.jetbrains.anko.dimen
import org.viktorot.notefy.R
import org.viktorot.notefy.base.BaseController
import org.viktorot.notefy.base.MainActivityCallback
import org.viktorot.notefy.models.NoteDbModel
import org.viktorot.notefy.models.NoteModel
import org.viktorot.notefy.note.NoteDetailsController
import org.viktorot.notefy.repo.NotesRepository
import org.viktorot.notefy.view.GridAutofitLayoutManager

class NoteListController : BaseController(), NotesListView {

    companion object {
        @JvmStatic
        val TAG: String = NoteListController::class.java.simpleName
    }

    private lateinit var adapter: NoteListAdapter
    private lateinit var presenter: NoteListPresenter

    lateinit var callback: MainActivityCallback

    private fun attachCallbacks() {
        try {
            callback = activity as MainActivityCallback
        } catch (ex: ClassCastException) {
            throw ClassCastException("$activity must implement MainActivityCallback")
        }
    }

    override fun inflateView(inflater: LayoutInflater, parent: ViewGroup): View {
        return inflater.inflate(R.layout.controller_note_list, parent, false)
    }

    override fun onViewCreated(view: View) {
        super.onViewCreated(view)

        attachCallbacks()

        presenter = NoteListPresenter(NotesRepository(view.context), this)
        presenter.getNotes()

        initRecyclerView(view)
    }

    override fun onDestroyView(view: View) {
        presenter.cleanUp()
        super.onDestroyView(view)
    }

    private fun initRecyclerView(v: View) {
        //val itemSize: Int = applicationContext!!.dimen(R.dimen.grid_item_size)
        v.note_list_recycler.layoutManager = LinearLayoutManager(applicationContext)

        adapter = NoteListAdapter(presenter::onNoteClick, presenter::onPinToggled)
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

        callback.showFab(false)
    }

}