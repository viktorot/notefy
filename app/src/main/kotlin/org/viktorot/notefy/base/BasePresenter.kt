package org.viktorot.notefy.base

import org.viktorot.notefy.repo.NoteRepository

abstract class BasePresenter(private val repo: NoteRepository, private val view: BaseView) {
    abstract fun cleanUp()
}