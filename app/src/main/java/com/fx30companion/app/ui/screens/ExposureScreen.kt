package com.fx30companion.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fx30companion.app.data.ExposureMath
import com.fx30companion.app.data.falseColorBands
import com.fx30companion.app.data.shutterPresets
import com.fx30companion.app.data.zebraSetup
import com.fx30companion.app.ui.components.FalseColorChart
import com.fx30companion.app.ui.components.HistogramView
import com.fx30companion.app.ui.components.SectionCard
import com.fx30companion.app.ui.components.WaveformMonitor
import com.fx30companion.app.ui.theme.FxAmber
import com.fx30companion.app.ui.theme.FxGreen
import com.fx30companion.app.ui.theme.FxRed

private data class Verdict(val title: String, val detail: String, val color: Color)

private fun verdictFor(ev: Float): Verdict {
    val skin = ExposureMath.ireAtStops(1.2, ev.toDouble())
    val top = ExposureMath.ireAtStops(4.5, ev.toDouble())
    return when {
        top >= 99.0 -> Verdict(
            "Clipping highlights",
            "Specular highlights are blowing out at %.0f IRE. Pull exposure down until zebras at 100+ disappear from anything important.".format(top),
            FxRed
        )
        skin in 48.0..58.0 -> Verdict(
            "Great exposure",
            "Skin sits at %.0f IRE — inside the 48–52 zone. This is what correct S-Log3 looks like on a waveform.".format(skin),
            FxGreen
        )
        ev > 0f -> Verdict(
            "Bright ETTR",
            "Skin at %.0f IRE. Bright log grades clean — just keep important highlights under 93 IRE.".format(skin),
            FxAmber
        )
        else -> Verdict(
            "Underexposed",
            "Skin at %.0f IRE. Shadows will turn noisy when you lift them in the grade — open up or add light.".format(skin),
            FxRed
        )
    }
}

@Composable
private fun LegendSwatch(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier.size(14.dp).clip(RoundedCornerShape(4.dp)).background(color)
        )
        Spacer(Modifier.width(6.dp))
        Text(label, style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun ExposureScreen() {
    var ev by rememberSaveable { mutableFloatStateOf(0f) }
    val verdict = verdictFor(ev)
    val evLabel = (if (ev >= 0) "+" else "") + "%.1f EV".format(ev)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Exposure Lab", style = MaterialTheme.typography.headlineSmall)
        Text(
            "Drag the exposure slider and watch the scopes. Learn what correct S-Log3 exposure looks like before you trust it on a job.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Card(colors = CardDefaults.cardColors(containerColor = verdict.color.copy(alpha = 0.14f))) {
            Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(12.dp).clip(RoundedCornerShape(6.dp))
                        .background(verdict.color)
                )
                Spacer(Modifier.width(10.dp))
                Column {
                    Text(verdict.title, style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold, color = verdict.color)
                    Text(verdict.detail, style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 2.dp))
                }
            }
        }

        SectionCard(title = "Exposure", subtitle = "Simulated S-Log3 scene") {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Exposure compensation", style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f))
                Text(evLabel, style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold, color = FxAmber)
            }
            Slider(
                value = ev,
                onValueChange = { ev = it },
                valueRange = -3f..3f,
                steps = 59
            )
            Row {
                TextButton(onClick = { ev = 0f }) { Text("0 EV") }
                TextButton(onClick = { ev = 1.7f }) { Text("ETTR +1.7") }
                TextButton(onClick = { ev = -1.5f }) { Text("−1.5 EV") }
            }
        }

        SectionCard(
            title = "Waveform monitor",
            subtitle = "How the graph is supposed to look"
        ) {
            WaveformMonitor(exposureEv = ev)
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                LegendSwatch(FxGreen.copy(alpha = 0.5f), "Skin 48–52%")
                LegendSwatch(Color.White.copy(alpha = 0.6f), "Grey 41% · White 61%")
                LegendSwatch(FxRed.copy(alpha = 0.5f), "Clip 93%+")
            }
            Text(
                "Skin trace in the green band, grey-card trace on the 41% line, nothing touching the red. " +
                        "That is a correct S-Log3 exposure.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        SectionCard(
            title = "Histogram",
            subtitle = "Same scene, same exposure"
        ) {
            HistogramView(exposureEv = ev)
            Text(
                "In S-Log3 the histogram sits left of center even when exposure is perfect — " +
                        "that is normal. Judge with zebras and IRE targets, not the hump.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        SectionCard(
            title = "Zebra setup",
            subtitle = "The FX30's best in-camera exposure tool"
        ) {
            zebraSetup.forEach { z ->
                Column(Modifier.padding(vertical = 6.dp)) {
                    Text(z.zebra, style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold)
                    Text(z.level, style = MaterialTheme.typography.bodyMedium, color = FxAmber,
                        fontWeight = FontWeight.Medium)
                    Text(z.purpose, style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        SectionCard(
            title = "False color chart",
            subtitle = "Atomos-style mapping · assumes a Rec.709 monitoring signal"
        ) {
            FalseColorChart(bands = falseColorBands)
            Text(
                "When shooting S-Log3, apply a monitoring LUT first — otherwise middle grey " +
                        "reads at 41% IRE, not 50%, and the colors will mislead you.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        SectionCard(
            title = "180° shutter rule",
            subtitle = "Shutter speed = 1 / (2 × frame rate)"
        ) {
            shutterPresets.forEach { p ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(p.fpsLabel, style = MaterialTheme.typography.bodyMedium)
                    Text(p.shutterSpeed, style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold, color = FxAmber)
                }
            }
            Text(
                "Firmware v5.00+ adds a true shutter-angle mode: MENU → Exposure/Color → Exposure → Shutter Mode → Angle.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 6.dp)
            )
        }
    }
}
