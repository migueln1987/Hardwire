package com.tacosforchessur.hardwire

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.tacosforchessur.hardwire.ui.ChordChangerScreen
import com.tacosforchessur.hardwire.ui.HomeScreen
import com.tacosforchessur.hardwire.ui.MetronomeScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = HomeRoute
        ) {
            composable<HomeRoute> {
                HomeScreen(onNavigateToMetronome = { bpm ->
                    navController.navigate(MetronomeRoute(bpm))
                },
                    onNavigateToChordChanger = {
                        navController.navigate(route = ChordChangerRoute)
                    })
            }

            composable<MetronomeRoute> { backStackEntry ->
                val route: MetronomeRoute = backStackEntry.toRoute()
                MetronomeScreen(
                    initialBpm = route.initialBpm
                )
            }

            composable<ChordChangerRoute> {
                ChordChangerScreen()
            }
        }
    }
}