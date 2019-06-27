package com.codegemz.kotlinx.lesson4

import android.app.IntentService
import android.content.Intent
import android.media.MediaPlayer
import org.jetbrains.anko.runOnUiThread
import org.jetbrains.anko.toast

const val ACTION_PLAY_MUSIC = "com.codegemz.kotlinx.lesson4.action.PLAY_MUSIC"

/**
 * An [IntentService] subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
class IntentService : IntentService("IntentService") {
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer.create(this, R.raw.bell)
    }

    override fun onHandleIntent(intent: Intent?) {
        when (intent?.action) {
            ACTION_PLAY_MUSIC -> {
                mediaPlayer.start();
                runOnUiThread {
                    toast("Play");
                }
            }
        }
    }
}
