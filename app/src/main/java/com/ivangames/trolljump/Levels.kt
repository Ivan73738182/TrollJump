package com.ivangames.trolljump

import android.graphics.RectF

object Levels {

    fun buildLevel(world: String, level: Int, w: Float, h: Float): LevelData {
        return when (world) {
            "mountains" -> buildMountainsLevel(level, w, h)
            "sea" -> buildSeaLevel(level, w, h)
            else -> buildForestLevel(level, w, h)
        }
    }

    // ===================== ЛЕС =====================

    private fun buildForestLevel(level: Int, w: Float, h: Float): LevelData {
        return when (level) {
            1 -> forestLevel1(w, h)
            2 -> forestLevel2(w, h)
            3 -> forestLevel3(w, h)
            4 -> forestLevel4(w, h)
            5 -> forestLevel5(w, h)
            else -> forestLevel1(w, h)
        }
    }

    private fun forestLevel1(w: Float, h: Float): LevelData {
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

    private fun forestLevel2(w: Float, h: Float): LevelData {
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

    private fun forestLevel3(w: Float, h: Float): LevelData {
        val data = LevelData()
        data.platforms.add(Platform(RectF(0f, h * 0.85f, w * 0.45f, h * 0.90f)))
        data.spikes.add(RectF(w * 0.45f, h * 0.82f, w * 0.55f, h * 0.85f))
        data.platforms.add(Platform(RectF(w * 0.55f, h * 0.85f, w * 1.00f, h * 0.90f)))
        data.spikes.add(RectF(w * 1.00f, h * 0.82f, w * 1.10f, h * 0.85f))
        data.platforms.add(Platform(RectF(w * 1.10f, h * 0.85f, w * 1.55f, h * 0.90f)))
        data.spikes.add(RectF(w * 1.55f, h * 0.82f, w * 1.65f, h * 0.85f))
        data.platforms.add(Platform(RectF(w * 1.65f, h * 0.85f, w * 2.1f, h * 0.90f)))
        data.door = RectF(w * 1.90f, h * 0.75f, w * 1.98f, h * 0.85f)
        return data
    }

    private fun forestLevel4(w: Float, h: Float): LevelData {
        val data = LevelData()
        data.platforms.add(Platform(RectF(0f, h * 0.85f, w * 2.5f, h * 0.90f)))
        data.platforms.add(Platform(RectF(w * 0.7f, h * 0.65f, w * 0.9f, h * 0.68f)))
        data.platforms.add(Platform(RectF(w * 1.5f, h * 0.65f, w * 1.7f, h * 0.68f)))
        data.fallingCeilings.add(FallingCeiling(RectF(w * 0.5f, h * 0.05f, w * 0.7f, h * 0.15f), w * 0.35f))
        data.fallingCeilings.add(FallingCeiling(RectF(w * 1.0f, h * 0.05f, w * 1.2f, h * 0.15f), w * 0.85f))
        data.fallingCeilings.add(FallingCeiling(RectF(w * 1.6f, h * 0.05f, w * 1.8f, h * 0.15f), w * 1.45f))
        data.door = RectF(w * 2.20f, h * 0.75f, w * 2.28f, h * 0.85f)
        return data
    }

    private fun forestLevel5(w: Float, h: Float): LevelData {
        val data = LevelData()
        data.platforms.add(Platform(RectF(0f, h * 0.85f, w * 0.6f, h * 0.90f)))
        var x = w * 0.65f
        repeat(4) {
            data.platforms.add(Platform(RectF(x, h * 0.85f, x + w * 0.10f, h * 0.88f), true))
            x += w * 0.13f
        }
        data.platforms.add(Platform(RectF(w * 1.20f, h * 0.85f, w * 1.6f, h * 0.90f)))
        data.spikes.add(RectF(w * 1.35f, h * 0.82f, w * 1.45f, h * 0.85f))
        data.fallingCeilings.add(FallingCeiling(RectF(w * 1.7f, h * 0.05f, w * 1.9f, h * 0.15f), w * 1.55f))
        data.platforms.add(Platform(RectF(w * 1.6f, h * 0.85f, w * 2.1f, h * 0.90f)))
        data.platforms.add(Platform(RectF(w * 1.75f, h * 0.55f, w * 1.95f, h * 0.58f)))
        data.door = RectF(w * 2.20f, h * 0.75f, w * 2.28f, h * 0.85f)
        return data
    }

    // ===================== ГОРЫ =====================

    private fun buildMountainsLevel(level: Int, w: Float, h: Float): LevelData {
        return when (level) {
            1 -> mountainsLevel1(w, h)
            2 -> mountainsLevel2(w, h)
            3 -> mountainsLevel3(w, h)
            4 -> mountainsLevel4(w, h)
            5 -> mountainsLevel5(w, h)
            else -> mountainsLevel1(w, h)
        }
    }

    // Уровень 1: ледяные ступеньки
    private fun mountainsLevel1(w: Float, h: Float): LevelData {
        val data = LevelData()
        data.platforms.add(Platform(RectF(0f, h * 0.85f, w * 0.5f, h * 0.90f)))
        // Ступеньки всё выше
        data.platforms.add(Platform(RectF(w * 0.55f, h * 0.72f, w * 0.80f, h * 0.75f)))
        data.platforms.add(Platform(RectF(w * 0.85f, h * 0.60f, w * 1.10f, h * 0.63f)))
        data.platforms.add(Platform(RectF(w * 1.15f, h * 0.48f, w * 1.45f, h * 0.51f)))
        data.platforms.add(Platform(RectF(w * 1.50f, h * 0.60f, w * 1.75f, h * 0.63f)))
        data.platforms.add(Platform(RectF(w * 1.80f, h * 0.72f, w * 2.10f, h * 0.75f)))
        data.platforms.add(Platform(RectF(w * 2.15f, h * 0.85f, w * 2.80f, h * 0.90f)))
        data.door = RectF(w * 2.60f, h * 0.75f, w * 2.68f, h * 0.85f)
        return data
    }

    // Уровень 2: сосульки падают
    private fun mountainsLevel2(w: Float, h: Float): LevelData {
        val data = LevelData()
        data.platforms.add(Platform(RectF(0f, h * 0.85f, w * 2.5f, h * 0.90f)))
        // Падающие сосульки (потолки)
        data.fallingCeilings.add(FallingCeiling(RectF(w * 0.4f, h * 0.05f, w * 0.5f, h * 0.20f), w * 0.25f))
        data.fallingCeilings.add(FallingCeiling(RectF(w * 0.8f, h * 0.05f, w * 0.9f, h * 0.20f), w * 0.65f))
        data.fallingCeilings.add(FallingCeiling(RectF(w * 1.2f, h * 0.05f, w * 1.3f, h * 0.20f), w * 1.05f))
        data.fallingCeilings.add(FallingCeiling(RectF(w * 1.6f, h * 0.05f, w * 1.7f, h * 0.20f), w * 1.45f))
        // Пара ступенек
        data.platforms.add(Platform(RectF(w * 0.6f, h * 0.55f, w * 0.8f, h * 0.58f)))
        data.platforms.add(Platform(RectF(w * 1.4f, h * 0.55f, w * 1.6f, h * 0.58f)))
        data.door = RectF(w * 2.20f, h * 0.75f, w * 2.28f, h * 0.85f)
        return data
    }

    // Уровень 3: скользкий пол и трещины
    private fun mountainsLevel3(w: Float, h: Float): LevelData {
        val data = LevelData()
        data.platforms.add(Platform(RectF(0f, h * 0.85f, w * 0.5f, h * 0.90f)))
        // Треснувшие платформы (исчезающие)
        data.platforms.add(Platform(RectF(w * 0.5f, h * 0.85f, w * 0.7f, h * 0.88f), true))
        data.platforms.add(Platform(RectF(w * 0.75f, h * 0.85f, w * 0.95f, h * 0.88f), true))
        data.platforms.add(Platform(RectF(w * 1.00f, h * 0.85f, w * 1.2f, h * 0.88f), true))
        // Платформа с шипами
        data.platforms.add(Platform(RectF(w * 1.25f, h * 0.85f, w * 1.6f, h * 0.90f)))
        data.spikes.add(RectF(w * 1.35f, h * 0.82f, w * 1.45f, h * 0.85f))
        // Ещё трещины
        data.platforms.add(Platform(RectF(w * 1.65f, h * 0.85f, w * 1.85f, h * 0.88f), true))
        // Финальный пол
        data.platforms.add(Platform(RectF(w * 1.9f, h * 0.85f, w * 2.5f, h * 0.90f)))
        data.door = RectF(w * 2.20f, h * 0.75f, w * 2.28f, h * 0.85f)
        return data
    }

    // Уровень 4: лавина (много падающих потолков)
    private fun mountainsLevel4(w: Float, h: Float): LevelData {
        val data = LevelData()
        data.platforms.add(Platform(RectF(0f, h * 0.85f, w * 2.6f, h * 0.90f)))
        // Лавина
        data.fallingCeilings.add(FallingCeiling(RectF(w * 0.3f, h * 0.05f, w * 0.4f, h * 0.18f), w * 0.20f))
        data.fallingCeilings.add(FallingCeiling(RectF(w * 0.6f, h * 0.05f, w * 0.7f, h * 0.18f), w * 0.50f))
        data.fallingCeilings.add(FallingCeiling(RectF(w * 0.9f, h * 0.05f, w * 1.0f, h * 0.18f), w * 0.80f))
        data.fallingCeilings.add(FallingCeiling(RectF(w * 1.2f, h * 0.05f, w * 1.3f, h * 0.18f), w * 1.10f))
        data.fallingCeilings.add(FallingCeiling(RectF(w * 1.5f, h * 0.05f, w * 1.6f, h * 0.18f), w * 1.40f))
        // Ступеньки для укрытия
        data.platforms.add(Platform(RectF(w * 0.5f, h * 0.55f, w * 0.65f, h * 0.58f)))
        data.platforms.add(Platform(RectF(w * 1.1f, h * 0.55f, w * 1.25f, h * 0.58f)))
        data.platforms.add(Platform(RectF(w * 1.7f, h * 0.55f, w * 1.85f, h * 0.58f)))
        data.door = RectF(w * 2.30f, h * 0.75f, w * 2.38f, h * 0.85f)
        return data
    }

    // Уровень 5: всё вместе
    private fun mountainsLevel5(w: Float, h: Float): LevelData {
        val data = LevelData()
        data.platforms.add(Platform(RectF(0f, h * 0.85f, w * 0.5f, h * 0.90f)))
        // Трещины
        data.platforms.add(Platform(RectF(w * 0.55f, h * 0.85f, w * 0.75f, h * 0.88f), true))
        data.platforms.add(Platform(RectF(w * 0.80f, h * 0.85f, w * 1.00f, h * 0.88f), true))
        // Шипы
        data.spikes.add(RectF(w * 1.05f, h * 0.82f, w * 1.15f, h * 0.85f))
        data.platforms.add(Platform(RectF(w * 1.15f, h * 0.85f, w * 1.55f, h * 0.90f)))
        // Ступеньки
        data.platforms.add(Platform(RectF(w * 1.60f, h * 0.65f, w * 1.80f, h * 0.68f)))
        data.platforms.add(Platform(RectF(w * 1.85f, h * 0.50f, w * 2.05f, h * 0.53f)))
        // Лавина
        data.fallingCeilings.add(FallingCeiling(RectF(w * 2.10f, h * 0.05f, w * 2.20f, h * 0.18f), w * 2.00f))
        // Финальный пол
        data.platforms.add(Platform(RectF(w * 1.60f, h * 0.85f, w * 2.5f, h * 0.90f)))
        data.door = RectF(w * 2.30f, h * 0.75f, w * 2.38f, h * 0.85f)
        return data
    }

    // ===================== МОРЕ =====================

    private fun buildSeaLevel(level: Int, w: Float, h: Float): LevelData {
        // Пока используем уровни Леса как заглушку
        return buildForestLevel(level, w, h)
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
