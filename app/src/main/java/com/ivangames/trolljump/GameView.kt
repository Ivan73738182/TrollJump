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
    private var playerSize = 60f
    private var velocityY = 0f
    private var velocityX = 0f

    // Физика
    private val gravity = 1.5f
    private val jumpPower = -22f
    private val moveSpeed = 8f
    private var onGround = false

    // Платформы (координаты: left, top, right, bottom)
    private val platforms = mutableListOf<RectF>()

    // Дверь (цель)
    private var doorRect = RectF()

    // Управление
    var moveLeft = false
    var moveRight = false
    var jump = false

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

    init {
        // Пока ставим платформы фиксированно (потом сделаем уровень)
        platforms.add(RectF(0f, 800f, 2000f, 850f))     // Основной пол
        platforms.add(RectF(400f, 650f, 700f, 690f))    // Платформа-ступенька
        platforms.add(RectF(900f, 550f, 1300f, 590f))   // Верхняя платформа

        doorRect = RectF(1700f, 700f, 1800f, 800f)      // Дверь в конце
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Фон
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

        // Проверка столкновения с платформами (только сверху)
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

        // Не улетать за экран (грубо)
        if (playerX < 0) playerX = 0f
        if (playerY > height + 200) {
            // Упал — респавн
            playerX = 200f
            playerY = 400f
            velocityY = 0f
        }
    }
}
