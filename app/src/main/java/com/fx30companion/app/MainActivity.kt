package com.fx30companion.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.fx30companion.app.ui.screens.CurvesScreen
import com.fx30companion.app.ui.screens.ExposureScreen
import com.fx30companion.app.ui.screens.HomeScreen
import com.fx30companion.app.ui.screens.ReferenceScreen
import com.fx30companion.app.ui.screens.ScenariosScreen
import com.fx30companion.app.ui.theme.Fx30CompanionTheme

enum class Tab(val title: String, val icon: ImageVector) {
    Home("Home", Icons.Filled.Home),
    Scenarios("Scenarios", Icons.Filled.Movie),
    Exposure("Exposure Lab", Icons.Filled.Tune),
    Curves("Curves", Icons.Filled.ShowChart),
    Reference("Reference", Icons.Filled.MenuBook)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Fx30CompanionTheme {
                Fx30App()
            }
        }
    }
}

@Composable
fun Fx30App() {
    var tabIndex by rememberSaveable { mutableIntStateOf(0) }
    val tab = Tab.entries[tabIndex]

    Scaffold(
        bottomBar = {
            NavigationBar {
                Tab.entries.forEachIndexed { index, t ->
                    NavigationBarItem(
                        selected = tabIndex == index,
                        onClick = { tabIndex = index },
                        icon = { Icon(t.icon, contentDescription = t.title) },
                        label = { Text(t.title) }
                    )
                }
            }
        }
    ) { innerPadding ->
        androidx.compose.foundation.layout.Box(
            modifier = Modifier.padding(innerPadding)
        ) {
            when (tab) {
                Tab.Home -> HomeScreen(
                    onOpenScenarios = { tabIndex = Tab.Scenarios.ordinal },
                    onOpenExposure = { tabIndex = Tab.Exposure.ordinal },
                    onOpenCurves = { tabIndex = Tab.Curves.ordinal }
                )
                Tab.Scenarios -> ScenariosScreen()
                Tab.Exposure -> ExposureScreen()
                Tab.Curves -> CurvesScreen()
                Tab.Reference -> ReferenceScreen()
            }
        }
    }
}
