package com.ivangames.trolljump

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class GameView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    // Игрок
    private var playerX = 200f
    private var playerY = 400f
    private var playerSize = 80f
    private var velocityY = 0f
    private var velocityX = 0f
    private var spawnX = 200f
    private var spawnY = 400f

    // Физика
    private val gravity = 1.5f
    private val jumpPower = -25f
    private val moveSpeed = 10f
    private var onGround = false

    // Камера
    private var cameraX = 0f

    // Уровень
    private val platforms = mutableListOf<Platform>()
    private val spikes = mutableListOf<RectF>()
    private val fallingCeilings = mutableListOf<FallingCeiling>()
    private var doorRect = RectF()
    private var initialized = false

    // Управление
    var moveLeft = false
    var moveRight = false
    var jump = false

    // Краски
    private val playerPaint = Paint().apply {
        color = Color.parseColor("#FFC107")
        style = Paint.Style.FILL
    }
    private val platformPaint = Paint().apply {
        color = Color.parseColor("#8B7355")
        style = Paint.Style.FILL
    }
    private val disappearingPaint = Paint().apply {
        color = Color.parseColor("#A0522D")
        style = Paint.Style.FILL
    }
    private val gonePaint = Paint().apply {
        color = Color.parseColor("#333333")
        style = Paint.Style.FILL
    }
    private val doorPaint = Paint().apply {
        color = Color.parseColor("#4FC3F7")
        style = Paint.Style.FILL
    }
    private val spikePaint = Paint().apply {
        color = Color.parseColor("#E53935")
        style = Paint.Style.FILL
    }
    private val ceilingPaint = Paint().apply {
        color = Color.parseColor("#666666")
        style = Paint.Style.FILL
    }
    private val ceilingActivePaint = Paint().apply {
        color = Color.parseColor("#B71C1C")
        style = Paint.Style.FILL
    }

    // Классы-объекты
    class Platform(val rect: RectF, val disappearing: Boolean = false) {
        var timer = 0
        var gone = false
    }

    class FallingCeiling(val rect: RectF, val triggerX: Float) {
        var triggered = false
        var velocityY = 0f
        var fallen = false
    }

    private fun setupLevel() {
        val w = width.toFloat()
        val h = height.toFloat()

        platforms.clear()
        spikes.clear()
        fallingCeilings.clear()

        // Пол — 3 экрана в ширину
        platforms.add(Platform(RectF(0f, h * 0.85f, w * 3f, h * 0.90f)))

        // Ступенька
        platforms.add(Platform(RectF(w * 0.40f, h * 0.65f, w * 0.60f, h * 0.68f)))

        // Исчезающая платформа
        platforms.add(Platform(RectF(w * 0.85f, h * 0.75f, w * 1.10f, h * 0.78f), true))

        // Верхняя платформа
        platforms.add(Platform(RectF(w * 1.30f, h * 0.55f, w * 1.60f, h * 0.58f)))

        // Шипы
        spikes.add(RectF(w * 0.70f, h * 0.82f, w * 0.78f, h * 0.85f))
        spikes.add(RectF(w * 1.90f, h * 0.82f, w * 2.00f, h * 0.85f))

        // Падающий потолок (триггерится, когда игрок подходит)
        fallingCeilings.add(FallingCeiling(RectF(w * 1.15f, h * 0.05f, w * 1.35f, h * 0.15f), w * 1.00f))

        // Дверь в конце уровня
        doorRect = RectF(w * 2.70f, h * 0.75f, w * 2.78f, h * 0.85f)

        // Игрок — на полу, слева
        playerX = w * 0.10f
        playerY = h * 0.85f - playerSize
        spawnX = playerX
        spawnY = playerY

        initialized = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (!initialized && width > 0 && height > 0) {
            setupLevel()
        }

        canvas.drawColor(Color.parseColor("#1A1A1A"))

        // Камера
        val w = width.toFloat()
        val targetCamX = (playerX - w * 0.35f).coerceAtLeast(0f)
        cameraX = cameraX + (targetCamX - cameraX) * 0.1f

        canvas.save()
        canvas.translate(-cameraX, 0f)

        // Платформы
        for (p in platforms) {
            if (p.disappearing && p.gone) {
                canvas.drawRect(p.rect, gonePaint)
            } else if (p.disappearing && p.timer > 0) {
                canvas.drawRect(p.rect, disappearingPaint)
            } else {
                canvas.drawRect(p.rect, platformPaint)
            }
        }

        // Шипы
        for (s in spikes) {
            canvas.drawRect(s, spikePaint)
        }

        // Падающий потолок
        for (c in fallingCeilings) {
            canvas.drawRect(c.rect, if (c.triggered) ceilingActivePaint else ceilingPaint)
        }

        // Дверь
        canvas.drawRect(doorRect, doorPaint)

        // Игрок
        canvas.drawRect(
            playerX, playerY,
            playerX + playerSize, playerY + playerSize,
            playerPaint
        )

        canvas.restore()

        update()
        invalidate()
    }

    private fun update() {
        // Горизонтальное движение
        if (moveLeft) velocityX = -moveSpeed
        else if (moveRight) velocityX = moveSpeed
        else velocityX = 0f

        playerX += velocityX

        // Гравитация
        velocityY += gravity
        playerY += velocityY

        // Приземление
        onGround = false
        for (p in platforms) {
            if (p.gone) continue
            val r = p.rect
            if (playerX + playerSize > r.left && playerX < r.right) {
                if (playerY + playerSize in r.top..r.top + 30f && velocityY >= 0) {
                    playerY = r.top - playerSize
                    velocityY = 0f
                    onGround = true
                    // Активируем исчезающий пол
                    if (p.disappearing && p.timer == 0) {
                        p.timer = 60  // ~1 секунда при 60fps
                    }
                }
            }
        }

        // Таймеры исчезающих платформ
        for (p in platforms) {
            if (p.disappearing && p.timer > 0 && !p.gone) {
                p.timer--
                if (p.timer == 0) p.gone = true
            }
        }

        // Триггер падающего потолка
        for (c in fallingCeilings) {
            if (!c.triggered && playerX > c.triggerX) {
                c.triggered = true
            }
            if (c.triggered && !c.fallen) {
                c.velocityY += 2f
                c.rect.top += c.velocityY
                c.rect.bottom += c.velocityY
                // Если достиг пола — остановить
                if (c.rect.bottom >= height * 0.85f) {
                    c.fallen = true
                }
                // Проверка столкновения с игроком
                if (RectF.intersects(c.rect, RectF(playerX, playerY, playerX + playerSize, playerY + playerSize))) {
                    respawn()
                }
            }
        }

        // Проверка шипов
        for (s in spikes) {
            if (RectF.intersects(s, RectF(playerX, playerY, playerX + playerSize, playerY + playerSize))) {
                respawn()
            }
        }

        // Прыжок
        if (jump && onGround) {
            velocityY = jumpPower
            onGround = false
        }

        // Границы
        if (playerX < 0) playerX = 0f

        // Падение вниз — респавн
        if (playerY > height + 300) {
            respawn()
        }

        // Проверка двери — победа
        if (RectF.intersects(doorRect, RectF(playerX, playerY, playerX + playerSize, playerY + playerSize))) {
            setupLevel()  // пока перезапуск уровня
        }
    }

    private fun respawn() {
        playerX = spawnX
        playerY = spawnY
        velocityX = 0f
        velocityY = 0f
        cameraX = 0f
        // Сброс ловушек
        for (p in platforms) {
            p.timer = 0
            p.gone = false
        }
        for (c in fallingCeilings) {
            c.triggered = false
            c.fallen = false
            c.velocityY = 0f
            c.rect.top = height * 0.05f
            c.rect.bottom = height * 0.15f
        }
    }
}
