package com.tacosforchessur.hardwire

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.tacosforchessur.hardwire.core.getDeviceInfo
import com.tacosforchessur.hardwire.core.logging.Logger
import com.tacosforchessur.hardwire.ui.components.DebugConsole
import com.tacosforchessur.hardwire.ui.components.MoveableFAB
import com.tacosforchessur.hardwire.ui.components.icons.VscodeCodiconsDebug
import com.tacosforchessur.hardwire.ui.screens.trainer.ChordChangerScreen
import com.tacosforchessur.hardwire.ui.screens.home.HomeScreen
import com.tacosforchessur.hardwire.ui.screens.library.ChordLibraryScreen
import com.tacosforchessur.hardwire.ui.screens.metronome.MetronomeScreen
import com.tacosforchessur.hardwire.viewmodel.ChordChangerViewModel
import com.tacosforchessur.hardwire.viewmodel.MetronomeViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    var showDebugConsole by remember { mutableStateOf(false) }
    val metronomeVm = remember { MetronomeViewModel() }
    val chordVm = remember { ChordChangerViewModel() }

    LaunchedEffect(Unit) {
        val info = getDeviceInfo()
        Logger.i("Device detected: $info")
    }

    MaterialTheme {
        val navController = rememberNavController()
        Box(modifier = Modifier.fillMaxSize()) {
            NavHost(
                navController = navController,
                startDestination = HomeRoute
            ) {
                composable<HomeRoute> {
                    HomeScreen(
                        onNavigateToMetronome = { bpm ->
                            navController.navigate(MetronomeRoute(bpm))
                        },
                        onNavigateToChordChanger = {
                            navController.navigate(route = ChordChangerRoute)
                        })
                }

                composable<MetronomeRoute> { backStackEntry ->
                    val route: MetronomeRoute = backStackEntry.toRoute()
                    MetronomeScreen(
                        viewModel = metronomeVm
                    )
                }

                composable<ChordChangerRoute> {
                    ChordChangerScreen(
                        metronomeVm = metronomeVm,
                        chordVm = chordVm,
                        onNavigateToMetronome = { bpm ->
                            navController.navigate(MetronomeRoute(bpm))
                        },
                        onNavigateToLibrary = {
                            navController.navigate(ChordLibraryScreen)
                        }
                    )
                }

                composable<ChordLibraryScreen> {
                    ChordLibraryScreen(
                        chordVm = chordVm,
                        onBack = { navController.popBackStack() }
                    )
                }
            }

            if (!showDebugConsole) {
                MoveableFAB(
                    onClick = { showDebugConsole = true},
                ) {
                    Icon(VscodeCodiconsDebug, contentDescription = "Open debug console")
                }
            }
            DebugConsole(
                isVisible = showDebugConsole,
                onDismissRequest = { showDebugConsole = false},
                modifier = Modifier.align(alignment = Alignment.BottomCenter)
                )
        }

    }
}