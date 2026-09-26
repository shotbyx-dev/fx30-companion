package com.fx30companion.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fx30companion.app.data.ExposureMath
import com.fx30companion.app.data.demoScene
import com.fx30companion.app.ui.theme.FxAmber
import com.fx30companion.app.ui.theme.FxBlue
import com.fx30companion.app.ui.theme.FxGreen
import com.fx30companion.app.ui.theme.FxOrange
import com.fx30companion.app.ui.theme.FxRed
import kotlin.math.exp
import kotlin.math.pow
import kotlin.math.sin

@Composable
fun SectionCard(
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            if (subtitle != null) {
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 2.dp, bottom = 8.dp)
                )
            } else {
                Spacer(Modifier.height(8.dp))
            }
            content()
        }
    }
}

@Composable
fun SettingRow(label: String, value: String) {
    Row(modifier = Modifier.padding(vertical = 5.dp)) {
        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(0.9f)
        )
        Text(
            value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1.6f)
        )
    }
}

private fun ireToY(ire: Float, height: Float, maxIre: Float = 110f): Float =
    height * (1f - (ire / maxIre).coerceIn(0f, 1f))

/**
 * Interactive waveform monitor: draws the demo scene's luminance zones as
 * traces, shifted by the exposure offset. This is what "correct" looks like:
 * skin trace sitting in the 48–52% band, grey at 41%, nothing at 93%+.
 */
@Composable
fun WaveformMonitor(exposureEv: Float, modifier: Modifier = Modifier) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(250.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF08080A))
    ) {
        val w = size.width
        val h = size.height
        val maxIre = 110f

        // Target bands (drawn first, under the traces)
        drawRect(
            FxGreen.copy(alpha = 0.10f),
            topLeft = Offset(0f, ireToY(52f, h, maxIre)),
            size = Size(w, ireToY(48f, h, maxIre) - ireToY(52f, h, maxIre))
        )
        drawRect(
            FxRed.copy(alpha = 0.10f),
            topLeft = Offset(0f, 0f),
            size = Size(w, ireToY(93f, h, maxIre))
        )
        val dash = PathEffect.dashPathEffect(floatArrayOf(9f, 9f), 0f)
        drawLine(
            Color.White.copy(alpha = 0.55f), Offset(0f, ireToY(41f, h, maxIre)),
            Offset(w, ireToY(41f, h, maxIre)), strokeWidth = 1.5f, pathEffect = dash
        )
        drawLine(
            Color.White.copy(alpha = 0.35f), Offset(0f, ireToY(61f, h, maxIre)),
            Offset(w, ireToY(61f, h, maxIre)), strokeWidth = 1.5f, pathEffect = dash
        )
        drawLine(
            FxRed.copy(alpha = 0.6f), Offset(0f, ireToY(93f, h, maxIre)),
            Offset(w, ireToY(93f, h, maxIre)), strokeWidth = 1.5f, pathEffect = dash
        )

        // Gridlines + labels
        drawIntoCanvas { canvas ->
            val paint = android.graphics.Paint().apply {
                color = android.graphics.Color.argb(140, 255, 255, 255)
                textSize = 11.sp.toPx()
                isAntiAlias = true
            }
            var ire = 0
            while (ire <= 100) {
                val y = ireToY(ire.toFloat(), h, maxIre)
                val major = ire % 20 == 0
                drawLine(
                    Color.White.copy(alpha = if (major) 0.16f else 0.07f),
                    Offset(0f, y), Offset(w, y), strokeWidth = 1f
                )
                if (major) canvas.nativeCanvas.drawText("$ire", 8f, y - 5f, paint)
                ire += 10
            }
        }

        // Scene traces
        val steps = 140
        val stepX = w / steps
        demoScene.forEachIndexed { zi, zone ->
            val ireVal = ExposureMath.ireAtStops(zone.stopsRelGrey, exposureEv.toDouble()).toFloat()
            val baseY = ireToY(ireVal, h, maxIre)
            val path = Path()
            for (i in 0..steps) {
                val x = i * stepX
                val wobble = sin(i * 0.33f + zi * 1.7f) * h * 0.008f +
                        sin(i * 0.11f + zi * 3.1f) * h * 0.013f
                val y = (baseY + wobble).coerceIn(0f, h)
                if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            val tone = lerp(Color(0xFF3A3A3A), Color(0xFFE8F5E9), (ireVal / 110f).coerceIn(0f, 1f))
            drawPath(
                path,
                tone.copy(alpha = (0.45f + zone.weight.toFloat() * 1.6f).coerceAtMost(1f)),
                style = Stroke(width = (1.5f + zone.weight.toFloat() * 9f).dp.toPx())
            )
        }
    }
}

/** Luminance histogram of the demo scene, shifting with exposure. */
@Composable
fun HistogramView(exposureEv: Float, modifier: Modifier = Modifier) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF08080A))
    ) {
        val w = size.width
        val h = size.height
        val bins = 100
        val values = FloatArray(bins)
        val sigma = 2.6
        for (zone in demoScene) {
            val center = ExposureMath.ireAtStops(zone.stopsRelGrey, exposureEv.toDouble())
            for (b in 0 until bins) {
                val d = (b.toDouble() - center) / sigma
                values[b] += (zone.weight * exp(-0.5 * d * d)).toFloat()
            }
        }
        val maxV = (values.maxOrNull() ?: 1f).coerceAtLeast(0.001f)
        val bw = w / bins
        for (b in 0 until bins) {
            val bh = (values[b] / maxV) * h * 0.9f
            drawRect(
                FxAmber.copy(alpha = 0.9f),
                topLeft = Offset(b * bw, h - bh),
                size = Size(bw * 0.9f, bh)
            )
        }
        val dash = PathEffect.dashPathEffect(floatArrayOf(8f, 8f), 0f)
        fun marker(ire: Float, label: String) {
            val x = (ire / 100f) * w
            drawLine(Color.White.copy(alpha = 0.7f), Offset(x, 0f), Offset(x, h),
                strokeWidth = 1.5f, pathEffect = dash)
            drawIntoCanvas { canvas ->
                val paint = android.graphics.Paint().apply {
                    color = android.graphics.Color.WHITE
                    textSize = 11.sp.toPx()
                    isAntiAlias = true
                }
                canvas.nativeCanvas.drawText(label, (x + 5f).coerceAtMost(w - 60f), 18f, paint)
            }
        }
        marker(41f, "grey 41")
        marker(50f, "skin 50")
    }
}

/** Atomos-style false color reference chart. */
@Composable
fun FalseColorChart(bands: List<com.fx30companion.app.data.FalseColorBand>) {
    Column {
        bands.forEach { band ->
            Row(
                modifier = Modifier.padding(vertical = 4.dp),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier
                        .width(44.dp)
                        .height(30.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(band.color)
                )
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(band.name, style = MaterialTheme.typography.titleSmall)
                    Text(band.meaning, style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Text(band.ireRange, style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

/**
 * S-Log3 vs Rec.709 gamma curve comparison.
 * X axis: stops relative to 18% grey. Y axis: IRE %.
 */
@Composable
fun GammaCurvePlot(modifier: Modifier = Modifier) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF08080A))
    ) {
        val w = size.width
        val h = size.height
        val stopsMin = -8.0
        val stopsMax = 6.0
        val maxIre = 115.0
        fun xToPx(s: Double) = ((s - stopsMin) / (stopsMax - stopsMin) * w).toFloat()
        fun yToPx(ire: Double) = (h * (1 - ire / maxIre)).toFloat()

        // grid
        var s = stopsMin
        while (s <= stopsMax) {
            val x = xToPx(s)
            drawLine(Color.White.copy(alpha = 0.07f), Offset(x, 0f), Offset(x, h))
            s += 2.0
        }
        var ire = 0.0
        while (ire <= 100.0) {
            val y = yToPx(ire)
            drawLine(Color.White.copy(alpha = 0.07f), Offset(0f, y), Offset(w, y))
            ire += 20.0
        }

        // reference levels
        val dash = PathEffect.dashPathEffect(floatArrayOf(9f, 9f), 0f)
        val refs = listOf(
            41.0 to "grey 41", 50.0 to "skin 48–52", 61.0 to "white 61", 93.0 to "clip 93"
        )
        drawIntoCanvas { canvas ->
            val paint = android.graphics.Paint().apply {
                color = android.graphics.Color.argb(200, 255, 255, 255)
                textSize = 11.sp.toPx()
                isAntiAlias = true
            }
            refs.forEach { (level, label) ->
                val y = yToPx(level)
                drawLine(Color.White.copy(alpha = 0.35f), Offset(0f, y), Offset(w, y),
                    strokeWidth = 1f, pathEffect = dash)
                canvas.nativeCanvas.drawText(label, w - 92f, y - 5f, paint)
            }
            val paintAxis = android.graphics.Paint().apply {
                color = android.graphics.Color.argb(140, 255, 255, 255)
                textSize = 11.sp.toPx()
                isAntiAlias = true
            }
            canvas.nativeCanvas.drawText("18% grey", xToPx(0.0) + 6f, h - 8f, paintAxis)
        }
        // 0-stops vertical
        drawLine(Color.White.copy(alpha = 0.25f), Offset(xToPx(0.0), 0f), Offset(xToPx(0.0), h),
            strokeWidth = 1f, pathEffect = dash)

        // S-Log3 curve
        val logPath = Path()
        val n = 160
        for (i in 0..n) {
            val st = stopsMin + (stopsMax - stopsMin) * i / n
            val v = ExposureMath.ire(0.18 * 2.0.pow(st)).coerceAtMost(maxIre)
            val x = xToPx(st); val y = yToPx(v)
            if (i == 0) logPath.moveTo(x, y) else logPath.lineTo(x, y)
        }
        drawPath(logPath, FxOrange, style = Stroke(width = 3.dp.toPx()))

        // Rec.709 curve
        val recPath = Path()
        var started = false
        for (i in 0..n) {
            val st = stopsMin + (stopsMax - stopsMin) * i / n
            val v = (ExposureMath.rec709(0.18 * 2.0.pow(st)) * 100.0)
            if (v > maxIre) { started = false; continue }
            val x = xToPx(st); val y = yToPx(v)
            if (!started) { recPath.moveTo(x, y); started = true } else recPath.lineTo(x, y)
        }
        drawPath(recPath, FxBlue, style = Stroke(width = 3.dp.toPx()))

        // legend
        drawIntoCanvas { canvas ->
            val paint = android.graphics.Paint().apply {
                textSize = 12.sp.toPx(); isAntiAlias = true
            }
            paint.color = android.graphics.Color.rgb(255, 106, 0)
            canvas.nativeCanvas.drawText("— S-Log3", 12f, 24f, paint)
            paint.color = android.graphics.Color.rgb(64, 196, 255)
            canvas.nativeCanvas.drawText("— Rec.709", 12f, 44f, paint)
        }
    }
}
