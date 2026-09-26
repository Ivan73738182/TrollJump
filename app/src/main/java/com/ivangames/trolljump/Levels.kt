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
        // Стартовый пол — короткий
        data.platforms.add(Platform(RectF(0f, h * 0.85f, w * 0.35f, h * 0.90f)))
        // Дорожка из быстрых исчезающих платформ
        data.platforms.add(Platform(RectF(w * 0.4f, h * 0.85f, w * 0.55f, h * 0.88f), true))
        data.platforms.add(Platform(RectF(w * 0.6f, h * 0.85f, w * 0.75f, h * 0.88f), true))
        // Шипы между
        data.spikes.add(RectF(w * 0.8f, h * 0.82f, w * 0.9f, h * 0.85f))
        // Ступенька
        data.platforms.add(Platform(RectF(w * 0.95f, h * 0.7f, w * 1.1f, h * 0.73f), true))
        // Шипы второй
        data.spikes.add(RectF(w * 1.2f, h * 0.82f, w * 1.3f, h * 0.85f))
        // Пол
        data.platforms.add(Platform(RectF(w * 1.3f, h * 0.85f, w * 1.7f, h * 0.90f)))
        // Падающий потолок
        data.fallingCeilings.add(FallingCeiling(RectF(w * 1.45f, h * 0.05f, w * 1.6f, h * 0.18f), w * 1.3f))
        // Финальный пол
        data.platforms.add(Platform(RectF(w * 1.7f, h * 0.85f, w * 2.3f, h * 0.90f)))
        data.door = RectF(w * 2.1f, h * 0.75f, w * 2.18f, h * 0.85f)
        return data
    }

    private fun forestLevel2(w: Float, h: Float): LevelData {
        val data = LevelData()
        data.platforms.add(Platform(RectF(0f, h * 0.85f, w * 0.3f, h * 0.90f)))
        // Дорожка из очень быстрых исчезающих платформ
        var x = w * 0.35f
        repeat(8) {
            data.platforms.add(Platform(RectF(x, h * 0.85f, x + w * 0.08f, h * 0.88f), true))
            x += w * 0.11f
        }
        // Финальный пол с шипами
        data.platforms.add(Platform(RectF(w * 1.3f, h * 0.85f, w * 1.8f, h * 0.90f)))
        data.spikes.add(RectF(w * 1.45f, h * 0.82f, w * 1.55f, h * 0.85f))
        // Падающий потолок над шипами
        data.fallingCeilings.add(FallingCeiling(RectF(w * 1.6f, h * 0.05f, w * 1.7f, h * 0.18f), w * 1.5f))
        // Финал
        data.platforms.add(Platform(RectF(w * 1.8f, h * 0.85f, w * 2.3f, h * 0.90f)))
        data.door = RectF(w * 2.1f, h * 0.75f, w * 2.18f, h * 0.85f)
        return data
    }

    private fun forestLevel3(w: Float, h: Float): LevelData {
        val data = LevelData()
        // Много ям с шипами
        data.platforms.add(Platform(RectF(0f, h * 0.85f, w * 0.30f, h * 0.90f)))
        data.spikes.add(RectF(w * 0.30f, h * 0.82f, w * 0.42f, h * 0.85f))
        data.platforms.add(Platform(RectF(w * 0.42f, h * 0.85f, w * 0.70f, h * 0.90f)))
        data.spikes.add(RectF(w * 0.70f, h * 0.82f, w * 0.82f, h * 0.85f))
        // Быстрая платформа
        data.platforms.add(Platform(RectF(w * 0.82f, h * 0.85f, w * 0.95f, h * 0.88f), true))
        data.platforms.add(Platform(RectF(w * 1.00f, h * 0.85f, w * 1.30f, h * 0.90f)))
        data.spikes.add(RectF(w * 1.30f, h * 0.82f, w * 1.42f, h * 0.85f))
        // Ступеньки
        data.platforms.add(Platform(RectF(w * 1.45f, h * 0.70f, w * 1.60f, h * 0.73f), true))
        data.platforms.add(Platform(RectF(w * 1.65f, h * 0.55f, w * 1.80f, h * 0.58f)))
        // Шипы
        data.spikes.add(RectF(w * 1.85f, h * 0.82f, w * 1.95f, h * 0.85f))
        // Финал
        data.platforms.add(Platform(RectF(w * 1.95f, h * 0.85f, w * 2.3f, h * 0.90f)))
        data.door = RectF(w * 2.1f, h * 0.75f, w * 2.18f, h * 0.85f)
        return data
    }

    private fun forestLevel4(w: Float, h: Float): LevelData {
        val data = LevelData()
        data.platforms.add(Platform(RectF(0f, h * 0.85f, w * 0.4f, h * 0.90f)))
        // Лавина потолков
        data.fallingCeilings.add(FallingCeiling(RectF(w * 0.45f, h * 0.05f, w * 0.55f, h * 0.18f), w * 0.35f))
        data.platforms.add(Platform(RectF(w * 0.55f, h * 0.85f, w * 0.75f, h * 0.88f), true))
        data.fallingCeilings.add(FallingCeiling(RectF(w * 0.85f, h * 0.05f, w * 0.95f, h * 0.18f), w * 0.75f))
        data.platforms.add(Platform(RectF(w * 0.95f, h * 0.85f, w * 1.15f, h * 0.88f), true))
        data.fallingCeilings.add(FallingCeiling(RectF(w * 1.25f, h * 0.05f, w * 1.35f, h * 0.18f), w * 1.15f))
        data.platforms.add(Platform(RectF(w * 1.35f, h * 0.85f, w * 1.55f, h * 0.88f), true))
        data.fallingCeilings.add(FallingCeiling(RectF(w * 1.65f, h * 0.05f, w * 1.75f, h * 0.18f), w * 1.55f))
        data.platforms.add(Platform(RectF(w * 1.75f, h * 0.85f, w * 2.3f, h * 0.90f)))
        data.door = RectF(w * 2.1f, h * 0.75f, w * 2.18f, h * 0.85f)
        return data
    }

    private fun forestLevel5(w: Float, h: Float): LevelData {
        val data = LevelData()
        data.platforms.add(Platform(RectF(0f, h * 0.85f, w * 0.3f, h * 0.90f)))
        // Комбо: быстрые платформы + шипы + потолки
        data.platforms.add(Platform(RectF(w * 0.35f, h * 0.85f, w * 0.45f, h * 0.88f), true))
        data.spikes.add(RectF(w * 0.50f, h * 0.82f, w * 0.60f, h * 0.85f))
        data.platforms.add(Platform(RectF(w * 0.60f, h * 0.85f, w * 0.72f, h * 0.88f), true))
        data.fallingCeilings.add(FallingCeiling(RectF(w * 0.80f, h * 0.05f, w * 0.90f, h * 0.18f), w * 0.70f))
        data.platforms.add(Platform(RectF(w * 0.90f, h * 0.85f, w * 1.10f, h * 0.90f)))
        // Ступеньки
        data.platforms.add(Platform(RectF(w * 1.15f, h * 0.65f, w * 1.30f, h * 0.68f), true))
        data.spikes.add(RectF(w * 1.35f, h * 0.82f, w * 1.45f, h * 0.85f))
        data.platforms.add(Platform(RectF(w * 1.45f, h * 0.85f, w * 1.65f, h * 0.90f)))
        data.fallingCeilings.add(FallingCeiling(RectF(w * 1.55f, h * 0.05f, w * 1.65f, h * 0.18f), w * 1.45f))
        data.platforms.add(Platform(RectF(w * 1.70f, h * 0.85f, w * 2.3f, h * 0.90f)))
        data.door = RectF(w * 2.1f, h * 0.75f, w * 2.18f, h * 0.85f)
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

    private fun mountainsLevel1(w: Float, h: Float): LevelData {
        val data = LevelData()
        data.platforms.add(Platform(RectF(0f, h * 0.85f, w * 0.35f, h * 0.90f)))
        // Ледяные ступеньки с трещинами
        data.platforms.add(Platform(RectF(w * 0.4f, h * 0.72f, w * 0.6f, h * 0.75f), true))
        data.platforms.add(Platform(RectF(w * 0.65f, h * 0.60f, w * 0.85f, h * 0.63f), true))
        data.platforms.add(Platform(RectF(w * 0.9f, h * 0.48f, w * 1.1f, h * 0.51f)))
        // Падающая сосулька
        data.fallingCeilings.add(FallingCeiling(RectF(w * 1.15f, h * 0.05f, w * 1.25f, h * 0.18f), w * 1.05f))
        data.platforms.add(Platform(RectF(w * 1.2f, h * 0.60f, w * 1.4f, h * 0.63f), true))
        data.platforms.add(Platform(RectF(w * 1.45f, h * 0.72f, w * 1.65f, h * 0.75f), true))
        data.spikes.add(RectF(w * 1.7f, h * 0.82f, w * 1.8f, h * 0.85f))
        data.platforms.add(Platform(RectF(w * 1.8f, h * 0.85f, w * 2.3f, h * 0.90f)))
        data.door = RectF(w * 2.1f, h * 0.75f, w * 2.18f, h * 0.85f)
        return data
    }

    private fun mountainsLevel2(w: Float, h: Float): LevelData {
        val data = LevelData()
        data.platforms.add(Platform(RectF(0f, h * 0.85f, w * 0.4f, h * 0.90f)))
        // Много падающих сосулек
        data.fallingCeilings.add(FallingCeiling(RectF(w * 0.45f, h * 0.05f, w * 0.55f, h * 0.20f), w * 0.35f))
        data.fallingCeilings.add(FallingCeiling(RectF(w * 0.65f, h * 0.05f, w * 0.75f, h * 0.20f), w * 0.55f))
        data.fallingCeilings.add(FallingCeiling(RectF(w * 0.85f, h * 0.05f, w * 0.95f, h * 0.20f), w * 0.75f))
        data.fallingCeilings.add(FallingCeiling(RectF(w * 1.05f, h * 0.05f, w * 1.15f, h * 0.20f), w * 0.95f))
        data.fallingCeilings.add(FallingCeiling(RectF(w * 1.25f, h * 0.05f, w * 1.35f, h * 0.20f), w * 1.15f))
        // Пол с трещинами
        data.platforms.add(Platform(RectF(w * 0.4f, h * 0.85f, w * 1.6f, h * 0.90f)))
        data.platforms.add(Platform(RectF(w * 1.65f, h * 0.85f, w * 1.85f, h * 0.88f), true))
        data.platforms.add(Platform(RectF(w * 1.9f, h * 0.85f, w * 2.3f, h * 0.90f)))
        data.door = RectF(w * 2.1f, h * 0.75f, w * 2.18f, h * 0.85f)
        return data
    }

    private fun mountainsLevel3(w: Float, h: Float): LevelData {
        val data = LevelData()
        data.platforms.add(Platform(RectF(0f, h * 0.85f, w * 0.3f, h * 0.90f)))
        // Много треснувших платформ
        var x = w * 0.35f
        repeat(5) {
            data.platforms.add(Platform(RectF(x, h * 0.85f, x + w * 0.08f, h * 0.88f), true))
            x += w * 0.10f
        }
        // Платформа с шипами
        data.platforms.add(Platform(RectF(w * 0.85f, h * 0.85f, w * 1.15f, h * 0.90f)))
        data.spikes.add(RectF(w * 0.95f, h * 0.82f, w * 1.05f, h * 0.85f))
        // Падающий потолок над шипами
        data.fallingCeilings.add(FallingCeiling(RectF(w * 1.0f, h * 0.05f, w * 1.1f, h * 0.18f), w * 0.95f))
        // Ещё треснувшие
        x = w * 1.2f
        repeat(4) {
            data.platforms.add(Platform(RectF(x, h * 0.85f, x + w * 0.08f, h * 0.88f), true))
            x += w * 0.10f
        }
        data.platforms.add(Platform(RectF(w * 1.65f, h * 0.85f, w * 2.3f, h * 0.90f)))
        data.door = RectF(w * 2.1f, h * 0.75f, w * 2.18f, h * 0.85f)
        return data
    }

    private fun mountainsLevel4(w: Float, h: Float): LevelData {
        val data = LevelData()
        data.platforms.add(Platform(RectF(0f, h * 0.85f, w * 0.3f, h * 0.90f)))
        // Лавина сосулек
        for (i in 0 until 6) {
            val cx = w * (0.35f + i * 0.16f)
            data.fallingCeilings.add(FallingCeiling(RectF(cx, h * 0.05f, cx + w * 0.08f, h * 0.18f), cx - w * 0.10f))
        }
        data.platforms.add(Platform(RectF(w * 0.4f, h * 0.55f, w * 0.55f, h * 0.58f), true))
        data.platforms.add(Platform(RectF(w * 0.85f, h * 0.55f, w * 1.0f, h * 0.58f), true))
        data.platforms.add(Platform(RectF(w * 1.3f, h * 0.55f, w * 1.45f, h * 0.58f), true))
        data.platforms.add(Platform(RectF(w * 1.7f, h * 0.85f, w * 2.3f, h * 0.90f)))
        data.door = RectF(w * 2.1f, h * 0.75f, w * 2.18f, h * 0.85f)
        return data
    }

    private fun mountainsLevel5(w: Float, h: Float): LevelData {
        val data = LevelData()
        data.platforms.add(Platform(RectF(0f, h * 0.85f, w * 0.3f, h * 0.90f)))
        // Все ловушки вместе
        data.platforms.add(Platform(RectF(w * 0.35f, h * 0.85f, w * 0.45f, h * 0.88f), true))
        data.spikes.add(RectF(w * 0.50f, h * 0.82f, w * 0.60f, h * 0.85f))
        data.fallingCeilings.add(FallingCeiling(RectF(w * 0.65f, h * 0.05f, w * 0.75f, h * 0.18f), w * 0.55f))
        data.platforms.add(Platform(RectF(w * 0.75f, h * 0.85f, w * 0.95f, h * 0.90f)))
        data.platforms.add(Platform(RectF(w * 1.00f, h * 0.65f, w * 1.15f, h * 0.68f), true))
        data.spikes.add(RectF(w * 1.20f, h * 0.82f, w * 1.30f, h * 0.85f))
        data.fallingCeilings.add(FallingCeiling(RectF(w * 1.35f, h * 0.05f, w * 1.45f, h * 0.18f), w * 1.25f))
        data.platforms.add(Platform(RectF(w * 1.45f, h * 0.85f, w * 1.65f, h * 0.88f), true))
        data.platforms.add(Platform(RectF(w * 1.7f, h * 0.85f, w * 2.3f, h * 0.90f)))
        data.door = RectF(w * 2.1f, h * 0.75f, w * 2.18f, h * 0.85f)
        return data
    }

    // ===================== МОРЕ =====================

    private fun buildSeaLevel(level: Int, w: Float, h: Float): LevelData {
        return when (level) {
            1 -> seaLevel1(w, h)
            2 -> seaLevel2(w, h)
            3 -> seaLevel3(w, h)
            4 -> seaLevel4(w, h)
            5 -> seaLevel5(w, h)
            else -> seaLevel1(w, h)
        }
    }

    private fun seaLevel1(w: Float, h: Float): LevelData {
        val data = LevelData()
        // Песчаный пол с ракушками
        data.platforms.add(Platform(RectF(0f, h * 0.85f, w * 0.4f, h * 0.90f)))
        data.platforms.add(Platform(RectF(w * 0.45f, h * 0.85f, w * 0.6f, h * 0.88f), true))
        data.platforms.add(Platform(RectF(w * 0.65f, h * 0.85f, w * 0.8f, h * 0.88f), true))
        // Кораллы-шипы
        data.spikes.add(RectF(w * 0.85f, h * 0.82f, w * 0.95f, h * 0.85f))
        data.platforms.add(Platform(RectF(w * 1.0f, h * 0.72f, w * 1.2f, h * 0.75f), true))
        data.spikes.add(RectF(w * 1.25f, h * 0.82f, w * 1.35f, h * 0.85f))
        data.platforms.add(Platform(RectF(w * 1.4f, h * 0.85f, w * 2.3f, h * 0.90f)))
        data.door = RectF(w * 2.1f, h * 0.75f, w * 2.18f, h * 0.85f)
        return data
    }

    private fun seaLevel2(w: Float, h: Float): LevelData {
        val data = LevelData()
        data.platforms.add(Platform(RectF(0f, h * 0.85f, w * 0.35f, h * 0.90f)))
        // Песчаные исчезающие платформы
        var x = w * 0.4f
        repeat(6) {
            data.platforms.add(Platform(RectF(x, h * 0.85f, x + w * 0.09f, h * 0.88f), true))
            x += w * 0.12f
        }
        // Кораллы и рыба-капкан
        data.spikes.add(RectF(w * 1.15f, h * 0.82f, w * 1.25f, h * 0.85f))
        data.platforms.add(Platform(RectF(w * 1.3f, h * 0.85f, w * 1.7f, h * 0.90f)))
        data.fallingCeilings.add(FallingCeiling(RectF(w * 1.45f, h * 0.05f, w * 1.55f, h * 0.18f), w * 1.35f))
        data.platforms.add(Platform(RectF(w * 1.75f, h * 0.85f, w * 2.3f, h * 0.90f)))
        data.door = RectF(w * 2.1f, h * 0.75f, w * 2.18f, h * 0.85f)
        return data
    }

    private fun seaLevel3(w: Float, h: Float): LevelData {
        val data = LevelData()
        data.platforms.add(Platform(RectF(0f, h * 0.85f, w * 0.3f, h * 0.90f)))
        // Много коралловых шипов
        data.spikes.add(RectF(w * 0.30f, h * 0.82f, w * 0.42f, h * 0.85f))
        data.platforms.add(Platform(RectF(w * 0.42f, h * 0.85f, w * 0.65f, h * 0.88f), true))
        data.spikes.add(RectF(w * 0.70f, h * 0.82f, w * 0.82f, h * 0.85f))
        data.platforms.add(Platform(RectF(w * 0.85f, h * 0.72f, w * 1.05f, h * 0.75f), true))
        data.spikes.add(RectF(w * 1.10f, h * 0.82f, w * 1.20f, h * 0.85f))
        data.platforms.add(Platform(RectF(w * 1.25f, h * 0.85f, w * 1.5f, h * 0.90f)))
        data.spikes.add(RectF(w * 1.55f, h * 0.82f, w * 1.65f, h * 0.85f))
        data.platforms.add(Platform(RectF(w * 1.65f, h * 0.85f, w * 2.3f, h * 0.90f)))
        data.door = RectF(w * 2.1f, h * 0.75f, w * 2.18f, h * 0.85f)
        return data
    }

    private fun seaLevel4(w: Float, h: Float): LevelData {
        val data = LevelData()
        data.platforms.add(Platform(RectF(0f, h * 0.85f, w * 0.35f, h * 0.90f)))
        // Акулы (падающие потолки)
        for (i in 0 until 5) {
            val ax = w * (0.4f + i * 0.2f)
            data.fallingCeilings.add(FallingCeiling(RectF(ax, h * 0.05f, ax + w * 0.1f, h * 0.18f), ax - w * 0.1f))
        }
        // Платформы между
        data.platforms.add(Platform(RectF(w * 0.5f, h * 0.55f, w * 0.65f, h * 0.58f), true))
        data.platforms.add(Platform(RectF(w * 0.9f, h * 0.55f, w * 1.05f, h * 0.58f), true))
        data.platforms.add(Platform(RectF(w * 1.3f, h * 0.55f, w * 1.45f, h * 0.58f), true))
        data.platforms.add(Platform(RectF(w * 1.7f, h * 0.85f, w * 2.3f, h * 0.90f)))
        data.door = RectF(w * 2.1f, h * 0.75f, w * 2.18f, h * 0.85f)
        return data
    }

    private fun seaLevel5(w: Float, h: Float): LevelData {
        val data = LevelData()
        data.platforms.add(Platform(RectF(0f, h * 0.85f, w * 0.3f, h * 0.90f)))
        // Всё вместе: кораллы + акулы + исчезающие платформы
        data.spikes.add(RectF(w * 0.35f, h * 0.82f, w * 0.45f, h * 0.85f))
        data.platforms.add(Platform(RectF(w * 0.45f, h * 0.85f, w * 0.58f, h * 0.88f), true))
        data.fallingCeilings.add(FallingCeiling(RectF(w * 0.65f, h * 0.05f, w * 0.75f, h * 0.18f), w * 0.55f))
        data.platforms.add(Platform(RectF(w * 0.75f, h * 0.85f, w * 0.90f, h * 0.90f)))
        data.spikes.add(RectF(w * 0.95f, h * 0.82f, w * 1.05f, h * 0.85f))
        data.platforms.add(Platform(RectF(w * 1.05f, h * 0.70f, w * 1.20f, h * 0.73f), true))
        data.fallingCeilings.add(FallingCeiling(RectF(w * 1.30f, h * 0.05f, w * 1.40f, h * 0.18f), w * 1.20f))
        data.platforms.add(Platform(RectF(w * 1.40f, h * 0.85f, w * 1.55f, h * 0.88f), true))
        data.spikes.add(RectF(w * 1.60f, h * 0.82f, w * 1.70f, h * 0.85f))
        data.platforms.add(Platform(RectF(w * 1.70f, h * 0.85f, w * 2.3f, h * 0.90f)))
        data.door = RectF(w * 2.1f, h * 0.75f, w * 2.18f, h * 0.85f)
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
