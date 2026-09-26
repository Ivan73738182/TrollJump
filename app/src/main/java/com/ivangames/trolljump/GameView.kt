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

    // Смерть
    private var deaths = 0
    private var deathFlashTimer = 0
    private var isDead = false

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
    var onLevelComplete: (() -> Unit)? = null

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
    private val deathOverlayPaint = Paint().apply {
        color = Color.parseColor("#CCFF0000")
        style = Paint.Style.FILL
    }
    private val hudPaint = Paint().apply {
        color = Color.WHITE
        textSize = 50f
        isAntiAlias = true
        isFakeBoldText = true
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

    private fun setupLevel() {
        val w = width.toFloat()
        val h = height.toFloat()

        platforms.clear()
        spikes.clear()
        fallingCeilings.clear()

        platforms.add(Platform(RectF(0f, h * 0.85f, w * 3f, h * 0.90f)))
        platforms.add(Platform(RectF(w * 0.40f, h * 0.65f, w * 0.60f, h * 0.68f)))
        platforms.add(Platform(RectF(w * 0.85f, h * 0.75f, w * 1.10f, h * 0.78f), true))
        platforms.add(Platform(RectF(w * 1.30f, h * 0.55f, w * 1.60f, h * 0.58f)))

        spikes.add(RectF(w * 0.70f, h * 0.82f, w * 0.78f, h * 0.85f))
        spikes.add(RectF(w * 1.90f, h * 0.82f, w * 2.00f, h * 0.85f))

        fallingCeilings.add(FallingCeiling(RectF(w * 1.15f, h * 0.05f, w * 1.35f, h * 0.15f), w * 1.00f))

        doorRect = RectF(w * 2.70f, h * 0.75f, w * 2.78f, h * 0.85f)

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

        val w = width.toFloat()
        val targetCamX = (playerX - w * 0.35f).coerceAtLeast(0f)
        cameraX = cameraX + (targetCamX - cameraX) * 0.1f

        canvas.save()
        canvas.translate(-cameraX, 0f)

        for (p in platforms) {
            if (p.disappearing && p.gone) {
                canvas.drawRect(p.rect, gonePaint)
            } else if (p.disappearing && p.timer > 0) {
                canvas.drawRect(p.rect, disappearingPaint)
            } else {
                canvas.drawRect(p.rect, platformPaint)
            }
        }

        for (s in spikes) {
            canvas.drawRect(s, spikePaint)
        }

        for (c in fallingCeilings) {
            canvas.drawRect(c.rect, if (c.triggered) ceilingActivePaint else ceilingPaint)
        }

        canvas.drawRect(doorRect, doorPaint)

        canvas.drawRect(
            playerX, playerY,
            playerX + playerSize, playerY + playerSize,
            playerPaint
        )

        canvas.restore()

        // HUD — счётчик смертей (поверх всего, не в камере)
        hudPaint.textSize = height * 0.05f
        canvas.drawText("💀 $deaths", 40f, height * 0.10f, hudPaint)

        // Красная вспышка при смерти
        if (deathFlashTimer > 0) {
            val alpha = (deathFlashTimer / 30f * 200).toInt().coerceIn(0, 200)
            deathOverlayPaint.alpha = alpha
            canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), deathOverlayPaint)
        }

        update()
        invalidate()
    }

    private fun update() {
        // Красная вспышка
        if (deathFlashTimer > 0) {
            deathFlashTimer--
            if (deathFlashTimer == 0) {
                performRespawn()
            }
            return
        }

        if (isDead) return

        if (moveLeft) velocityX = -moveSpeed
        else if (moveRight) velocityX = moveSpeed
        else velocityX = 0f

        playerX += velocityX

        velocityY += gravity
        playerY += velocityY

        onGround = false
        for (p in platforms) {
            if (p.gone) continue
            val r = p.rect
            if (playerX + playerSize > r.left && playerX < r.right) {
                if (playerY + playerSize in r.top..r.top + 30f && velocityY >= 0) {
                    playerY = r.top - playerSize
                    velocityY = 0f
                    onGround = true
                    if (p.disappearing && p.timer == 0) {
                        p.timer = 60
                    }
                }
            }
        }

        for (p in platforms) {
            if (p.disappearing && p.timer > 0 && !p.gone) {
                p.timer--
                if (p.timer == 0) p.gone = true
            }
        }

        for (c in fallingCeilings) {
            if (!c.triggered && playerX > c.triggerX) {
                c.triggered = true
            }
            if (c.triggered && !c.fallen) {
                c.velocityY += 2f
                c.rect.top += c.velocityY
                c.rect.bottom += c.velocityY
                if (c.rect.bottom >= height * 0.85f) {
                    c.fallen = true
                }
                if (RectF.intersects(c.rect, RectF(playerX, playerY, playerX + playerSize, playerY + playerSize))) {
                    die()
                    return
                }
            }
        }

        for (s in spikes) {
            if (RectF.intersects(s, RectF(playerX, playerY, playerX + playerSize, playerY + playerSize))) {
                die()
                return
            }
        }

        if (jump && onGround) {
            velocityY = jumpPower
            onGround = false
        }

        if (playerX < 0) playerX = 0f

        if (playerY > height + 300) {
            die()
            return
        }

if (RectF.intersects(doorRect, RectF(playerX, playerY, playerX + playerSize, playerY + playerSize))) {
    onLevelComplete?.invoke()
}
    }

    private fun die() {
        if (isDead) return
        isDead = true
        deaths++
        deathFlashTimer = 30  // ~0.5 секунды
    }

    private fun performRespawn() {
        playerX = spawnX
        playerY = spawnY
        velocityX = 0f
        velocityY = 0f
        cameraX = 0f
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
        isDead = false
    }
}
