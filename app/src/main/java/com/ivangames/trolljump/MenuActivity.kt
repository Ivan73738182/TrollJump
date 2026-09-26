package com.ivangames.trolljump

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val forestBtn = findViewById<LinearLayout>(R.id.forestBtn)
        val mountainsBtn = findViewById<LinearLayout>(R.id.mountainsBtn)
        val seaBtn = findViewById<LinearLayout>(R.id.seaBtn)
        val line1 = findViewById<View>(R.id.line1)
        val line2 = findViewById<View>(R.id.line2)

        // Лес — всегда открыт
        forestBtn.setOnClickListener { openWorld("forest") }

        // Горы — открыт, если Лес пройден (5/5)
        if (ProgressManager.isWorldUnlocked(this, "mountains")) {
            mountainsBtn.background = getDrawable(R.drawable.world_circle)
            mountainsBtn.backgroundTintList = ColorStateList.valueOf(0xFF2E7D32.toInt())
            mountainsBtn.setOnClickListener { openWorld("mountains") }
        } else {
            mountainsBtn.setOnClickListener {
                android.widget.Toast.makeText(this, "Сначала пройди Лес!", android.widget.Toast.LENGTH_SHORT).show()
            }
        }

        // Море — открыт, если Горы пройдены (5/5)
        if (ProgressManager.isWorldUnlocked(this, "sea")) {
            seaBtn.background = getDrawable(R.drawable.world_circle)
            seaBtn.backgroundTintList = ColorStateList.valueOf(0xFF1565C0.toInt())
            seaBtn.setOnClickListener { openWorld("sea") }
        } else {
            seaBtn.setOnClickListener {
                android.widget.Toast.makeText(this, "Сначала пройди Горы!", android.widget.Toast.LENGTH_SHORT).show()
            }
        }

        // Полоски — жёлтые, если мир открыт
        if (ProgressManager.getCompleted(this, "forest") >= 5) {
            line1.setBackgroundColor(0xFFFFC107.toInt())
        }
        if (ProgressManager.getCompleted(this, "mountains") >= 5) {
            line2.setBackgroundColor(0xFFFFC107.toInt())
        }
    }

    private fun openWorld(world: String) {
        val intent = Intent(this, LevelSelectActivity::class.java)
        intent.putExtra("world", world)
        startActivity(intent)
    }
}
