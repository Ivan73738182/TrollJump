private fun drawCharacter(canvas: Canvas) {
    val size = playerSize
    val px = playerX
    val py = playerY

    // Анимация ходьбы
    if (velocityX != 0f && onGround) {
        isWalking = true
        walkTimer = (walkTimer + 1) % 20
        if (velocityX > 0) facingRight = true
        else if (velocityX < 0) facingRight = false
    } else {
        isWalking = false
    }

    // Смещение ног при ходьбе
    val legOffset = if (isWalking) {
        if (walkTimer < 10) 6f else -6f
    } else 0f

    // Размеры частей тела
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

    val charPaint = Paint().apply {
        color = Color.parseColor("#1A1A1A")
        style = Paint.Style.FILL
    }

    // Левая нога
    canvas.drawRect(
        leftLegX,
        legY,
        leftLegX + legW,
        legY + legH,
        charPaint
    )

    // Правая нога
    canvas.drawRect(
        rightLegX,
        legY,
        rightLegX + legW,
        legY + legH,
        charPaint
    )

    // Тело
    canvas.drawRect(
        bodyX,
        bodyY,
        bodyX + bodyW,
        bodyY + bodyH,
        charPaint
    )

    // Голова
    canvas.drawRect(
        headX,
        headY,
        headX + headW,
        headY + headH,
        charPaint
    )
}
