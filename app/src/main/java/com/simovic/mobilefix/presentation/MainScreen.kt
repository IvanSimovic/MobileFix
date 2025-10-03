package com.simovic.mobilefix.presentation

import android.os.Bundle
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import androidx.navigation.navArgument
import com.simovic.mobilefix.app.BuildConfig
import com.simovic.mobilefix.feature.album.presentation.screen.albumdetail.AlbumDetailScreen
import com.simovic.mobilefix.feature.album.presentation.screen.albumlist.AlbumListScreen
import com.simovic.mobilefix.feature.favourite.presentation.screen.favourite.FavouriteScreen
import com.simovic.mobilefix.feature.settings.presentation.screen.aboutlibraries.AboutLibrariesScreen
import com.simovic.mobilefix.feature.settings.presentation.screen.settings.SettingsScreen
import com.simovic.mobilefix.presentation.util.NavigationDestinationLogger

@Composable
fun MainShowcaseScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val actions = remember(navController) { MainActions(navController) }

    if (BuildConfig.DEBUG) {
        addOnDestinationChangedListener(navController)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = { BottomNavigationBar(navController) },
    ) { innerPadding ->

        val graph =
            navController.createGraph(startDestination = AlbumDestinations.ROOT) {
                composable(AlbumDestinations.ROOT) {
                    AlbumListScreen(
                        // artistName: String, albumName: String, mbId: String?
                        onNavigateToAlbumDetail = { artistName, albumName, albumMbId ->
                            actions.toAlbumDetails(artistName, albumName, albumMbId)
                        },
                    )
                }
                composable(
                    route = "${AlbumDestinations.ALBUM_DETAILS}/{artistName}/{albumName}/{albumMbId}",
                    arguments = listOf(
                        navArgument("artistName") { type = NavType.StringType },
                        navArgument("albumName") { type = NavType.StringType },
                        navArgument("albumMbId") {
                            type = NavType.StringType
                            nullable = true
                            defaultValue = null
                        }
                    ),
                ) { backStackEntry ->
                    val args = backStackEntry.arguments

                    AlbumDetailScreen(
                        albumName = args?.getString("albumName") ?: "",
                        artistName = args?.getString("artistName") ?: "",
                        albumMbId = args?.getString("albumMbId") ?: "",
                        onBackClick = {
                            actions.navigateUp()
                        },
                    )
                }

                composable(FavouriteDestinations.ROOT) {
                    FavouriteScreen()
                }

                composable(SettingsDestinations.ROOT) {
                    SettingsScreen(
                        onNavigateToAboutLibraries = {
                            actions.toAboutLibraries()
                        },
                    )
                }

                composable(SettingsDestinations.ABOUT_LIBRARY) {
                    AboutLibrariesScreen(
                        onBackClick = {
                            actions.navigateUp()
                        },
                    )
                }

            }
        NavHost(
            navController = navController,
            graph = graph,
            modifier = Modifier.padding(innerPadding),
        )
    }
}

private fun addOnDestinationChangedListener(navController: NavController) {
    navController.addOnDestinationChangedListener(
        object : NavController.OnDestinationChangedListener {
            override fun onDestinationChanged(
                controller: NavController,
                destination: NavDestination,
                arguments: Bundle?,
            ) {
                NavigationDestinationLogger.logDestinationChange(destination, arguments)
            }
        },
    )
}

object AlbumDestinations {
    const val ROOT = "album"
    const val ALBUM_DETAILS = "${ROOT}_details"
}

object FavouriteDestinations {
    const val ROOT = "favourite"
}

object SettingsDestinations {
    const val ROOT = "settings"
    const val ABOUT_LIBRARY = "${ROOT}_about_library"
}

class MainActions(navController: NavHostController) {

    val toAlbumDetails: (String, String, String?) -> Unit = { artistName, albumName, albumMbId ->
        navController.navigate(
            "${AlbumDestinations.ALBUM_DETAILS}/$artistName/$albumName/$albumMbId"
        )
    }

    val navigateUp: () -> Unit = {
        navController.navigateUp()
    }

    val toAboutLibraries: () -> Unit = {
        navController.navigate(SettingsDestinations.ABOUT_LIBRARY)
    }
}
