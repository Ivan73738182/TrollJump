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

    // Физика
    private val gravity = 1.5f
    private val jumpPower = -25f
    private val moveSpeed = 10f
    private var onGround = false

    // Уровень
    private val platforms = mutableListOf<RectF>()
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
    private val doorPaint = Paint().apply {
        color = Color.parseColor("#4FC3F7")
        style = Paint.Style.FILL
    }

    private fun setupLevel() {
        val w = width.toFloat()
        val h = height.toFloat()

        platforms.clear()

        // Основной пол
        platforms.add(RectF(0f, h * 0.85f, w * 1.5f, h * 0.90f))
        // Ступенька
        platforms.add(RectF(w * 0.30f, h * 0.65f, w * 0.50f, h * 0.68f))
        // Верхняя платформа
        platforms.add(RectF(w * 0.65f, h * 0.50f, w * 1.00f, h * 0.53f))

        // Дверь в конце уровня
        doorRect = RectF(w * 1.30f, h * 0.75f, w * 1.38f, h * 0.85f)

        // Игрок слева на полу
        playerX = w * 0.10f
        playerY = h * 0.85f - playerSize

        initialized = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (!initialized && width > 0 && height > 0) {
            setupLevel()
        }

        canvas.drawColor(Color.parseColor("#1A1A1A"))

        // Платформы
        for (p in platforms) {
            canvas.drawRect(p, platformPaint)
        }

        // Дверь
        canvas.drawRect(doorRect, doorPaint)

        // Игрок
        canvas.drawRect(
            playerX, playerY,
            playerX + playerSize, playerY + playerSize,
            playerPaint
        )

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

        // Приземление на платформы
        onGround = false
        for (p in platforms) {
            if (playerX + playerSize > p.left && playerX < p.right) {
                if (playerY + playerSize in p.top..p.top + 30f && velocityY >= 0) {
                    playerY = p.top - playerSize
                    velocityY = 0f
                    onGround = true
                }
            }
        }

        // Прыжок
        if (jump && onGround) {
            velocityY = jumpPower
            onGround = false
        }

        // Границы
        if (playerX < 0) playerX = 0f

        // Падение — респавн
        if (playerY > height + 300) {
            playerX = width * 0.10f
            playerY = height * 0.85f - playerSize
            velocityY = 0f
        }
    }
}
