package com.ivangames.trolljump

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var gameView: GameView
    private lateinit var winOverlay: FrameLayout
    private lateinit var winText: TextView

    private var currentWorld = "forest"
    private var currentLevel = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        currentWorld = intent.getStringExtra("world") ?: "forest"
        currentLevel = intent.getIntExtra("level", 1)

        gameView = findViewById(R.id.gameView)
        gameView.levelNumber = currentLevel
        gameView.world = currentWorld
        winOverlay = findViewById(R.id.winOverlay)
        winText = findViewById(R.id.winText)

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

        gameView.onLevelComplete = {
            ProgressManager.markCompleted(this, currentWorld, currentLevel)
            winText.text = "Уровень $currentLevel пройден!"
            winOverlay.visibility = FrameLayout.VISIBLE
        }

findViewById<Button>(R.id.winBackBtn).setOnClickListener {
    val intent = Intent(this, LevelSelectActivity::class.java)
    intent.putExtra("world", currentWorld)
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
    startActivity(intent)
    finish()
}
    }
}
