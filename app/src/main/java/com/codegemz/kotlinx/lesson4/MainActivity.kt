package com.codegemz.kotlinx.lesson4

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainView().setContentView(this)
    }
}

class MainView : AnkoComponent<MainActivity> {
    override fun createView(ui: AnkoContext<MainActivity>) = with(ui) {
        relativeLayout {
            button("Play music") {
                onClick {
                    val intent = Intent(ctx, IntentService::class.java)
                    intent.setAction(ACTION_PLAY_MUSIC);
                    ctx.startService(intent)
                }
            }.lparams() {
                gravity = Gravity.CENTER_HORIZONTAL
                topMargin = dip(70)
            }
        }
    }
}
