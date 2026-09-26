package com.ivangames.trolljump

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val forestBtn = findViewById<LinearLayout>(R.id.forestBtn)

        forestBtn.setOnClickListener {
            val intent = Intent(this, LevelSelectActivity::class.java)
            intent.putExtra("world", "forest")
            startActivity(intent)
        }
    }
}
