package org.viktorot.notefy.services

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.util.Log

class PollService : IntentService(PollService.TAG) {

	companion object {
		private val TAG = "PollService"

		fun newIntent(context: Context): Intent {
			return Intent(context, PollService::class.java)
		}
	}

    override fun onHandleIntent(intent: Intent) {
        Log.i(TAG, "Received an intent: " + intent)
    }

}
