package com.fx30companion.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fx30companion.app.data.slog3Targets
import com.fx30companion.app.ui.components.GammaCurvePlot
import com.fx30companion.app.ui.components.SectionCard
import com.fx30companion.app.ui.theme.FxAmber
import com.fx30companion.app.ui.theme.FxBlue
import com.fx30companion.app.ui.theme.FxOrange

@Composable
fun CurvesScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Gamma curves", style = MaterialTheme.typography.headlineSmall)
        Text(
            "Why log looks flat — and where everything should sit on the curve.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        SectionCard(
            title = "S-Log3 vs Rec.709",
            subtitle = "X: stops relative to 18% grey · Y: IRE %"
        ) {
            GammaCurvePlot()
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("S-Log3", color = FxOrange, style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold)
                Text("Rec.709", color = FxBlue, style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold)
            }
            Text(
                "Both curves agree at 18% grey (~41% IRE) — then they split. Rec.709 rushes to white; " +
                        "S-Log3 flattens out to squeeze 14+ stops into the file. That flat top is your highlight " +
                        "headroom, and it is why log looks washed out before the grade.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        SectionCard(
            title = "IRE targets for S-Log3",
            subtitle = "Memorize these four numbers"
        ) {
            slog3Targets.forEach { t ->
                Column(Modifier.padding(vertical = 6.dp)) {
                    Row {
                        Text(t.label, style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                        Text(
                            if (t.bandTop != null) "%.0f–%.0f%%".format(t.ire, t.bandTop)
                            else "%.0f%%".format(t.ire),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold, color = FxAmber
                        )
                    }
                    Text(t.note, style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        SectionCard(
            title = "ETTR: expose to the right",
            subtitle = "Sony's recommended method for clean log footage"
        ) {
            Text(
                "Log stores far more data in bright tones than in shadows. Overexpose by +1.7 to +2.0 stops " +
                        "on the in-camera meter — without clipping anything important — and the image grades clean. " +
                        "Underexpose and the shadows fall apart into noise when you lift them.\n\n" +
                        "Practical method: point at a grey card, set a narrow zebra to 41%, and open up until it " +
                        "just stripes. Then protect highlights with Zebra 1 at 100+.",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
