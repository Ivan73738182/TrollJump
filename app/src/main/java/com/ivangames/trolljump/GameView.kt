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

    var levelNumber = 1

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
    private var levelData: LevelData? = null
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

    private fun setupLevel() {
        val w = width.toFloat()
        val h = height.toFloat()

        levelData = Levels.buildLevel(levelNumber, w, h)

        playerX = w * 0.10f
        playerY = h * 0.85f - playerSize
        spawnX = playerX
        spawnY = playerY
        deaths = 0
        isDead = false
        deathFlashTimer = 0
        cameraX = 0f

        initialized = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (!initialized && width > 0 && height > 0) {
            setupLevel()
        }

        val data = levelData ?: return

        canvas.drawColor(Color.parseColor("#1A1A1A"))

        val w = width.toFloat()
        val targetCamX = (playerX - w * 0.35f).coerceAtLeast(0f)
        cameraX = cameraX + (targetCamX - cameraX) * 0.1f

        canvas.save()
        canvas.translate(-cameraX, 0f)

        for (p in data.platforms) {
            if (p.disappearing && p.gone) {
                canvas.drawRect(p.rect, gonePaint)
            } else if (p.disappearing && p.timer > 0) {
                canvas.drawRect(p.rect, disappearingPaint)
            } else {
                canvas.drawRect(p.rect, platformPaint)
            }
        }

        for (s in data.spikes) {
            canvas.drawRect(s, spikePaint)
        }

        for (c in data.fallingCeilings) {
            canvas.drawRect(c.rect, if (c.triggered) ceilingActivePaint else ceilingPaint)
        }

        canvas.drawRect(data.door, doorPaint)

        canvas.drawRect(
            playerX, playerY,
            playerX + playerSize, playerY + playerSize,
            playerPaint
        )

        canvas.restore()

        hudPaint.textSize = height * 0.05f
        canvas.drawText("💀 $deaths", 40f, height * 0.10f, hudPaint)

        if (deathFlashTimer > 0) {
            val alpha = (deathFlashTimer / 30f * 200).toInt().coerceIn(0, 200)
            deathOverlayPaint.alpha = alpha
            canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), deathOverlayPaint)
        }

        update()
        invalidate()
    }

    private fun update() {
        val data = levelData ?: return

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
        for (p in data.platforms) {
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

        for (p in data.platforms) {
            if (p.disappearing && p.timer > 0 && !p.gone) {
                p.timer--
                if (p.timer == 0) p.gone = true
            }
        }

        for (c in data.fallingCeilings) {
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

        for (s in data.spikes) {
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

        if (RectF.intersects(data.door, RectF(playerX, playerY, playerX + playerSize, playerY + playerSize))) {
            onLevelComplete?.invoke()
        }
    }

    private fun die() {
        if (isDead) return
        isDead = true
        deaths++
        deathFlashTimer = 30
    }

    private fun performRespawn() {
        val data = levelData ?: return
        playerX = spawnX
        playerY = spawnY
        velocityX = 0f
        velocityY = 0f
        cameraX = 0f
        for (p in data.platforms) {
            p.timer = 0
            p.gone = false
        }
        for (c in data.fallingCeilings) {
            c.triggered = false
            c.fallen = false
            c.velocityY = 0f
            c.rect.top = height * 0.05f
            c.rect.bottom = height * 0.15f
        }
        isDead = false
    }
}
