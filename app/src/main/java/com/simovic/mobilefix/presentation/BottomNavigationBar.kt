package com.simovic.mobilefix.presentation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.simovic.mobilefix.app.R
import timber.log.Timber

@Composable
fun BottomNavigationBar(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val navigationItems = getBottomNavigationItems()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    Timber.d("::DEVELOP current route is: $currentRoute")

    val selectedNavigationIndex =
        navigationItems
            .indexOfFirst {
                Timber.d("::DEVELOP:: Comparing with ${it.route}")
                currentRoute?.startsWith(it.route) == true
            }.takeIf { it >= 0 } ?: 0

    NavigationBar(
        modifier = modifier,
    ) {
        navigationItems.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedNavigationIndex == index,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(0)
                        restoreState = true // Restores previous state if returning
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(item.iconRes),
                        contentDescription = stringResource(item.titleRes),
                    )
                },
                label = {
                    Text(
                        stringResource(item.titleRes),
                    )
                },
                colors =
                    NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.surface,
                        indicatorColor = MaterialTheme.colorScheme.primary,
                    ),
            )
        }
    }
}

private fun getBottomNavigationItems() =
    listOf(
        NavigationBarItem(
            R.string.bottom_navigation_albums,
            R.drawable.ic_music_library,
            AlbumDestinations.ROOT,
        ),
        NavigationBarItem(
            R.string.bottom_navigation_favorites,
            R.drawable.ic_favorite,
            FavouriteDestinations.ROOT,
        ),
        NavigationBarItem(
            R.string.bottom_navigation_settings,
            R.drawable.ic_settings,
            SettingsDestinations.ROOT,
        ),
    )

data class NavigationBarItem(
    @StringRes val titleRes: Int,
    @DrawableRes val iconRes: Int,
    val route: String,
)

@Preview
@Composable
private fun BottomNavigationBarPreview() {
    BottomNavigationBar(
        navController = rememberNavController(),
    )
}
