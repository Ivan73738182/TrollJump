package com.ivangames.trolljump

import android.content.Context

object ProgressManager {

    private const val PREFS = "trolljump_progress"

    fun isLevelUnlocked(context: Context, world: String, level: Int): Boolean {
        if (level == 1) return true
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val completed = prefs.getInt("${world}_completed", 0)
        return level <= completed + 1
    }

    fun markCompleted(context: Context, world: String, level: Int) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val current = prefs.getInt("${world}_completed", 0)
        if (level > current) {
            prefs.edit().putInt("${world}_completed", level).apply()
        }
    }

    fun getCompleted(context: Context, world: String): Int {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        return prefs.getInt("${world}_completed", 0)
    }
}
