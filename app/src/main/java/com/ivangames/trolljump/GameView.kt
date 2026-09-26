package com.ivangames.trolljump

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Shader
import android.media.AudioManager
import android.media.ToneGenerator
import android.util.AttributeSet
import android.view.View
import kotlin.random.Random

class GameView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    var levelNumber = 1
    var world = "forest"

    private var playerX = 200f
    private var playerY = 400f
    private var playerSize = 80f
    private var velocityY = 0f
    private var velocityX = 0f
    private var spawnX = 200f
    private var spawnY = 400f

    private val gravity = 1.5f
    private val jumpPower = -25f
    private val moveSpeed = 10f
    private var onGround = false

    private var cameraX = 0f

    private var deaths = 0
    private var deathFlashTimer = 0
    private var isDead = false

    private var levelData: LevelData? = null
    private var initialized = false

    var moveLeft = false
    var moveRight = false
    var jump = false

    var onLevelComplete: (() -> Unit)? = null

    private var toneGen: ToneGenerator? = null

    private var walkTimer = 0
    private var facingRight = true
    private var isWalking = false

    private var platformColor = "#8B7355"
    private var disappearingColor = "#A0522D"
    private var spikeColor = "#E53935"
    private var ceilingColor = "#666666"
    private var ceilingActiveColor = "#B71C1C"
    private var doorColor = "#4FC3F7"

    // Эффекты
    private var particleList = mutableListOf<Particle>()
    private var dustList = mutableListOf<Dust>()
    private var winEffectActive = false
    private var winEffectTimer = 0

    class Particle(var x: Float, var y: Float, var vx: Float, var vy: Float, var life: Int, var size: Float)
    class Dust(var x: Float, var y: Float, var vx: Float, var vy: Float, var life: Int)

    private val platformPaint = Paint().apply { style = Paint.Style.FILL }
    private val disappearingPaint = Paint().apply { style = Paint.Style.FILL }
    private val gonePaint = Paint().apply {
        color = Color.parseColor("#333333")
        style = Paint.Style.FILL
    }
    private val doorPaint = Paint().apply { style = Paint.Style.FILL }
    private val doorDarkPaint = Paint().apply {
        color = Color.parseColor("#2A2A2A")
        style = Paint.Style.FILL
    }
    private val doorHandlePaint = Paint().apply {
        color = Color.parseColor("#FFC107")
        style = Paint.Style.FILL
    }
    private val doorGlowPaint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
    }
    private val spikePaint = Paint().apply { style = Paint.Style.FILL }
    private val ceilingPaint = Paint().apply { style = Paint.Style.FILL }
    private val ceilingActivePaint = Paint().apply { style = Paint.Style.FILL }
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
    private val bgPaint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
    }
    private val charPaint = Paint().apply {
        color = Color.parseColor("#1A1A1A")
        style = Paint.Style.FILL
    }
    private val charPaintWhite = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.FILL
    }
    private val trunkPaint = Paint().apply {
        color = Color.parseColor("#5D4037")
        style = Paint.Style.FILL
    }
    private val crackPaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 3f
        isAntiAlias = true
    }
    private val spikeEyePaint = Paint().apply {
        color = Color.parseColor("#4A0000")
        style = Paint.Style.FILL
    }
    private val dustPaint = Paint().apply {
        color = Color.parseColor("#CCCCCC")
        style = Paint.Style.FILL
        isAntiAlias = true
    }
    private val starPaint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private var treePositions: List<Float> = emptyList()
    private var mountainPositions: List<Float> = emptyList()

    init {
        try {
            toneGen = ToneGenerator(AudioManager.STREAM_MUSIC, 80)
        } catch (e: Exception) {
            toneGen = null
        }
    }

    private fun applyWorldColors() {
        when (world) {
            "mountains" -> {
                platformColor = "#78909C"
                disappearingColor = "#90A4AE"
                spikeColor = "#81D4FA"
                ceilingColor = "#546E7A"
                ceilingActiveColor = "#37474F"
                doorColor = "#FFFFFF"
            }
            "sea" -> {
                platformColor = "#FFE0B2"
                disappearingColor = "#FFCC80"
                spikeColor = "#AB47BC"
                ceilingColor = "#0288D1"
                ceilingActiveColor = "#01579B"
                doorColor = "#00E5FF"
            }
            else -> {
                platformColor = "#8B7355"
                disappearingColor = "#A0522D"
                spikeColor = "#E53935"
                ceilingColor = "#666666"
                ceilingActiveColor = "#B71C1C"
                doorColor = "#4FC3F7"
            }
        }
        platformPaint.color = Color.parseColor(platformColor)
        disappearingPaint.color = Color.parseColor(disappearingColor)
        spikePaint.color = Color.parseColor(spikeColor)
        ceilingPaint.color = Color.parseColor(ceilingColor)
        ceilingActivePaint.color = Color.parseColor(ceilingActiveColor)
        doorPaint.color = Color.parseColor(doorColor)
    }

    private fun setupLevel() {
        val w = width.toFloat()
        val h = height.toFloat()

        applyWorldColors()
        levelData = Levels.buildLevel(world, levelNumber, w, h)

        playerX = w * 0.10f
        playerY = h * 0.85f - playerSize
        spawnX = playerX
        spawnY = playerY
        deaths = 0
        isDead = false
        deathFlashTimer = 0
        cameraX = 0f
        particleList.clear()
        dustList.clear()
        winEffectActive = false
        winEffectTimer = 0

        val totalWidth = w * 3f
        val treeList = mutableListOf<Float>()
        var x = 0f
        while (x < totalWidth) {
            treeList.add(x)
            x += Random.nextFloat() * 100f + 80f
        }
        treePositions = treeList

        val mountainList = mutableListOf<Float>()
        x = 0f
        while (x < totalWidth) {
            mountainList.add(x)
            x += Random.nextFloat() * 200f + 150f
        }
        mountainPositions = mountainList

        initialized = true
    }

    private fun drawBackground(canvas: Canvas) {
        when (world) {
            "mountains" -> drawMountainBackground(canvas)
            "sea" -> drawSeaBackground(canvas)
            else -> drawForestBackground(canvas)
        }
    }

    private fun drawForestBackground(canvas: Canvas) {
        val w = width.toFloat()
        val h = height.toFloat()

        val skyShader = LinearGradient(
            0f, 0f, 0f, h,
            intArrayOf(
                Color.parseColor("#1A3A5C"),
                Color.parseColor("#2E5D4F"),
                Color.parseColor("#1A1A1A")
            ),
            floatArrayOf(0f, 0.6f, 1f),
            Shader.TileMode.CLAMP
        )
        bgPaint.shader = skyShader
        canvas.drawRect(0f, 0f, w, h, bgPaint)
        bgPaint.shader = null

        val mountainPath = Path()
        mountainPath.moveTo(0f, h * 0.75f)
        var mx = 0f
        while (mx < w + 100f) {
            mountainPath.lineTo(mx, h * 0.55f)
            mountainPath.lineTo(mx + 80f, h * 0.75f)
            mx += 160f
        }
        mountainPath.lineTo(w, h * 0.75f)
        mountainPath.lineTo(w, h * 0.85f)
        mountainPath.lineTo(0f, h * 0.85f)
        mountainPath.close()
        bgPaint.color = Color.parseColor("#2A4A3A")
        canvas.drawPath(mountainPath, bgPaint)

        val camOffset = cameraX
        drawTreeLayer(canvas, camOffset * 0.3f, 0.72f, 40f, Paint().apply { color = Color.parseColor("#1B4A2E"); style = Paint.Style.FILL })
        drawTreeLayer(canvas, camOffset * 0.6f, 0.78f, 60f, Paint().apply { color = Color.parseColor("#236B3F"); style = Paint.Style.FILL })
        drawTreeLayer(canvas, camOffset * 0.9f, 0.84f, 80f, Paint().apply { color = Color.parseColor("#2E8B57"); style = Paint.Style.FILL })
    }

    private fun drawTreeLayer(canvas: Canvas, offset: Float, baseYRatio: Float, size: Float, treePaint: Paint) {
        val h = height.toFloat()
        val baseY = h * baseYRatio

        for (treeX in treePositions) {
            val screenX = treeX - offset
            if (screenX < -size * 2 || screenX > width + size * 2) continue

            canvas.drawRect(screenX + size * 0.35f, baseY, screenX + size * 0.65f, baseY + size * 0.4f, trunkPaint)
            canvas.drawRect(screenX, baseY - size * 0.4f, screenX + size, baseY, treePaint)
            canvas.drawRect(screenX + size * 0.15f, baseY - size * 0.7f, screenX + size * 0.85f, baseY - size * 0.3f, treePaint)
            canvas.drawRect(screenX + size * 0.3f, baseY - size * 0.9f, screenX + size * 0.7f, baseY - size * 0.6f, treePaint)
        }
    }
    private fun drawMountainBackground(canvas: Canvas) {
        val w = width.toFloat()
        val h = height.toFloat()

        val skyShader = LinearGradient(
            0f, 0f, 0f, h,
            intArrayOf(
                Color.parseColor("#4A6B8A"),
                Color.parseColor("#7BA0C0"),
                Color.parseColor("#2A3A4A")
            ),
            floatArrayOf(0f, 0.5f, 1f),
            Shader.TileMode.CLAMP
        )
        bgPaint.shader = skyShader
        canvas.drawRect(0f, 0f, w, h, bgPaint)
        bgPaint.shader = null

        val camOffset = cameraX
        drawMountainLayer(canvas, camOffset * 0.3f, 0.75f, 120f, Color.parseColor("#E0E8F0"))
        drawMountainLayer(canvas, camOffset * 0.6f, 0.82f, 160f, Color.parseColor("#B0C0D0"))
        drawMountainLayer(canvas, camOffset * 0.9f, 0.88f, 200f, Color.parseColor("#8090A0"))
    }

    private fun drawMountainLayer(canvas: Canvas, offset: Float, baseYRatio: Float, size: Float, color: Int) {
        val h = height.toFloat()
        val baseY = h * baseYRatio

        val paint = Paint().apply {
            this.color = color
            style = Paint.Style.FILL
            isAntiAlias = true
        }

        for (mountainX in mountainPositions) {
            val screenX = mountainX - offset
            if (screenX < -size * 2 || screenX > width + size * 2) continue

            val path = Path()
            path.moveTo(screenX, baseY)
            path.lineTo(screenX + size / 2, baseY - size * 0.6f)
            path.lineTo(screenX + size, baseY)
            path.close()
            canvas.drawPath(path, paint)
        }
    }

    private fun drawSeaBackground(canvas: Canvas) {
        val w = width.toFloat()
        val h = height.toFloat()

        val skyShader = LinearGradient(
            0f, 0f, 0f, h,
            intArrayOf(
                Color.parseColor("#0D47A1"),
                Color.parseColor("#1976D2"),
                Color.parseColor("#00ACC1")
            ),
            floatArrayOf(0f, 0.5f, 1f),
            Shader.TileMode.CLAMP
        )
        bgPaint.shader = skyShader
        canvas.drawRect(0f, 0f, w, h, bgPaint)
        bgPaint.shader = null

        val bubblePaint = Paint().apply {
            color = Color.parseColor("#80E0FF")
            style = Paint.Style.FILL
            alpha = 120
        }
        val camOffset = cameraX
        for (i in 0 until 40) {
            val bx = (i * 137f - camOffset * 0.5f) % (w + 200f) - 100f
            val by = (i * 73f) % h
            val bs = 8f + (i % 5) * 4f
            canvas.drawCircle(bx, by, bs, bubblePaint)
        }

        val weedPaint = Paint().apply {
            color = Color.parseColor("#1B5E20")
            style = Paint.Style.FILL
        }
        for (i in 0 until 20) {
            val wx = (i * 200f - camOffset * 0.7f) % (w + 400f) - 200f
            val baseY = h * 0.92f
            for (j in 0 until 3) {
                val offsetX = j * 12f - 12f
                canvas.drawRect(wx + offsetX, baseY - 60f + j * 10f, wx + offsetX + 8f, baseY, weedPaint)
            }
        }
    }

    private fun drawSpikes(canvas: Canvas, rect: RectF) {
        val spikeWidth = rect.width() / 5f
        val spikeHeight = rect.height()
        val baseY = rect.bottom

        for (i in 0 until 5) {
            val x = rect.left + i * spikeWidth
            val path = Path()
            path.moveTo(x, baseY)
            path.lineTo(x + spikeWidth / 2f, baseY - spikeHeight)
            path.lineTo(x + spikeWidth, baseY)
            path.close()
            canvas.drawPath(path, spikePaint)
        }

        for (i in 0 until 5) {
            val x = rect.left + i * spikeWidth + spikeWidth / 2f
            canvas.drawCircle(x, baseY - spikeHeight * 0.3f, spikeWidth * 0.08f, spikeEyePaint)
        }
    }

    private fun drawCeiling(canvas: Canvas, c: FallingCeiling) {
        val paint = if (c.triggered) ceilingActivePaint else ceilingPaint
        canvas.drawRect(c.rect, paint)

        crackPaint.color = if (c.triggered) Color.parseColor("#2A0000") else Color.parseColor("#333333")

        val w = c.rect.width()
        val h = c.rect.height()

        val path1 = Path()
        path1.moveTo(c.rect.left + w * 0.25f, c.rect.top)
        path1.lineTo(c.rect.left + w * 0.30f, c.rect.top + h * 0.5f)
        path1.lineTo(c.rect.left + w * 0.22f, c.rect.bottom)
        canvas.drawPath(path1, crackPaint)

        val path2 = Path()
        path2.moveTo(c.rect.left + w * 0.65f, c.rect.top)
        path2.lineTo(c.rect.left + w * 0.72f, c.rect.top + h * 0.6f)
        path2.lineTo(c.rect.left + w * 0.68f, c.rect.bottom)
        canvas.drawPath(path2, crackPaint)

        if (c.triggered) {
            val glowPaint = Paint().apply {
                color = Color.parseColor("#66FF0000")
                style = Paint.Style.FILL
            }
            canvas.drawRect(c.rect, glowPaint)
        }
    }

    private fun drawDoor(canvas: Canvas, rect: RectF) {
        val w = rect.width()
        val h = rect.height()

        // Свечение вокруг двери
        doorGlowPaint.color = Color.parseColor(doorColor)
        doorGlowPaint.alpha = 80
        canvas.drawRect(
            rect.left - 8f, rect.top - 8f,
            rect.right + 8f, rect.bottom + 8f,
            doorGlowPaint
        )

        // Основа двери
        canvas.drawRect(rect, doorPaint)

        // Тёмная рамка
        val borderPaint = Paint().apply {
            color = Color.parseColor("#1A1A1A")
            style = Paint.Style.STROKE
            strokeWidth = 4f
        }
        canvas.drawRect(rect, borderPaint)

        // Внутренняя арка (тёмная часть двери)
        val arcRect = RectF(
            rect.left + w * 0.15f,
            rect.top + h * 0.10f,
            rect.right - w * 0.15f,
            rect.bottom - h * 0.25f
        )
        canvas.drawRect(arcRect, doorDarkPaint)

        // Ручка двери
        val handleX = rect.left + w * 0.75f
        val handleY = rect.top + h * 0.55f
        val handleSize = w * 0.12f
        canvas.drawCircle(handleX, handleY, handleSize, doorHandlePaint)
    }

    private fun drawCharacter(canvas: Canvas) {
        val size = playerSize
        val px = playerX
        val py = playerY

        if (velocityX != 0f && onGround) {
            isWalking = true
            walkTimer = (walkTimer + 1) % 20
            if (velocityX > 0) facingRight = true
            else if (velocityX < 0) facingRight = false
        } else {
            isWalking = false
        }

        val legOffset = if (isWalking) {
            if (walkTimer < 10) 6f else -6f
        } else 0f

        val headW = size * 0.40f
        val headH = size * 0.30f
        val headX = px + size * 0.30f
        val headY = py

        val bodyW = size * 0.70f
        val bodyH = size * 0.40f
        val bodyX = px + size * 0.15f
        val bodyY = py + size * 0.30f

        val legW = size * 0.20f
        val legH = size * 0.30f
        val legY = py + size * 0.70f
        val leftLegX = px + size * 0.20f + legOffset
        val rightLegX = px + size * 0.60f - legOffset

        canvas.drawRect(leftLegX, legY, leftLegX + legW, legY + legH, charPaint)
        canvas.drawRect(rightLegX, legY, rightLegX + legW, legY + legH, charPaint)
        canvas.drawRect(bodyX, bodyY, bodyX + bodyW, bodyY + bodyH, charPaint)
        canvas.drawRect(headX, headY, headX + headW, headY + headH, charPaint)

        val eyeSize = size * 0.06f
        val eyeY = headY + headH * 0.40f
        val eyeSpacing = size * 0.09f

        val eyeShift = if (facingRight) size * 0.06f else -size * 0.06f
        val eyeCenterX = headX + headW / 2f + eyeShift

        canvas.drawRect(
            eyeCenterX - eyeSpacing / 2f - eyeSize / 2f,
            eyeY,
            eyeCenterX - eyeSpacing / 2f + eyeSize / 2f,
            eyeY + eyeSize,
            charPaintWhite
        )
        canvas.drawRect(
            eyeCenterX + eyeSpacing / 2f - eyeSize / 2f,
            eyeY,
            eyeCenterX + eyeSpacing / 2f + eyeSize / 2f,
            eyeY + eyeSize,
            charPaintWhite
        )
    }

    private fun spawnDust() {
        if (!onGround || velocityX == 0f) return
        val dustX = playerX + playerSize / 2f
        val dustY = playerY + playerSize
        dustList.add(Dust(dustX, dustY, -velocityX * 0.3f + Random.nextFloat() * 2f - 1f, -Random.nextFloat() * 2f, 20))
    }

    private fun updateDust() {
        val iter = dustList.iterator()
        while (iter.hasNext()) {
            val d = iter.next()
            d.x += d.vx
            d.y += d.vy
            d.vy += 0.2f
            d.life--
            if (d.life <= 0) iter.remove()
        }
    }

    private fun drawDust(canvas: Canvas) {
        for (d in dustList) {
            val alpha = (d.life / 20f * 200).toInt().coerceIn(0, 200)
            dustPaint.alpha = alpha
            canvas.drawCircle(d.x, d.y, 6f, dustPaint)
        }
    }

    private fun spawnWinParticles() {
        val doorCenterX = (levelData?.door?.left ?: 0f) + (levelData?.door?.width() ?: 0f) / 2f
        val doorCenterY = (levelData?.door?.top ?: 0f) + (levelData?.door?.height() ?: 0f) / 2f
        for (i in 0 until 60) {
            val angle = Random.nextFloat() * 2f * Math.PI.toFloat()
            val speed = Random.nextFloat() * 8f + 3f
            particleList.add(Particle(
                doorCenterX, doorCenterY,
                Math.cos(angle.toDouble()).toFloat() * speed,
                Math.sin(angle.toDouble()).toFloat() * speed,
                60,
                Random.nextFloat() * 8f + 4f
            ))
        }
    }

    private fun updateParticles() {
        val iter = particleList.iterator()
        while (iter.hasNext()) {
            val p = iter.next()
            p.x += p.vx
            p.y += p.vy
            p.vy += 0.3f
            p.vx *= 0.98f
            p.life--
            if (p.life <= 0) iter.remove()
        }
    }

    private fun drawParticles(canvas: Canvas) {
        val colors = intArrayOf(
            Color.parseColor("#FFC107"),
            Color.parseColor("#FFEB3B"),
            Color.parseColor("#FF9800"),
            Color.parseColor("#FFFFFF")
        )
        for (p in particleList) {
            val alpha = (p.life / 60f * 255).toInt().coerceIn(0, 255)
            starPaint.color = colors[Random.nextInt(colors.size)]
            starPaint.alpha = alpha
            canvas.drawCircle(p.x, p.y, p.size, starPaint)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (!initialized && width > 0 && height > 0) {
            setupLevel()
        }

        val data = levelData ?: return

        drawBackground(canvas)

        val w = width.toFloat()
        val targetCamX = (playerX - w * 0.35f).coerceAtLeast(0f)
        cameraX = cameraX + (targetCamX - cameraX) * 0.1f

        canvas.save()
        canvas.translate(-cameraX, 0f)

        // Платформы
        for (p in data.platforms) {
            if (p.disappearing && p.gone) {
                canvas.drawRect(p.rect, gonePaint)
            } else if (p.disappearing && p.timer > 0) {
                val blink = (p.timer / 5) % 2 == 0
                canvas.drawRect(p.rect, if (blink) disappearingPaint else gonePaint)
            } else {
                canvas.drawRect(p.rect, platformPaint)

                if (p.disappearing) {
                    val cp = Paint().apply {
                        color = Color.parseColor("#4A2A10")
                        style = Paint.Style.STROKE
                        strokeWidth = 2f
                    }
                    val r = p.rect
                    canvas.drawLine(r.left + r.width() * 0.3f, r.top, r.left + r.width() * 0.4f, r.bottom, cp)
                    canvas.drawLine(r.left + r.width() * 0.7f, r.top, r.left + r.width() * 0.6f, r.bottom, cp)
                }
            }
        }

        // Шипы
        for (s in data.spikes) {
            drawSpikes(canvas, s)
        }

        // Потолки
        for (c in data.fallingCeilings) {
            drawCeiling(canvas, c)
        }

        // Дверь
        drawDoor(canvas, data.door)

        // Пыль под ногами
        drawDust(canvas)

        // Персонаж
        drawCharacter(canvas)

        // Частицы победы
        drawParticles(canvas)

        canvas.restore()

        // HUD
        hudPaint.textSize = height * 0.05f
        canvas.drawText("💀 $deaths", 40f, height * 0.10f, hudPaint)

        // Красная вспышка
        if (deathFlashTimer > 0) {
            val alpha = (deathFlashTimer / 30f * 200).toInt().coerceIn(0, 200)
            deathOverlayPaint.alpha = alpha
            canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), deathOverlayPaint)
        }

        // Золотая вспышка победы
        if (winEffectActive) {
            val winOverlay = Paint().apply {
                color = Color.parseColor("#FFD700")
                alpha = (winEffectTimer / 60f * 100).toInt().coerceIn(0, 100)
            }
            canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), winOverlay)
            winEffectTimer--
            if (winEffectTimer <= 0) winEffectActive = false
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
            playTone(ToneGenerator.TONE_PROP_BEEP, 80)
        }

        if (playerX < 0) playerX = 0f

        if (playerY > height + 300) {
            die()
            return
        }

        // Пыль при беге
        if (isWalking && walkTimer % 3 == 0) {
            spawnDust()
        }
        updateDust()
        updateParticles()

        // Проверка двери
        if (RectF.intersects(data.door, RectF(playerX, playerY, playerX + playerSize, playerY + playerSize))) {
            if (!winEffectActive) {
                playTone(ToneGenerator.TONE_PROP_ACK, 300)
                winEffectActive = true
                winEffectTimer = 60
                spawnWinParticles()
                onLevelComplete?.invoke()
            }
        }
    }

    private fun die() {
        if (isDead) return
        isDead = true
        deaths++
        deathFlashTimer = 30
        playTone(ToneGenerator.TONE_CDMA_ABBR_ALERT, 200)
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

    private fun playTone(tone: Int, durationMs: Int) {
        try {
            toneGen?.startTone(tone, durationMs)
        } catch (e: Exception) {
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        try {
            toneGen?.release()
        } catch (e: Exception) {
        }
        toneGen = null
    }
}
