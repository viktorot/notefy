package org.viktorot.notefy.view

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import org.viktorot.notefy.R;
import org.viktorot.notefy.util.NotificationFactory

class EditActivity : AppCompatActivity() {

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        val fab = findViewById(R.id.batn) as FloatingActionButton
        fab.setOnClickListener { view -> NotificationFactory.displayNormalNotification() }
    }}