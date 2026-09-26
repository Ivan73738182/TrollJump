package com.ivangames.trolljump

import android.graphics.RectF

object Levels {

    fun buildLevel(level: Int, w: Float, h: Float): LevelData {
        return when (level) {
            1 -> buildLevel1(w, h)
            2 -> buildLevel2(w, h)
            3 -> buildLevel3(w, h)
            4 -> buildLevel4(w, h)
            5 -> buildLevel5(w, h)
            else -> buildLevel1(w, h)
        }
    }

    // Уровень 1 — лёгкий
    private fun buildLevel1(w: Float, h: Float): LevelData {
        val data = LevelData()
        data.platforms.add(Platform(RectF(0f, h * 0.85f, w * 3f, h * 0.90f)))
        data.platforms.add(Platform(RectF(w * 0.40f, h * 0.65f, w * 0.60f, h * 0.68f)))
        data.platforms.add(Platform(RectF(w * 0.85f, h * 0.75f, w * 1.10f, h * 0.78f), true))
        data.platforms.add(Platform(RectF(w * 1.30f, h * 0.55f, w * 1.60f, h * 0.58f)))
        data.spikes.add(RectF(w * 0.70f, h * 0.82f, w * 0.78f, h * 0.85f))
        data.spikes.add(RectF(w * 1.90f, h * 0.82f, w * 2.00f, h * 0.85f))
        data.fallingCeilings.add(FallingCeiling(RectF(w * 1.15f, h * 0.05f, w * 1.35f, h * 0.15f), w * 1.00f))
        data.door = RectF(w * 2.70f, h * 0.75f, w * 2.78f, h * 0.85f)
        return data
    }

    // Уровень 2 — исчезающий пол
    private fun buildLevel2(w: Float, h: Float): LevelData {
        val data = LevelData()
        data.platforms.add(Platform(RectF(0f, h * 0.85f, w * 0.5f, h * 0.90f)))
        var x = w * 0.55f
        repeat(6) {
            data.platforms.add(Platform(RectF(x, h * 0.85f, x + w * 0.12f, h * 0.88f), true))
            x += w * 0.16f
        }
        data.platforms.add(Platform(RectF(w * 1.55f, h * 0.85f, w * 2.1f, h * 0.90f)))
        data.door = RectF(w * 1.90f, h * 0.75f, w * 1.98f, h * 0.85f)
        return data
    }

    // Уровень 3 — шипы и ямы (исправлено: ямы меньше)
    private fun buildLevel3(w: Float, h: Float): LevelData {
        val data = LevelData()
        // Пол 1
        data.platforms.add(Platform(RectF(0f, h * 0.85f, w * 0.45f, h * 0.90f)))
        // Яма 1 (маленькая)
        data.spikes.add(RectF(w * 0.45f, h * 0.82f, w * 0.55f, h * 0.85f))
        // Пол 2
        data.platforms.add(Platform(RectF(w * 0.55f, h * 0.85f, w * 1.00f, h * 0.90f)))
        // Яма 2 (маленькая)
        data.spikes.add(RectF(w * 1.00f, h * 0.82f, w * 1.10f, h * 0.85f))
        // Пол 3
        data.platforms.add(Platform(RectF(w * 1.10f, h * 0.85f, w * 1.55f, h * 0.90f)))
        // Яма 3 (маленькая)
        data.spikes.add(RectF(w * 1.55f, h * 0.82f, w * 1.65f, h * 0.85f))
        // Финальный пол
        data.platforms.add(Platform(RectF(w * 1.65f, h * 0.85f, w * 2.1f, h * 0.90f)))
        // Дверь
        data.door = RectF(w * 1.90f, h * 0.75f, w * 1.98f, h * 0.85f)
        return data
    }

    // Уровень 4 — падающий потолок
    private fun buildLevel4(w: Float, h: Float): LevelData {
        val data = LevelData()
        // Длинный пол
        data.platforms.add(Platform(RectF(0f, h * 0.85f, w * 2.5f, h * 0.90f)))
        // Пара ступенек
        data.platforms.add(Platform(RectF(w * 0.7f, h * 0.65f, w * 0.9f, h * 0.68f)))
        data.platforms.add(Platform(RectF(w * 1.5f, h * 0.65f, w * 1.7f, h * 0.68f)))
        // Падающие потолки — 3 штуки
        data.fallingCeilings.add(FallingCeiling(RectF(w * 0.5f, h * 0.05f, w * 0.7f, h * 0.15f), w * 0.35f))
        data.fallingCeilings.add(FallingCeiling(RectF(w * 1.0f, h * 0.05f, w * 1.2f, h * 0.15f), w * 0.85f))
        data.fallingCeilings.add(FallingCeiling(RectF(w * 1.6f, h * 0.05f, w * 1.8f, h * 0.15f), w * 1.45f))
        // Дверь
        data.door = RectF(w * 2.20f, h * 0.75f, w * 2.28f, h * 0.85f)
        return data
    }

    // Уровень 5 — финальный, всё вместе
    private fun buildLevel5(w: Float, h: Float): LevelData {
        val data = LevelData()
        // Пол 1
        data.platforms.add(Platform(RectF(0f, h * 0.85f, w * 0.6f, h * 0.90f)))
        // Исчезающая дорожка
        var x = w * 0.65f
        repeat(4) {
            data.platforms.add(Platform(RectF(x, h * 0.85f, x + w * 0.10f, h * 0.88f), true))
            x += w * 0.13f
        }
        // Пол 2 с шипами
        data.platforms.add(Platform(RectF(w * 1.20f, h * 0.85f, w * 1.6f, h * 0.90f)))
        data.spikes.add(RectF(w * 1.35f, h * 0.82f, w * 1.45f, h * 0.85f))
        // Падающий потолок
        data.fallingCeilings.add(FallingCeiling(RectF(w * 1.7f, h * 0.05f, w * 1.9f, h * 0.15f), w * 1.55f))
        // Пол 3
        data.platforms.add(Platform(RectF(w * 1.6f, h * 0.85f, w * 2.1f, h * 0.90f)))
        // Верхняя платформа
        data.platforms.add(Platform(RectF(w * 1.75f, h * 0.55f, w * 1.95f, h * 0.58f)))
        // Дверь
        data.door = RectF(w * 2.20f, h * 0.75f, w * 2.28f, h * 0.85f)
        return data
    }
}

// Классы данных
class LevelData {
    val platforms = mutableListOf<Platform>()
    val spikes = mutableListOf<RectF>()
    val fallingCeilings = mutableListOf<FallingCeiling>()
    var door = RectF()
}

class Platform(val rect: RectF, val disappearing: Boolean = false) {
    var timer = 0
    var gone = false
}

class FallingCeiling(val rect: RectF, val triggerX: Float) {
    var triggered = false
    var velocityY = 0f
    var fallen = false
}
