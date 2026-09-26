package com.ivangames.trolljump

import android.content.Context

object ProgressManager {

    private const val PREFS = "trolljump_progress"

    fun isLevelUnlocked(context: Context, world: String, level: Int): Boolean {
        if (level == 1) return isWorldUnlocked(context, world)
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val completed = prefs.getInt("${world}_completed", 0)
        return level <= completed + 1
    }

    fun markCompleted(context: Context, world: String, level: Int, deaths: Int, timeSeconds: Int) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

        // Прогресс
        val current = prefs.getInt("${world}_completed", 0)
        if (level > current) {
            prefs.edit().putInt("${world}_completed", level).apply()
        }

        // Медалька (лучшая)
        val oldMedal = prefs.getInt("${world}_${level}_medal", -1)
        val newMedal = when {
            deaths == 0 -> 3   // 🥇 золото
            deaths <= 3 -> 2   // 🥈 серебро
            else -> 1          // 🥉 бронза
        }
        if (newMedal > oldMedal) {
            prefs.edit().putInt("${world}_${level}_medal", newMedal).apply()
        }

        // Лучшее время
        val oldTime = prefs.getInt("${world}_${level}_time", 999999)
        if (timeSeconds < oldTime) {
            prefs.edit().putInt("${world}_${level}_time", timeSeconds).apply()
        }
    }

    fun getMedal(context: Context, world: String, level: Int): Int {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        return prefs.getInt("${world}_${level}_medal", -1)
    }

    fun getBestTime(context: Context, world: String, level: Int): Int {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        return prefs.getInt("${world}_${level}_time", -1)
    }

    fun getCompleted(context: Context, world: String): Int {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        return prefs.getInt("${world}_completed", 0)
    }

    fun isWorldUnlocked(context: Context, world: String): Boolean {
        if (world == "forest") return true
        return when (world) {
            "mountains" -> getCompleted(context, "forest") >= 5
            "sea" -> getCompleted(context, "mountains") >= 5
            else -> false
        }
    }

    fun resetAll(context: Context) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
    }
}
