package com.ivangames.trolljump

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LevelSelectActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level_select)

        val world = intent.getStringExtra("world") ?: "forest"

        val titleView = findViewById<TextView>(R.id.worldTitle)
        titleView.text = when (world) {
            "forest" -> "🌳 Лес"
            "mountains" -> "🏔 Горы"
            "sea" -> "🌊 Море"
            else -> "🌳 Лес"
        }

        val buttons = listOf(
            findViewById<Button>(R.id.level1),
            findViewById<Button>(R.id.level2),
            findViewById<Button>(R.id.level3),
            findViewById<Button>(R.id.level4),
            findViewById<Button>(R.id.level5)
        )

        buttons.forEachIndexed { index, button ->
            val levelNumber = index + 1
            val unlocked = ProgressManager.isLevelUnlocked(this, world, levelNumber)

            if (unlocked) {
                button.text = "УРОВЕНЬ $levelNumber"
                button.backgroundTintList = ColorStateList.valueOf(0xFF2E7D32.toInt())
                button.isEnabled = true
                button.setOnClickListener {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("world", world)
                    intent.putExtra("level", levelNumber)
                    startActivity(intent)
                }
            } else {
                button.text = "🔒 УРОВЕНЬ $levelNumber"
                button.backgroundTintList = ColorStateList.valueOf(0xFF333333.toInt())
                button.isEnabled = false
            }
        }

        // Проверка: если все 5 пройдены — показать поздравление
        if (ProgressManager.getCompleted(this, world) >= 5) {
            val nextWorld = when (world) {
                "forest" -> "Горы"
                "mountains" -> "Море"
                else -> null
            }
            if (nextWorld != null) {
                Toast.makeText(this, "🏆 Ты прошёл мир! Открыты $nextWorld!", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "🏆 Ты прошёл все миры! Поздравляю!", Toast.LENGTH_LONG).show()
            }
        }

        findViewById<Button>(R.id.backBtn).setOnClickListener {
            finish()
        }
    }
}
