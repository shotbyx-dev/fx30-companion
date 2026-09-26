package com.fx30companion.app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fx30companion.app.data.commonMistakes
import com.fx30companion.app.data.keySpecs
import com.fx30companion.app.data.pictureProfiles
import com.fx30companion.app.ui.components.SectionCard
import com.fx30companion.app.ui.components.SettingRow
import com.fx30companion.app.ui.theme.FxAmber
import com.fx30companion.app.ui.theme.FxGreen

@Composable
fun ReferenceScreen() {
    var checked by rememberSaveable { mutableStateOf(List(commonMistakes.size) { false }) }
    val done = checked.count { it }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Reference", style = MaterialTheme.typography.headlineSmall)
            Text(
                "Picture profiles, the 14 classic mistakes, and key specs.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        item {
            SectionCard(
                title = "Picture Profiles",
                subtitle = "FX30 defaults — PP is forced Off when a Log Shooting mode is active"
            ) {
                pictureProfiles.forEach { pp ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (pp.highlight)
                                FxAmber.copy(alpha = 0.10f)
                            else MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Row(Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text(pp.id, style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold, color = FxAmber,
                                modifier = Modifier.padding(end = 10.dp))
                            Column {
                                Text("${pp.gamma} · ${pp.colorMode}",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Medium)
                                Text(pp.useFor, style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }
        }

        item {
            SectionCard(
                title = "Mistakes checklist",
                subtitle = "$done of ${commonMistakes.size} reviewed — tap to check off"
            ) {
                commonMistakes.forEachIndexed { i, m ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                checked = checked.toMutableList().also { it[i] = !it[i] }
                            }
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            if (checked[i]) Icons.Filled.CheckCircle else Icons.Filled.RadioButtonUnchecked,
                            contentDescription = null,
                            tint = if (checked[i]) FxGreen else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(end = 10.dp, top = 2.dp)
                        )
                        Column {
                            Text(
                                m.title,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                color = if (checked[i]) MaterialTheme.colorScheme.onSurfaceVariant
                                else MaterialTheme.colorScheme.onSurface
                            )
                            Text(m.fix, style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }

        item {
            SectionCard(title = "Key specs", subtitle = "Sony FX30 (ILME-FX30)") {
                keySpecs.forEach { s -> SettingRow(s.label, s.value) }
            }
        }
    }
}
