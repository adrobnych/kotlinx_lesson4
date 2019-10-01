package com.codegemz.kotlinx.lesson4

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder

const val ACTION_PLAY_MUSIC = "com.codegemz.kotlinx.lesson4.action.PLAY_MUSIC"
const val ACTION_STOP_MUSIC = "com.codegemz.kotlinx.lesson4.action.STOP_MUSIC"
const val ACTION_STOP_SERVICE = "com.codegemz.kotlinx.lesson4.action.STOP_SERVICE"

/**
 * A [Service] is an application component that can perform long-running operations
 * in the background, and it doesn't provide a user interface.
 */
class MyService : Service() {
    private lateinit var mediaPlayer: MediaPlayer
    private var binder: LocalBinder = LocalBinder()

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        // creating player instance for playing music
        mediaPlayer = MediaPlayer.create(this, R.raw.bell)
        mediaPlayer.setOnCompletionListener {
            binder.onPlayingStopped?.invoke()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        onHandleIntent(intent)
        return START_STICKY
    }

    private fun onHandleIntent(intent: Intent?) {
        when (intent?.action) {
            ACTION_PLAY_MUSIC -> {
                if (mediaPlayer.isPlaying) {
                    mediaPlayer.stop()
                    mediaPlayer.prepare()
                }
                mediaPlayer.start()
                binder.onPlayingStarted?.invoke()
            }
            ACTION_STOP_MUSIC -> {
                if (mediaPlayer.isPlaying) {
                    mediaPlayer.stop()
                    mediaPlayer.prepare()
                    binder.onPlayingStopped?.invoke()
                }
            }
            ACTION_STOP_SERVICE -> {
                if (mediaPlayer.isPlaying) {
                    mediaPlayer.stop()
                }
                stopSelf()
            }
        }
    }


    companion object {
        // Start play music
        fun play(context: Context) {
            with(Intent(context, MyService::class.java)) {
                action = ACTION_PLAY_MUSIC
                context.startService(this)
            }
        }

        // Stop music
        fun stop(context: Context) {
            with(Intent(context, MyService::class.java)) {
                action = ACTION_STOP_MUSIC
                context.startService(this)
            }
        }

        // Stop service
        fun stopService(context: Context) {
            val intent = Intent(context, MyService::class.java)
            intent.action = ACTION_STOP_SERVICE
            context.startService(intent)
        }

        // Get play intent by context
        fun getPlayIntent(context: Context): Intent {
            val intent = Intent(context, MyService::class.java)
            intent.action = ACTION_PLAY_MUSIC
            return intent;
        }
    }

    // Binder class implementation. Used to connection
    inner class LocalBinder : Binder() {
        var onPlayingStarted: (() -> Unit)? = null
        var onPlayingStopped: (() -> Unit)? = null
    }
}
