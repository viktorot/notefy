package org.viktorot.notefy.view

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import org.viktorot.notefy.NotefyApplication
import org.viktorot.notefy.R;

class DeletePopupActivity : AppCompatActivity() {

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_delete_popup)

        val builder = AlertDialog.Builder(this)
        builder.setMessage("Cyka")
                .setPositiveButton("Edit", { dialog, id -> Toast.makeText(this.applicationContext, "Delete", Toast.LENGTH_SHORT).show() })
                .setNegativeButton("Cancel", { dialog, id -> Toast.makeText(this.applicationContext, "Cancel", Toast.LENGTH_SHORT).show() })
                .setOnDismissListener({ this.finish() })

        builder.create().show()
    }

}
