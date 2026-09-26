package com.ivangames.trolljump

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class LevelSelectActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level_select)

        val world = intent.getStringExtra("world") ?: "forest"

        val buttons = listOf(
            findViewById<Button>(R.id.level1),
            findViewById<Button>(R.id.level2),
            findViewById<Button>(R.id.level3),
            findViewById<Button>(R.id.level4),
            findViewById<Button>(R.id.level5)
        )

        buttons.forEachIndexed { index, button ->
            val levelNumber = index + 1
            button.text = "Уровень $levelNumber"
            button.setOnClickListener {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("world", world)
                intent.putExtra("level", levelNumber)
                startActivity(intent)
            }
        }

        findViewById<Button>(R.id.backBtn).setOnClickListener {
            finish()
        }
    }
}
