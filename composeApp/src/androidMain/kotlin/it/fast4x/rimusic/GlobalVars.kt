package it.fast4x.rimusic

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import app.kreate.android.Settings
import it.fast4x.rimusic.ui.styling.LocalAppearance

@Composable
fun typography() = LocalAppearance.current.typography

@Composable
@ReadOnlyComposable
fun colorPalette() = LocalAppearance.current.colorPalette

@Composable
fun thumbnailShape() = LocalAppearance.current.thumbnailShape

@Composable
fun showSearchIconInNav() = Settings.SHOW_SEARCH_IN_NAVIGATION_BAR.value

@Composable
fun showStatsIconInNav() = Settings.SHOW_STATS_IN_NAVIGATION_BAR.value

@Composable
fun binder() = LocalPlayerServiceBinder.current?.service

fun appContext(): Context = Dependencies.application.applicationContext
fun context(): Context = Dependencies.application

fun ytAccountName() = Settings.YOUTUBE_ACCOUNT_NAME.value
fun ytAccountThumbnail() = Settings.YOUTUBE_ACCOUNT_AVATAR.value
fun isVideoEnabled() = Settings.PLAYER_ACTION_TOGGLE_VIDEO.value

fun isConnectionMeteredEnabled() = Settings.IS_CONNECTION_METERED.value
fun isAutoSyncEnabled() = Settings.AUTO_SYNC.value
fun isHandleAudioFocusEnabled() = Settings.AUDIO_SMART_PAUSE_DURING_CALLS.value
fun isBassBoostEnabled() = Settings.AUDIO_BASS_BOOSTED.value
fun isDebugModeEnabled() = Settings.DEBUG_LOG.value