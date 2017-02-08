package org.viktorot.notefy.base

import org.viktorot.notefy.repo.NotesRepository

abstract class BasePresenter(private val repo: NotesRepository, private val view: BaseView) {
    abstract fun cleanUp()
}