package com.example.devclicker.ui.theme

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.devclicker.data.repository.GameData
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlin.random.Random

private data class MatrixStream(
    val id: Int = Random.nextInt(),
    val snippet: String,
    val x: Float, // Posição X (horizontal)
    var y: Float, // Posição Y (vertical)
    val speed: Float,
    val length: Int = snippet.length,
    val charHeight: Float
) {
    fun getAlpha(charIndex: Int): Float {
        if (charIndex == length - 1) return 1.0f
        val alpha = (charIndex.toFloat() / length.toFloat()) * 0.7f + 0.3f
        return alpha
    }
}

@Composable
fun MatrixBackground(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF00C853),
    fontSizeDp: Int = 14,
    maxStreams: Int = 25,
    streamSpeedRange: Pair<Float, Float> = 100f to 250f
) {
    val streams = remember { mutableListOf<MatrixStream>() }
    val density = LocalDensity.current
    val fontSizePx = with(density) { fontSizeDp.dp.toPx() }
    val charHeight = fontSizePx * 1.2f
    var screenSize by remember { mutableStateOf(Size.Zero) }

    var ticker by remember { mutableStateOf(0) }

    val textPaint = remember {
        Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            typeface = Typeface.MONOSPACE
            textSize = fontSizePx
        }
    }
    val colorInt = remember(color) { color.toArgb() }
    val headColorInt = remember { Color.White.toArgb() }

    LaunchedEffect(Unit) {
        var lastFrameTimeNanos = System.nanoTime()
        while (isActive) {
            withFrameNanos { }

            val currentTimeNanos = System.nanoTime()
            val deltaTimeMs = (currentTimeNanos - lastFrameTimeNanos) / 1_000_000f
            val deltaTimeSeconds = deltaTimeMs / 1000f
            lastFrameTimeNanos = currentTimeNanos

            streams.forEach { stream ->
                stream.y += stream.speed * deltaTimeSeconds
            }

            streams.removeAll { it.y > screenSize.height + (it.length * it.charHeight) }
            ticker++
        }
    }

    LaunchedEffect(screenSize) {
        while (isActive) {
            if (screenSize != Size.Zero && streams.size < maxStreams) {
                val snippet = GameData.codeSnippets.random()
                streams.add(
                    MatrixStream(
                        snippet = snippet,
                        x = Random.nextFloat() * screenSize.width,
                        y = -(snippet.length * charHeight),
                        speed = Random.nextFloat() * (streamSpeedRange.second - streamSpeedRange.first) + streamSpeedRange.first,
                        charHeight = charHeight
                    )
                )
            }
            delay(Random.nextLong(100, 500))
        }
    }


    Canvas(
        modifier = modifier.fillMaxSize()
    ) {
        screenSize = size
        val currentTicker = ticker

        drawRect(Color.Black) // Fundo preto

        drawIntoCanvas { canvas ->
            streams.forEach { stream ->
                stream.snippet.forEachIndexed { index, char ->
                    val charY = stream.y + (index * stream.charHeight)

                    if (charY < 0 || charY > size.height) return@forEachIndexed

                    val isHead = index == stream.length - 1
                    textPaint.color = if (isHead) headColorInt else colorInt
                    textPaint.alpha = (stream.getAlpha(index) * 255).toInt()

                    canvas.nativeCanvas.drawText(
                        char.toString(),
                        stream.x,
                        charY,
                        textPaint
                    )
                }
            }
        }
    }
}