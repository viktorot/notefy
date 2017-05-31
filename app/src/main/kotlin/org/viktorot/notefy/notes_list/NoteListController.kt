package org.viktorot.notefy.notes_list

import android.os.Bundle
import android.support.v7.view.ActionMode
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import com.bluelinelabs.conductor.RouterTransaction
import org.viktorot.notefy.R
import org.viktorot.notefy.base.BaseController
import org.viktorot.notefy.base.MainActivityCallback
import org.viktorot.notefy.empty
import org.viktorot.notefy.models.NoteModel
import org.viktorot.notefy.note.NoteDetailsController
import org.viktorot.notefy.repository

class NoteListController : BaseController(), NotesListView {

    private lateinit var adapter: NoteListAdapter

    private var actionMode: ActionMode? = null

    private val actionModeActive: Boolean get() = actionMode != null

    private lateinit var recycler: RecyclerView

    private lateinit var loadingView: View

    private lateinit var emptyView: View

    private lateinit var errorView: View

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

        recycler = view.findViewById(R.id.note_list_recycler) as RecyclerView
        emptyView = view.findViewById(R.id.empty_view)
        loadingView = view.findViewById(R.id.loading_view)
        errorView = view.findViewById(R.id.error_view)

        initRecyclerView()

        presenter.init()
        presenter.getNotes()
    }

    override fun onDestroyView(view: View) {
        presenter.cleanUp()

        super.onDestroyView(view)
    }

    private fun initRecyclerView() {
        recycler.layoutManager = LinearLayoutManager(applicationContext)

        adapter = NoteListAdapter(
                this::onItemClick,
                presenter::onPinToggled,
                this::onItemLongPress,
                this::onSelectionStateChanged,
                this::onItemSelected
        )

        recycler.adapter = adapter
    }

    private fun onItemClick(id: Int) {
        presenter.onNoteClick(id)
    }

    private fun onItemLongPress(id: Int, position: Int) {

    }

    private fun onSelectionStateChanged(active: Boolean) {
        when (active) {
            true -> {
                when (actionModeActive) {
                    true -> updateActionModeTitle()
                    false -> {
                        startActionMode()
                        updateActionModeTitle()
                    }
                }
            }
            false -> {
                closeActionMode()
            }
        }
    }

    private fun onItemSelected(position: Int, selected: Boolean) {
        updateActionModeTitle()
    }

    private fun startActionMode() {
        actionMode = appCompatActivity!!.startSupportActionMode(ActionModeCallback())
    }

    private fun closeActionMode() {
        adapter.clearSelection()

        actionMode?.finish()
        actionMode = null
    }

    private fun updateActionModeTitle() {
        val count = adapter.selectedCount
        when (count > 0) {
            true -> actionMode?.title = "[$count selected]"
            false -> actionMode?.title = String.empty
        }
    }

    override fun updateNote(note: NoteModel) {
        adapter.updateItem(note)
    }

    override fun showNotes(notes: List<NoteModel>) {
        errorView.visibility = View.GONE
        loadingView.visibility = View.GONE
        emptyView.visibility = View.GONE
        recycler.visibility = View.VISIBLE
        adapter.setItems(notes)
    }

    override fun showLoadingView() {
        errorView.visibility = View.GONE
        loadingView.visibility = View.VISIBLE
        emptyView.visibility = View.GONE
        recycler.visibility = View.GONE
    }

    override fun showEmptyView() {
        errorView.visibility = View.GONE
        loadingView.visibility = View.GONE
        emptyView.visibility = View.VISIBLE
        recycler.visibility = View.GONE
    }

    override fun showError() {
        errorView.visibility = View.VISIBLE
        loadingView.visibility = View.GONE
        emptyView.visibility = View.GONE
        recycler.visibility = View.GONE
    }

    override fun navigateToNote(note: NoteModel) {
        val args = Bundle()
        args.putParcelable(NoteDetailsController.NOTE, note)
        router?.pushController(RouterTransaction.with(NoteDetailsController(args)))

        callback.showFab(false)
    }

    override fun onNotesDeleted(positions: List<Int>) {
        adapter.removeItem(positions)
    }

    private fun deleteNote(positions: List<Int>) {
        presenter.deleteNote(positions)
    }

    /*
        ACTION MODE
     */

    inner class ActionModeCallback: ActionMode.Callback {

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            return true
        }

        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            mode ?: return false

            mode.menuInflater.inflate(R.menu.action_mode, menu)
            return true
        }

        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            item ?: return false

            when (item.itemId) {
                R.id.action_delete_note -> {
                    deleteNote(adapter.selectedIndices)
                    closeActionMode()
                }
            }

            return true
        }

        override fun onDestroyActionMode(mode: ActionMode?) {
            this@NoteListController.closeActionMode()
        }

    }

}