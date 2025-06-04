package it.fast4x.rimusic.ui.screens.home

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.lifecycle.Lifecycle
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavController
import app.kreate.android.R
import app.kreate.android.Settings
import app.kreate.android.themed.rimusic.screen.home.HomeSongsScreen
import it.fast4x.compose.persist.PersistMapCleanup
import it.fast4x.rimusic.enums.HomeScreenTabs
import it.fast4x.rimusic.enums.NavRoutes
import it.fast4x.rimusic.models.toUiMood
import it.fast4x.rimusic.ui.components.Skeleton
import it.fast4x.rimusic.utils.preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.knighthat.utils.Toaster
import kotlin.system.exitProcess


@ExperimentalMaterial3Api
@ExperimentalTextApi
@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@UnstableApi
@Composable
fun HomeScreen(
    navController: NavController,
    onPlaylistUrl: (String) -> Unit,
    miniPlayer: @Composable () -> Unit = {},
    openTabFromShortcut: Int
) {
    val saveableStateHolder = rememberSaveableStateHolder()

    val preferences = LocalContext.current.preferences
    //val showSearchTab by rememberPreference(showSearchTabKey, false)
    //val showStatsInNavbar by rememberPreference(showStatsInNavbarKey, false)
    val enableQuickPicksPage by Settings.QUICK_PICKS_PAGE

    PersistMapCleanup("home/")

    val openTabFromShortcut1 by remember{ mutableIntStateOf(openTabFromShortcut) }

    val startupScreen by Settings.STARTUP_SCREEN
    var initialtabIndex =
        when (openTabFromShortcut1) {
            -1 -> when (startupScreen) {
                HomeScreenTabs.Default -> HomeScreenTabs.QuickPics.index
                else -> startupScreen.index
            }
            else -> openTabFromShortcut1
        }

    var (tabIndex, onTabChanged) = Settings.HOME_TAB_INDEX

    if (tabIndex == -2) navController.navigate(NavRoutes.search.name)


    if (!enableQuickPicksPage && tabIndex==0) tabIndex = 1

    Skeleton(
        navController,
        tabIndex,
        onTabChanged,
        miniPlayer,
        navBarContent = { Item ->
            if (enableQuickPicksPage)
                Item(0, stringResource(R.string.quick_picks), R.drawable.sparkles)
            Item(1, stringResource(R.string.songs), R.drawable.musical_notes)
            Item(2, stringResource(R.string.artists), R.drawable.people)
            Item(3, stringResource(R.string.albums), R.drawable.album)
            Item(4, stringResource(R.string.playlists), R.drawable.library)
        }
    ) { currentTabIndex ->
        saveableStateHolder.SaveableStateProvider(key = currentTabIndex) {
            when (currentTabIndex) {
                0 -> HomeQuickPicks(
                    onAlbumClick = {
                        navController.navigate(route = "${NavRoutes.album.name}/$it")
                    },
                    onArtistClick = {
                        navController.navigate(route = "${NavRoutes.artist.name}/$it")
                    },
                    onPlaylistClick = {
                        navController.navigate(route = "${NavRoutes.playlist.name}/$it")
                    },
                    onSearchClick = {
                        navController.navigate(NavRoutes.search.name)
                    },
                    onMoodClick = { mood ->
                        navController.currentBackStackEntry?.savedStateHandle?.set("mood", mood.toUiMood())
                        navController.navigate(NavRoutes.mood.name)
                    },
                    onSettingsClick = {
                        navController.navigate(NavRoutes.settings.name)
                    },
                    navController = navController

                )

                1 -> HomeSongsScreen( navController )

                2 -> HomeArtists(
                    onArtistClick = {
                        navController.navigate(route = "${NavRoutes.artist.name}/${it.id}")
                    },
                    onSearchClick = {
                        //searchRoute("")
                        navController.navigate(NavRoutes.search.name)
                    },
                    onSettingsClick = {
                        //settingsRoute()
                        navController.navigate(NavRoutes.settings.name)
                    }
                )

                3 -> HomeAlbums(
                    navController = navController,
                    onAlbumClick = {
                        //albumRoute(it.id)
                        navController.navigate(route = "${NavRoutes.album.name}/${it.id}")
                    },
                    onSearchClick = {
                        //searchRoute("")
                        navController.navigate(NavRoutes.search.name)
                    },
                    onSettingsClick = {
                        //settingsRoute()
                        navController.navigate(NavRoutes.settings.name)
                    }
                )

                4 -> HomeLibrary(
                    onPlaylistClick = {
                        //localPlaylistRoute(it.id)
                        navController.navigate(route = "${NavRoutes.localPlaylist.name}/${it.id}")
                    },
                    onSearchClick = {
                        //searchRoute("")
                        navController.navigate(NavRoutes.search.name)
                    },
                    onSettingsClick = {
                        //settingsRoute()
                        navController.navigate(NavRoutes.settings.name)
                    }

                )
            }
        }
    }

    // Exit app when user uses back
    val context = LocalContext.current
    var confirmCount by remember { mutableIntStateOf( 0 ) }
    BackHandler {
        // Prevent this from being applied when user is not on HomeScreen
        if( NavRoutes.home.isNotHere( navController ) )  {
            if ( navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED )
                navController.popBackStack()

            return@BackHandler
        }

        if( confirmCount == 0 ) {
            Toaster.i( R.string.press_once_again_to_exit )
            confirmCount++

            // Reset confirmCount after 5s
            CoroutineScope( Dispatchers.Default ).launch {
                delay( 5000L )
                confirmCount = 0
            }
        } else {
            val activity = context as? Activity
            activity?.finishAffinity()
            // Close app with exit 0 notify that no problem occurred
            exitProcess( 0 )
        }
    }
}
