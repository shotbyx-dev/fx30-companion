package com.fx30companion.app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.SlowMotionVideo
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fx30companion.app.data.Scenario
import com.fx30companion.app.data.scenarios
import com.fx30companion.app.ui.components.SectionCard
import com.fx30companion.app.ui.components.SettingRow
import com.fx30companion.app.ui.theme.FxAmber
import com.fx30companion.app.ui.theme.FxGreen
import com.fx30companion.app.ui.theme.FxRed

fun scenarioIcon(id: String): ImageVector = when (id) {
    "cinematic" -> Icons.Filled.Movie
    "vlog" -> Icons.Filled.Videocam
    "lowlight" -> Icons.Filled.DarkMode
    "slowmo" -> Icons.Filled.SlowMotionVideo
    "interview" -> Icons.Filled.Mic
    else -> Icons.Filled.Landscape
}

@Composable
fun ScenariosScreen() {
    var selected by rememberSaveable { mutableStateOf<String?>(null) }
    val scenario = scenarios.firstOrNull { it.id == selected }

    if (scenario == null) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Text("Shooting scenarios",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 4.dp))
                Text("Tap a scenario for the full recipe — dial these in and shoot.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 4.dp))
            }
            items(scenarios) { s ->
                Card(
                    modifier = Modifier.fillMaxWidth().clickable { selected = s.id },
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(scenarioIcon(s.id), contentDescription = null, tint = FxAmber,
                            modifier = Modifier.padding(end = 14.dp))
                        Column {
                            Text(s.title, style = MaterialTheme.typography.titleMedium)
                            Text(s.subtitle, style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("${s.resolution} · ${s.codec.substringBefore(" (")}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(top = 4.dp))
                        }
                    }
                }
            }
        }
    } else {
        ScenarioDetail(scenario = scenario, onBack = { selected = null })
    }
}

@Composable
private fun ScenarioDetail(scenario: Scenario, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
            }
            Icon(scenarioIcon(scenario.id), contentDescription = null, tint = FxAmber,
                modifier = Modifier.padding(end = 10.dp))
            Column {
                Text(scenario.title, style = MaterialTheme.typography.headlineSmall)
                Text(scenario.subtitle, style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Spacer(Modifier.height(8.dp))
        val scroll = rememberScrollState()
        Column(
            modifier = Modifier.verticalScroll(scroll),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
                SectionCard(title = "Camera setup") {
                    SettingRow("Mode", scenario.mode)
                    SettingRow("Codec", scenario.codec)
                    SettingRow("Resolution", scenario.resolution)
                    SettingRow("Shutter", scenario.shutter)
                    SettingRow("Aperture", scenario.aperture)
                    SettingRow("ISO", scenario.iso)
                    SettingRow("White balance", scenario.whiteBalance)
                    SettingRow("Picture profile", scenario.pictureProfile)
                    SettingRow("Audio", scenario.audio)
                    SettingRow("Stabilization", scenario.stabilization)
                }
                SectionCard(title = "Tips") {
                    scenario.tips.forEach { tip ->
                        Row(Modifier.padding(vertical = 4.dp)) {
                            Icon(Icons.Filled.CheckCircle, contentDescription = null,
                                tint = FxGreen, modifier = Modifier.padding(end = 8.dp))
                            Text(tip, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
                if (scenario.warnings.isNotEmpty()) {
                    SectionCard(title = "Watch out") {
                        scenario.warnings.forEach { w ->
                            Row(Modifier.padding(vertical = 4.dp)) {
                                Icon(Icons.Filled.Warning, contentDescription = null,
                                    tint = FxRed, modifier = Modifier.padding(end = 8.dp))
                                Text(w, style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                }
            }
    }
}
