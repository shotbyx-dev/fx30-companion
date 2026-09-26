package com.fx30companion.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fx30companion.app.ui.components.SectionCard
import com.fx30companion.app.ui.theme.FxAmber

@Composable
fun HomeScreen(
    onOpenScenarios: () -> Unit,
    onOpenExposure: () -> Unit,
    onOpenCurves: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(20.dp)) {
                Text(
                    "FX30 Companion",
                    style = MaterialTheme.typography.headlineSmall,
                    color = androidx.compose.ui.graphics.Color.Black
                )
                Text(
                    "Best settings for every shoot — and what your exposure scopes should look like.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.8f),
                    modifier = Modifier.padding(top = 6.dp)
                )
                Spacer(Modifier.height(12.dp))
                Button(onClick = onOpenScenarios) {
                    Icon(Icons.Filled.PlayArrow, contentDescription = null)
                    Text("Start with a scenario", modifier = Modifier.padding(start = 4.dp))
                }
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(Modifier.padding(14.dp)) {
                    Text("800 / 2500", style = MaterialTheme.typography.titleMedium, color = FxAmber)
                    Text("Dual base ISO for S-Log3", style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(Modifier.padding(14.dp)) {
                    Text("14+ stops", style = MaterialTheme.typography.titleMedium, color = FxAmber)
                    Text("Dynamic range in S-Log3", style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(Modifier.padding(14.dp)) {
                    Text("10-bit", style = MaterialTheme.typography.titleMedium, color = FxAmber)
                    Text("4:2:2 internal to 120p", style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        SectionCard(
            title = "No waveform in the camera?",
            subtitle = "The FX30 has no built-in waveform monitor or false color — that is exactly why this app exists."
        ) {
            Text(
                "Use zebras (100+ for clipping, 70 for skin) and the histogram in-camera. " +
                        "For real scopes, use the free Sony Monitor & Control app or an external monitor. " +
                        "The Exposure Lab tab shows you what a correct waveform looks like for S-Log3, so you can " +
                        "recognize it anywhere.",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(10.dp))
            OutlinedButton(onClick = onOpenExposure, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Filled.Tune, contentDescription = null)
                Text("Open the Exposure Lab", modifier = Modifier.padding(start = 6.dp))
            }
        }

        SectionCard(
            title = "Why S-Log3 looks flat",
            subtitle = "The log curve in one picture"
        ) {
            Text(
                "S-Log3 squeezes 14+ stops of light into the recording by going flat in the highlights — " +
                        "18% grey sits at just 41% IRE and 90% white at 61%. It looks wrong on the LCD and " +
                        "perfect after the grade.",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(10.dp))
            OutlinedButton(onClick = onOpenCurves, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Filled.ShowChart, contentDescription = null)
                Text("See the gamma curve", modifier = Modifier.padding(start = 6.dp))
            }
        }

        Row(verticalAlignment = Alignment.Top) {
            Icon(Icons.Filled.Info, contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(
                "Field guide, not a manual: settings distilled from Sony's Help Guide and working cinematographers. " +
                        "Always verify critical shoots on your own scopes.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        Text(
            "Made for FX30 shooters learning exposure.",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium
        )
    }
}
