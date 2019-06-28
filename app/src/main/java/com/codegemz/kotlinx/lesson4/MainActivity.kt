package com.codegemz.kotlinx.lesson4

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.os.Bundle
import android.os.IBinder
import android.view.Gravity
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class MainActivity : AppCompatActivity() {
    private lateinit var view: MainView

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {

        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            if (service is MyService.LocalBinder) {
                service.onPlayingStarted = {
                    view.updatePlayButton(true)
                }
                service.onPlayingStopped = {
                    view.updatePlayButton(false)
                }
            }
            view.updatePlayButton(false)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = MainView()
        view.setContentView(this)

        // Bind service for receiving callbacks
        val intent = Intent(this, MyService::class.java)
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Stop when close the app
        MyService.stopService(this)
        unbindService(serviceConnection)
    }
}

class MainView : AnkoComponent<MainActivity> {
    private lateinit var playButton: Button

    override fun createView(ui: AnkoContext<MainActivity>) = with(ui) {
        relativeLayout {
            playButton = button("Waiting for service..") {

            }.lparams {
                gravity = Gravity.CENTER_HORIZONTAL
                topMargin = dip(70)
            }
        }
    }

    internal fun updatePlayButton(isPlaying: Boolean) {
        with(playButton) {
            if (isPlaying) {
                text = resources.getString(R.string.stop_music)
                setBackgroundColor(Color.CYAN)
                onClick {
                    MyService.stop(context)
                }
            } else {
                text = resources.getString(R.string.play_music)
                setBackgroundColor(Color.GREEN)
                onClick {
                    MyService.play(context)
                }
            }
        }
    }
}
