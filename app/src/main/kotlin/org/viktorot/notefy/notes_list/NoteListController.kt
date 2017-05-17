package org.viktorot.notefy.notes_list

import android.os.Bundle
import android.support.v7.view.ActionMode
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.bluelinelabs.conductor.RouterTransaction
import kotlinx.android.synthetic.main.controller_note_list.view.*
import org.viktorot.notefy.R
import org.viktorot.notefy.base.BaseController
import org.viktorot.notefy.base.MainActivityCallback
import org.viktorot.notefy.models.NoteModel
import org.viktorot.notefy.note.NoteDetailsController
import org.viktorot.notefy.repository
import timber.log.Timber

class NoteListController : BaseController(), NotesListView {

    private lateinit var adapter: NoteListAdapter

    private var actionMode: ActionModeCallback? = null

    private val presenter: NoteListPresenter by lazy {
        NoteListPresenter(applicationContext!!.repository, this)
    }

    lateinit var callback: MainActivityCallback

    private fun attachCallbacks() {
        try {
            callback = activity as MainActivityCallback
        }
        catch (ex: ClassCastException) {
            throw ClassCastException("$activity must implement MainActivityCallback")
        }
    }

    override fun inflateView(inflater: LayoutInflater, parent: ViewGroup): View {
        return inflater.inflate(R.layout.controller_note_list, parent, false)
    }

    override fun onViewCreated(view: View) {
        super.onViewCreated(view)
        attachCallbacks()

        presenter.init()
        presenter.getNotes()

        initRecyclerView(view)
    }

    override fun onDestroyView(view: View) {
        presenter.cleanUp()

        super.onDestroyView(view)
    }

    private fun initRecyclerView(v: View) {
        v.note_list_recycler.layoutManager = LinearLayoutManager(applicationContext)

        adapter = NoteListAdapter(this::onItemClick, presenter::onPinToggled, this::onItemLongPress)

        v.note_list_recycler.adapter = adapter
    }

    private fun onItemClick(id: Int) {
        //presenter.onNoteClick(id)
        Timber.v("selected => %d", adapter.selectedCount)
    }

    private fun onItemLongPress(id: Int, position: Int) {
        val am = ActionModeCallback()
        appCompatActivity!!.startSupportActionMode(am)

        actionMode = am
    }

    fun updateActionModeTitle() {
        appCompatActivity?.actionBar?.title = "11111"
    }

    override fun updateNote(note: NoteModel) {
        adapter.updateItem(note)
    }

    override fun showNotes(notes: List<NoteModel>) {
        val v: View = view ?: return

        v.error_view.visibility = View.GONE
        v.loading_view.visibility = View.GONE
        v.empty_view.visibility = View.GONE
        v.note_list_recycler.visibility = View.VISIBLE
        adapter.setItems(notes)
    }

    override fun showLoadingView() {
        val v: View = view ?: return

        v.error_view.visibility = View.GONE
        v.loading_view.visibility = View.VISIBLE
        v.empty_view.visibility = View.GONE
        v.note_list_recycler.visibility = View.GONE
    }

    override fun showEmptyView() {
        val v: View = view ?: return

        v.error_view.visibility = View.GONE
        v.loading_view.visibility = View.GONE
        v.empty_view.visibility = View.VISIBLE
        v.note_list_recycler.visibility = View.GONE
    }

    override fun showError() {
        val v: View = view ?: return

        v.error_view.visibility = View.VISIBLE
        v.loading_view.visibility = View.GONE
        v.empty_view.visibility = View.GONE
        v.note_list_recycler.visibility = View.GONE
    }

    override fun navigateToNote(note: NoteModel) {
        val args = Bundle()
        args.putParcelable(NoteDetailsController.NOTE, note)
        router?.pushController(RouterTransaction.with(NoteDetailsController(args)))

        callback.showFab(false)
    }


    private class ActionModeCallback: ActionMode.Callback {

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            return true
        }

        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            mode ?: return false

            mode.menuInflater.inflate(R.menu.action_mode, menu)
            //NoteListController@this.upda
            return true
        }

        override fun onDestroyActionMode(mode: ActionMode?) {
        }

        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            return false
        }

    }

}