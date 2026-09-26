package com.ivangames.trolljump

import android.os.Bundle
import android.view.MotionEvent
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var gameView: GameView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gameView = findViewById(R.id.gameView)

        val btnLeft = findViewById<Button>(R.id.btnLeft)
        val btnRight = findViewById<Button>(R.id.btnRight)
        val btnJump = findViewById<Button>(R.id.btnJump)

        btnLeft.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> gameView.moveLeft = true
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> gameView.moveLeft = false
            }
            true
        }

        btnRight.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> gameView.moveRight = true
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> gameView.moveRight = false
            }
            true
        }

        btnJump.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> gameView.jump = true
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> gameView.jump = false
            }
            true
        }
    }
}
