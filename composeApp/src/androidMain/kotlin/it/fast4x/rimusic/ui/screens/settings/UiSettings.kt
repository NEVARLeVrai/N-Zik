package it.fast4x.rimusic.ui.screens.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavController
import app.kreate.android.R
import app.kreate.android.Settings
import it.fast4x.rimusic.LocalPlayerServiceBinder
import it.fast4x.rimusic.colorPalette
import it.fast4x.rimusic.enums.AlbumSwipeAction
import it.fast4x.rimusic.enums.BackgroundProgress
import it.fast4x.rimusic.enums.CarouselSize
import it.fast4x.rimusic.enums.ColorPaletteMode
import it.fast4x.rimusic.enums.ColorPaletteName
import it.fast4x.rimusic.enums.DurationInMilliseconds
import it.fast4x.rimusic.enums.DurationInMinutes
import it.fast4x.rimusic.enums.ExoPlayerMinTimeForEvent
import it.fast4x.rimusic.enums.FontType
import it.fast4x.rimusic.enums.HomeScreenTabs
import it.fast4x.rimusic.enums.IconLikeType
import it.fast4x.rimusic.enums.MaxSongs
import it.fast4x.rimusic.enums.MaxStatisticsItems
import it.fast4x.rimusic.enums.MaxTopPlaylistItems
import it.fast4x.rimusic.enums.MenuStyle
import it.fast4x.rimusic.enums.MiniPlayerType
import it.fast4x.rimusic.enums.NavigationBarPosition
import it.fast4x.rimusic.enums.NavigationBarType
import it.fast4x.rimusic.enums.PauseBetweenSongs
import it.fast4x.rimusic.enums.PlayerBackgroundColors
import it.fast4x.rimusic.enums.PlayerControlsType
import it.fast4x.rimusic.enums.PlayerInfoType
import it.fast4x.rimusic.enums.PlayerPlayButtonType
import it.fast4x.rimusic.enums.PlayerPosition
import it.fast4x.rimusic.enums.PlayerThumbnailSize
import it.fast4x.rimusic.enums.PlayerTimelineSize
import it.fast4x.rimusic.enums.PlayerTimelineType
import it.fast4x.rimusic.enums.PlayerType
import it.fast4x.rimusic.enums.PlaylistSwipeAction
import it.fast4x.rimusic.enums.QueueSwipeAction
import it.fast4x.rimusic.enums.QueueType
import it.fast4x.rimusic.enums.RecommendationsNumber
import it.fast4x.rimusic.enums.ThumbnailRoundness
import it.fast4x.rimusic.enums.ThumbnailType
import it.fast4x.rimusic.enums.TransitionEffect
import it.fast4x.rimusic.enums.UiType
import it.fast4x.rimusic.ui.components.themed.ConfirmationDialog
import it.fast4x.rimusic.ui.components.themed.HeaderWithIcon
import it.fast4x.rimusic.ui.styling.DefaultDarkColorPalette
import it.fast4x.rimusic.ui.styling.DefaultLightColorPalette
import it.fast4x.rimusic.ui.styling.Dimensions
import it.fast4x.rimusic.utils.customColorKey
import it.fast4x.rimusic.utils.customThemeDark_Background0Key
import it.fast4x.rimusic.utils.customThemeDark_Background1Key
import it.fast4x.rimusic.utils.customThemeDark_Background2Key
import it.fast4x.rimusic.utils.customThemeDark_Background3Key
import it.fast4x.rimusic.utils.customThemeDark_Background4Key
import it.fast4x.rimusic.utils.customThemeDark_TextKey
import it.fast4x.rimusic.utils.customThemeDark_accentKey
import it.fast4x.rimusic.utils.customThemeDark_iconButtonPlayerKey
import it.fast4x.rimusic.utils.customThemeDark_textDisabledKey
import it.fast4x.rimusic.utils.customThemeDark_textSecondaryKey
import it.fast4x.rimusic.utils.customThemeLight_Background0Key
import it.fast4x.rimusic.utils.customThemeLight_Background1Key
import it.fast4x.rimusic.utils.customThemeLight_Background2Key
import it.fast4x.rimusic.utils.customThemeLight_Background3Key
import it.fast4x.rimusic.utils.customThemeLight_Background4Key
import it.fast4x.rimusic.utils.customThemeLight_TextKey
import it.fast4x.rimusic.utils.customThemeLight_accentKey
import it.fast4x.rimusic.utils.customThemeLight_iconButtonPlayerKey
import it.fast4x.rimusic.utils.customThemeLight_textDisabledKey
import it.fast4x.rimusic.utils.customThemeLight_textSecondaryKey
import it.fast4x.rimusic.utils.minimumSilenceDurationKey
import it.fast4x.rimusic.utils.rememberPreference
import me.knighthat.component.dialog.RestartAppDialog
import me.knighthat.component.tab.Search
import me.knighthat.utils.Toaster

@Composable
fun DefaultUiSettings() {
    var exoPlayerMinTimeForEvent by Settings.QUICK_PICKS_MIN_DURATION
    exoPlayerMinTimeForEvent = ExoPlayerMinTimeForEvent.`20s`
    var persistentQueue by Settings.ENABLE_PERSISTENT_QUEUE
    persistentQueue = false
    var resumePlaybackOnStart by Settings.RESUME_PLAYBACK_ON_STARTUP
    resumePlaybackOnStart = false
    var closebackgroundPlayer by Settings.CLOSE_BACKGROUND_JOB_IN_TASK_MANAGER
    closebackgroundPlayer = false
    var closeWithBackButton by Settings.CLOSE_APP_ON_BACK
    closeWithBackButton = true
    var resumePlaybackWhenDeviceConnected by Settings.RESUME_PLAYBACK_WHEN_CONNECT_TO_AUDIO_DEVICE
    resumePlaybackWhenDeviceConnected = false

    var skipSilence by Settings.AUDIO_SKIP_SILENCE
    skipSilence = false
    var skipMediaOnError by Settings.PLAYBACK_SKIP_ON_ERROR
    skipMediaOnError = false
    var volumeNormalization by Settings.AUDIO_VOLUME_NORMALIZATION
    volumeNormalization = false
    var recommendationsNumber by Settings.MAX_NUMBER_OF_SMART_RECOMMENDATIONS
    recommendationsNumber = RecommendationsNumber.`5`
    var keepPlayerMinimized by Settings.PLAYER_KEEP_MINIMIZED
    keepPlayerMinimized = false
    var uiType by Settings.MAIN_THEME
    uiType = UiType.RiMusic
    var disablePlayerHorizontalSwipe by Settings.PLAYER_THUMBNAIL_HORIZONTAL_SWIPE_DISABLED
    disablePlayerHorizontalSwipe = false
    var colorPaletteName by Settings.COLOR_PALETTE
    colorPaletteName = ColorPaletteName.Dynamic
    var colorPaletteMode by Settings.THEME_MODE
    colorPaletteMode = ColorPaletteMode.Dark
    var indexNavigationTab by Settings.STARTUP_SCREEN
    indexNavigationTab = HomeScreenTabs.Default
    var fontType by Settings.FONT
    fontType = FontType.Rubik
    var useSystemFont by Settings.USE_SYSTEM_FONT
    useSystemFont = false
    var applyFontPadding by Settings.APPLY_FONT_PADDING
    applyFontPadding = false
    var isSwipeToActionEnabled by Settings.ENABLE_SWIPE_ACTION
    isSwipeToActionEnabled = true
    var disableClosingPlayerSwipingDown by Settings.MINI_DISABLE_SWIPE_DOWN_TO_DISMISS
    disableClosingPlayerSwipingDown = false
    var showSearchTab by Settings.SHOW_SEARCH_IN_NAVIGATION_BAR
    showSearchTab = false
    var showStatsInNavbar by Settings.SHOW_STATS_IN_NAVIGATION_BAR
    showStatsInNavbar = false
    var maxStatisticsItems by Settings.MAX_NUMBER_OF_STATISTIC_ITEMS
    maxStatisticsItems = MaxStatisticsItems.`10`
    var showStatsListeningTime by Settings.SHOW_LISTENING_STATS
    showStatsListeningTime = true
    var maxTopPlaylistItems by Settings.MAX_NUMBER_OF_TOP_PLAYED
    maxTopPlaylistItems = MaxTopPlaylistItems.`10`
    var navigationBarPosition by Settings.NAVIGATION_BAR_POSITION
    navigationBarPosition = NavigationBarPosition.Bottom
    var navigationBarType by Settings.NAVIGATION_BAR_TYPE
    navigationBarType = NavigationBarType.IconAndText
    var pauseBetweenSongs by Settings.PAUSE_BETWEEN_SONGS
    pauseBetweenSongs = PauseBetweenSongs.`0`
    var maxSongsInQueue by Settings.MAX_NUMBER_OF_SONG_IN_QUEUE
    maxSongsInQueue = MaxSongs.`500`
    var thumbnailRoundness by Settings.THUMBNAIL_BORDER_RADIUS
    thumbnailRoundness = ThumbnailRoundness.Heavy
    var showFavoritesPlaylist by Settings.HOME_SONGS_SHOW_FAVORITES_CHIP
    showFavoritesPlaylist = true
    var showDownloadedPlaylist by Settings.HOME_SONGS_SHOW_DOWNLOADED_CHIP
    showDownloadedPlaylist = true
    var showMyTopPlaylist by Settings.HOME_SONGS_SHOW_MOST_PLAYED_CHIP
    showMyTopPlaylist = true
    var showOnDevicePlaylist by Settings.HOME_SONGS_SHOW_ON_DEVICE_CHIP
    showOnDevicePlaylist = true
    var shakeEventEnabled by Settings.AUDIO_SHAKE_TO_SKIP
    shakeEventEnabled = false
    var useVolumeKeysToChangeSong by Settings.AUDIO_VOLUME_BUTTONS_CHANGE_SONG
    useVolumeKeysToChangeSong = false
    var showFloatingIcon by Settings.SHOW_FLOATING_ICON
    showFloatingIcon = false
    var menuStyle by Settings.MENU_STYLE
    menuStyle = MenuStyle.List
    var transitionEffect by Settings.TRANSITION_EFFECT
    transitionEffect = TransitionEffect.Scale
    var enableCreateMonthlyPlaylists by Settings.MONTHLY_PLAYLIST_COMPILATION
    enableCreateMonthlyPlaylists = true
    var showPipedPlaylists by Settings.SHOW_PIPED_PLAYLISTS
    showPipedPlaylists = true
    var showPinnedPlaylists by Settings.SHOW_PINNED_PLAYLISTS
    showPinnedPlaylists = true
    var showMonthlyPlaylists by Settings.SHOW_MONTHLY_PLAYLISTS
    showMonthlyPlaylists = true
    var customThemeLight_Background0 by rememberPreference(customThemeLight_Background0Key, DefaultLightColorPalette.background0.hashCode())
    customThemeLight_Background0 = DefaultLightColorPalette.background0.hashCode()
    var customThemeLight_Background1 by rememberPreference(customThemeLight_Background1Key, DefaultLightColorPalette.background1.hashCode())
    customThemeLight_Background1 = DefaultLightColorPalette.background1.hashCode()
    var customThemeLight_Background2 by rememberPreference(customThemeLight_Background2Key, DefaultLightColorPalette.background2.hashCode())
    customThemeLight_Background2 = DefaultLightColorPalette.background2.hashCode()
    var customThemeLight_Background3 by rememberPreference(customThemeLight_Background3Key, DefaultLightColorPalette.background3.hashCode())
    customThemeLight_Background3 = DefaultLightColorPalette.background3.hashCode()
    var customThemeLight_Background4 by rememberPreference(customThemeLight_Background4Key, DefaultLightColorPalette.background4.hashCode())
    customThemeLight_Background4 = DefaultLightColorPalette.background4.hashCode()
    var customThemeLight_Text by rememberPreference(customThemeLight_TextKey, DefaultLightColorPalette.text.hashCode())
    customThemeLight_Text = DefaultLightColorPalette.text.hashCode()
    var customThemeLight_TextSecondary by rememberPreference(customThemeLight_textSecondaryKey, DefaultLightColorPalette.textSecondary.hashCode())
    customThemeLight_TextSecondary = DefaultLightColorPalette.textSecondary.hashCode()
    var customThemeLight_TextDisabled by rememberPreference(customThemeLight_textDisabledKey, DefaultLightColorPalette.textDisabled.hashCode())
    customThemeLight_TextDisabled = DefaultLightColorPalette.textDisabled.hashCode()
    var customThemeLight_IconButtonPlayer by rememberPreference(customThemeLight_iconButtonPlayerKey, DefaultLightColorPalette.iconButtonPlayer.hashCode())
    customThemeLight_IconButtonPlayer = DefaultLightColorPalette.iconButtonPlayer.hashCode()
    var customThemeLight_Accent by rememberPreference(customThemeLight_accentKey, DefaultLightColorPalette.accent.hashCode())
    customThemeLight_Accent = DefaultLightColorPalette.accent.hashCode()
    var customThemeDark_Background0 by rememberPreference(customThemeDark_Background0Key, DefaultDarkColorPalette.background0.hashCode())
    customThemeDark_Background0 = DefaultDarkColorPalette.background0.hashCode()
    var customThemeDark_Background1 by rememberPreference(customThemeDark_Background1Key, DefaultDarkColorPalette.background1.hashCode())
    customThemeDark_Background1 = DefaultDarkColorPalette.background1.hashCode()
    var customThemeDark_Background2 by rememberPreference(customThemeDark_Background2Key, DefaultDarkColorPalette.background2.hashCode())
    customThemeDark_Background2 = DefaultDarkColorPalette.background2.hashCode()
    var customThemeDark_Background3 by rememberPreference(customThemeDark_Background3Key, DefaultDarkColorPalette.background3.hashCode())
    customThemeDark_Background3 = DefaultDarkColorPalette.background3.hashCode()
    var customThemeDark_Background4 by rememberPreference(customThemeDark_Background4Key, DefaultDarkColorPalette.background4.hashCode())
    customThemeDark_Background4 = DefaultDarkColorPalette.background4.hashCode()
    var customThemeDark_Text by rememberPreference(customThemeDark_TextKey, DefaultDarkColorPalette.text.hashCode())
    customThemeDark_Text = DefaultDarkColorPalette.text.hashCode()
    var customThemeDark_TextSecondary by rememberPreference(customThemeDark_textSecondaryKey, DefaultDarkColorPalette.textSecondary.hashCode())
    customThemeDark_TextSecondary = DefaultDarkColorPalette.textSecondary.hashCode()
    var customThemeDark_TextDisabled by rememberPreference(customThemeDark_textDisabledKey, DefaultDarkColorPalette.textDisabled.hashCode())
    customThemeDark_TextDisabled = DefaultDarkColorPalette.textDisabled.hashCode()
    var customThemeDark_IconButtonPlayer by rememberPreference(customThemeDark_iconButtonPlayerKey, DefaultDarkColorPalette.iconButtonPlayer.hashCode())
    customThemeDark_IconButtonPlayer = DefaultDarkColorPalette.iconButtonPlayer.hashCode()
    var customThemeDark_Accent by rememberPreference(customThemeDark_accentKey, DefaultDarkColorPalette.accent.hashCode())
    customThemeDark_Accent = DefaultDarkColorPalette.accent.hashCode()
    var resetCustomLightThemeDialog by rememberSaveable { mutableStateOf(false) }
    resetCustomLightThemeDialog = false
    var resetCustomDarkThemeDialog by rememberSaveable { mutableStateOf(false) }
    resetCustomDarkThemeDialog = false
    var playbackFadeAudioDuration by Settings.AUDIO_FADE_DURATION
    playbackFadeAudioDuration = DurationInMilliseconds.Disabled
    var playerPosition by Settings.MINI_PLAYER_POSITION
    playerPosition = PlayerPosition.Bottom
    var excludeSongWithDurationLimit by Settings.LIMIT_SONGS_WITH_DURATION
    excludeSongWithDurationLimit = DurationInMinutes.Disabled
    var playlistindicator by Settings.SHOW_PLAYLIST_INDICATOR
    playlistindicator = false
    var discoverIsEnabled by Settings.ENABLE_DISCOVER
    discoverIsEnabled = false
    var isPauseOnVolumeZeroEnabled by Settings.PAUSE_WHEN_VOLUME_SET_TO_ZERO
    isPauseOnVolumeZeroEnabled = false
    var minimumSilenceDuration by rememberPreference(minimumSilenceDurationKey, 2_000_000L)
    minimumSilenceDuration = 2_000_000L
    var pauseListenHistory by Settings.PAUSE_HISTORY
    pauseListenHistory = false
    var showTopActionsBar by Settings.PLAYER_SHOW_TOP_ACTIONS_BAR
    showTopActionsBar = true
    var playerControlsType by Settings.PLAYER_CONTROLS_TYPE
    playerControlsType = PlayerControlsType.Modern
    var playerInfoType by Settings.PLAYER_INFO_TYPE
    playerInfoType = PlayerInfoType.Modern
    var playerType by Settings.PLAYER_TYPE
    playerType = PlayerType.Essential
    var queueType by Settings.QUEUE_TYPE
    queueType = QueueType.Essential
    var fadingedge by Settings.PLAYER_BACKGROUND_FADING_EDGE
    fadingedge = false
    var carousel by Settings.PLAYER_THUMBNAILS_CAROUSEL
    carousel = true
    var carouselSize by Settings.CAROUSEL_SIZE
    carouselSize = CarouselSize.Biggest
    var thumbnailType by Settings.THUMBNAIL_TYPE
    thumbnailType = ThumbnailType.Modern
    var playerTimelineType by Settings.PLAYER_TIMELINE_TYPE
    playerTimelineType = PlayerTimelineType.Default
    var playerThumbnailSize by Settings.PLAYER_PORTRAIT_THUMBNAIL_SIZE
    playerThumbnailSize = PlayerThumbnailSize.Biggest
    var playerTimelineSize by Settings.PLAYER_TIMELINE_SIZE
    playerTimelineSize = PlayerTimelineSize.Biggest
    var playerInfoShowIcons by Settings.PLAYER_SONG_INFO_ICON
    playerInfoShowIcons = true
    var miniPlayerType by Settings.MINI_PLAYER_TYPE
    miniPlayerType = MiniPlayerType.Modern
    var playerSwapControlsWithTimeline by Settings.PLAYER_IS_CONTROL_AND_TIMELINE_SWAPPED
    playerSwapControlsWithTimeline = false
    var playerPlayButtonType by Settings.PLAYER_PLAY_BUTTON_TYPE
    playerPlayButtonType = PlayerPlayButtonType.Disabled
    var buttonzoomout by Settings.ZOOM_OUT_ANIMATION
    buttonzoomout = false
    var iconLikeType by Settings.LIKE_ICON
    iconLikeType = IconLikeType.Essential
    var playerBackgroundColors by Settings.PLAYER_BACKGROUND
    playerBackgroundColors = PlayerBackgroundColors.BlurredCoverColor
    var blackgradient by Settings.BLACK_GRADIENT
    blackgradient = false
    var showTotalTimeQueue by Settings.PLAYER_SHOW_TOTAL_QUEUE_TIME
    showTotalTimeQueue = true
    var showNextSongsInPlayer by Settings.PLAYER_SHOW_NEXT_IN_QUEUE
    showNextSongsInPlayer = false
    var showRemainingSongTime by Settings.PLAYER_SHOW_SONGS_REMAINING_TIME
    showRemainingSongTime = true
    var disableScrollingText by Settings.SCROLLING_TEXT_DISABLED
    disableScrollingText = false
    var effectRotationEnabled by Settings.ROTATION_EFFECT
    effectRotationEnabled = true
    var thumbnailTapEnabled by Settings.PLAYER_TAP_THUMBNAIL_FOR_LYRICS
    thumbnailTapEnabled = true
    var clickLyricsText by Settings.LYRICS_JUMP_ON_TAP
    clickLyricsText = true
    var backgroundProgress by Settings.MINI_PLAYER_PROGRESS_BAR
    backgroundProgress = BackgroundProgress.MiniPlayer
    var transparentBackgroundActionBarPlayer by Settings.PLAYER_TRANSPARENT_ACTIONS_BAR
    transparentBackgroundActionBarPlayer = false
    var actionspacedevenly by Settings.PLAYER_ACTION_BUTTONS_SPACED_EVENLY
    actionspacedevenly = false
    var tapqueue by Settings.PLAYER_ACTIONS_BAR_TAP_TO_OPEN_QUEUE
    tapqueue = true
    var swipeUpQueue by Settings.PLAYER_ACTIONS_BAR_SWIPE_UP_TO_OPEN_QUEUE
    swipeUpQueue = true
    var showButtonPlayerAddToPlaylist by Settings.PLAYER_ACTION_ADD_TO_PLAYLIST
    showButtonPlayerAddToPlaylist = true
    var showButtonPlayerArrow by Settings.PLAYER_ACTION_OPEN_QUEUE_ARROW
    showButtonPlayerArrow = false
    var showButtonPlayerDownload by Settings.PLAYER_ACTION_DOWNLOAD
    showButtonPlayerDownload = true
    var showButtonPlayerLoop by Settings.PLAYER_ACTION_LOOP
    showButtonPlayerLoop = true
    var showButtonPlayerLyrics by Settings.PLAYER_ACTION_SHOW_LYRICS
    showButtonPlayerLyrics = true
    var expandedplayertoggle by Settings.PLAYER_ACTION_TOGGLE_EXPAND
    expandedplayertoggle = true
    var showButtonPlayerShuffle by Settings.PLAYER_ACTION_SHUFFLE
    showButtonPlayerShuffle = true
    var showButtonPlayerSleepTimer by Settings.PLAYER_ACTION_SLEEP_TIMER
    showButtonPlayerSleepTimer = false
    var showButtonPlayerMenu by Settings.PLAYER_ACTION_SHOW_MENU
    showButtonPlayerMenu = false
    var showButtonPlayerSystemEqualizer by Settings.PLAYER_ACTION_OPEN_EQUALIZER
    showButtonPlayerSystemEqualizer = false
    var queueSwipeLeftAction by Settings.QUEUE_SWIPE_LEFT_ACTION
    queueSwipeLeftAction = QueueSwipeAction.RemoveFromQueue
    var queueSwipeRightAction by Settings.QUEUE_SWIPE_RIGHT_ACTION
    queueSwipeRightAction = QueueSwipeAction.PlayNext

    var playlistSwipeLeftAction by Settings.PLAYLIST_SWIPE_LEFT_ACTION
    playlistSwipeLeftAction = PlaylistSwipeAction.Favourite
    var playlistSwipeRightAction by Settings.PLAYLIST_SWIPE_LEFT_ACTION
    playlistSwipeRightAction = PlaylistSwipeAction.PlayNext

    var albumSwipeLeftAction by Settings.ALBUM_SWIPE_LEFT_ACTION
    albumSwipeLeftAction = AlbumSwipeAction.PlayNext
    var albumSwipeRightAction by Settings.ALBUM_SWIPE_RIGHT_ACTION
    albumSwipeRightAction = AlbumSwipeAction.Bookmark

    var showButtonPlayerDiscover by Settings.PLAYER_ACTION_DISCOVER
    showButtonPlayerDiscover = false
    var playerEnableLyricsPopupMessage by Settings.PLAYER_ACTION_LYRICS_POPUP_MESSAGE
    playerEnableLyricsPopupMessage = true
    var visualizerEnabled by Settings.PLAYER_VISUALIZER
    visualizerEnabled = false
    var showthumbnail by Settings.PLAYER_SHOW_THUMBNAIL
    showthumbnail = true
}

@ExperimentalAnimationApi
@UnstableApi
@Composable
fun UiSettings(
    navController: NavController
) {
    val binder = LocalPlayerServiceBinder.current



    var recommendationsNumber by Settings.MAX_NUMBER_OF_SMART_RECOMMENDATIONS

    var keepPlayerMinimized by Settings.PLAYER_KEEP_MINIMIZED

    var disablePlayerHorizontalSwipe by Settings.PLAYER_THUMBNAIL_HORIZONTAL_SWIPE_DISABLED

    var colorPaletteName by Settings.COLOR_PALETTE
    var colorPaletteMode by Settings.THEME_MODE
    var indexNavigationTab by Settings.STARTUP_SCREEN
    var fontType by Settings.FONT
    var useSystemFont by Settings.USE_SYSTEM_FONT
    var applyFontPadding by Settings.APPLY_FONT_PADDING
    var isSwipeToActionEnabled by Settings.ENABLE_SWIPE_ACTION
    var showSearchTab by Settings.SHOW_SEARCH_IN_NAVIGATION_BAR
    var showStatsInNavbar by Settings.SHOW_STATS_IN_NAVIGATION_BAR
    var maxStatisticsItems by Settings.MAX_NUMBER_OF_STATISTIC_ITEMS
    var showStatsListeningTime by Settings.SHOW_LISTENING_STATS
    var maxTopPlaylistItems by Settings.MAX_NUMBER_OF_TOP_PLAYED
    var navigationBarPosition by Settings.NAVIGATION_BAR_POSITION
    var navigationBarType by Settings.NAVIGATION_BAR_TYPE
    val search = Search()

    var showFavoritesPlaylist by Settings.HOME_SONGS_SHOW_FAVORITES_CHIP
    var showCachedPlaylist by Settings.HOME_SONGS_SHOW_CACHED_CHIP
    var showDownloadedPlaylist by Settings.HOME_SONGS_SHOW_DOWNLOADED_CHIP
    var showMyTopPlaylist by Settings.HOME_SONGS_SHOW_MOST_PLAYED_CHIP
    var showOnDevicePlaylist by Settings.HOME_SONGS_SHOW_ON_DEVICE_CHIP
    var showFloatingIcon by Settings.SHOW_FLOATING_ICON
    var menuStyle by Settings.MENU_STYLE
    var transitionEffect by Settings.TRANSITION_EFFECT
    var enableCreateMonthlyPlaylists by Settings.MONTHLY_PLAYLIST_COMPILATION
    var showPipedPlaylists by Settings.SHOW_PIPED_PLAYLISTS
    var showPinnedPlaylists by Settings.SHOW_PINNED_PLAYLISTS
    var showMonthlyPlaylists by Settings.SHOW_MONTHLY_PLAYLISTS

    var customThemeLight_Background0 by rememberPreference(customThemeLight_Background0Key, DefaultLightColorPalette.background0.hashCode())
    var customThemeLight_Background1 by rememberPreference(customThemeLight_Background1Key, DefaultLightColorPalette.background1.hashCode())
    var customThemeLight_Background2 by rememberPreference(customThemeLight_Background2Key, DefaultLightColorPalette.background2.hashCode())
    var customThemeLight_Background3 by rememberPreference(customThemeLight_Background3Key, DefaultLightColorPalette.background3.hashCode())
    var customThemeLight_Background4 by rememberPreference(customThemeLight_Background4Key, DefaultLightColorPalette.background4.hashCode())
    var customThemeLight_Text by rememberPreference(customThemeLight_TextKey, DefaultLightColorPalette.text.hashCode())
    var customThemeLight_TextSecondary by rememberPreference(customThemeLight_textSecondaryKey, DefaultLightColorPalette.textSecondary.hashCode())
    var customThemeLight_TextDisabled by rememberPreference(customThemeLight_textDisabledKey, DefaultLightColorPalette.textDisabled.hashCode())
    var customThemeLight_IconButtonPlayer by rememberPreference(customThemeLight_iconButtonPlayerKey, DefaultLightColorPalette.iconButtonPlayer.hashCode())
    var customThemeLight_Accent by rememberPreference(customThemeLight_accentKey, DefaultLightColorPalette.accent.hashCode())

    var customThemeDark_Background0 by rememberPreference(customThemeDark_Background0Key, DefaultDarkColorPalette.background0.hashCode())
    var customThemeDark_Background1 by rememberPreference(customThemeDark_Background1Key, DefaultDarkColorPalette.background1.hashCode())
    var customThemeDark_Background2 by rememberPreference(customThemeDark_Background2Key, DefaultDarkColorPalette.background2.hashCode())
    var customThemeDark_Background3 by rememberPreference(customThemeDark_Background3Key, DefaultDarkColorPalette.background3.hashCode())
    var customThemeDark_Background4 by rememberPreference(customThemeDark_Background4Key, DefaultDarkColorPalette.background4.hashCode())
    var customThemeDark_Text by rememberPreference(customThemeDark_TextKey, DefaultDarkColorPalette.text.hashCode())
    var customThemeDark_TextSecondary by rememberPreference(customThemeDark_textSecondaryKey, DefaultDarkColorPalette.textSecondary.hashCode())
    var customThemeDark_TextDisabled by rememberPreference(customThemeDark_textDisabledKey, DefaultDarkColorPalette.textDisabled.hashCode())
    var customThemeDark_IconButtonPlayer by rememberPreference(customThemeDark_iconButtonPlayerKey, DefaultDarkColorPalette.iconButtonPlayer.hashCode())
    var customThemeDark_Accent by rememberPreference(customThemeDark_accentKey, DefaultDarkColorPalette.accent.hashCode())

    var resetCustomLightThemeDialog by rememberSaveable { mutableStateOf(false) }
    var resetCustomDarkThemeDialog by rememberSaveable { mutableStateOf(false) }
    var playerPosition by Settings.MINI_PLAYER_POSITION

    /*  ViMusic Mode Settings  */
    var showTopActionsBar by Settings.PLAYER_SHOW_TOP_ACTIONS_BAR
    var playerControlsType by Settings.PLAYER_CONTROLS_TYPE
    var playerType by Settings.PLAYER_TYPE
    var queueType by Settings.QUEUE_TYPE
    var fadingedge by Settings.PLAYER_BACKGROUND_FADING_EDGE
    var carousel by Settings.PLAYER_THUMBNAILS_CAROUSEL
    var carouselSize by Settings.CAROUSEL_SIZE
    var thumbnailType by Settings.THUMBNAIL_TYPE
    var playerTimelineType by Settings.PLAYER_TIMELINE_TYPE
    var playerThumbnailSize by Settings.PLAYER_PORTRAIT_THUMBNAIL_SIZE
    var playerTimelineSize by Settings.PLAYER_TIMELINE_SIZE
    var playerInfoShowIcons by Settings.PLAYER_SONG_INFO_ICON
    var miniPlayerType by Settings.MINI_PLAYER_TYPE
    var playerSwapControlsWithTimeline by Settings.PLAYER_IS_CONTROL_AND_TIMELINE_SWAPPED
    var playerPlayButtonType by Settings.PLAYER_PLAY_BUTTON_TYPE
    var buttonzoomout by Settings.ZOOM_OUT_ANIMATION
    var iconLikeType by Settings.LIKE_ICON
    var playerBackgroundColors by Settings.PLAYER_BACKGROUND
    var blackgradient by Settings.BLACK_GRADIENT
    var showTotalTimeQueue by Settings.PLAYER_SHOW_TOTAL_QUEUE_TIME
    var showNextSongsInPlayer by Settings.PLAYER_SHOW_NEXT_IN_QUEUE
    var showRemainingSongTime by Settings.PLAYER_SHOW_SONGS_REMAINING_TIME
    var disableScrollingText by Settings.SCROLLING_TEXT_DISABLED
    var effectRotationEnabled by Settings.ROTATION_EFFECT
    var thumbnailTapEnabled by Settings.PLAYER_TAP_THUMBNAIL_FOR_LYRICS
    var clickLyricsText by Settings.LYRICS_JUMP_ON_TAP
    var backgroundProgress by Settings.MINI_PLAYER_PROGRESS_BAR
    var transparentBackgroundActionBarPlayer by Settings.PLAYER_TRANSPARENT_ACTIONS_BAR
    var actionspacedevenly by Settings.PLAYER_ACTION_BUTTONS_SPACED_EVENLY
    var tapqueue by Settings.PLAYER_ACTIONS_BAR_TAP_TO_OPEN_QUEUE
    var swipeUpQueue by Settings.PLAYER_ACTIONS_BAR_SWIPE_UP_TO_OPEN_QUEUE
    var showButtonPlayerAddToPlaylist by Settings.PLAYER_ACTION_ADD_TO_PLAYLIST
    var showButtonPlayerArrow by Settings.PLAYER_ACTION_OPEN_QUEUE_ARROW
    var showButtonPlayerDownload by Settings.PLAYER_ACTION_DOWNLOAD
    var showButtonPlayerLoop by Settings.PLAYER_ACTION_LOOP
    var showButtonPlayerLyrics by Settings.PLAYER_ACTION_SHOW_LYRICS
    var expandedplayertoggle by Settings.PLAYER_ACTION_TOGGLE_EXPAND
    var showButtonPlayerShuffle by Settings.PLAYER_ACTION_SHUFFLE
    var showButtonPlayerSleepTimer by Settings.PLAYER_ACTION_SLEEP_TIMER
    var showButtonPlayerMenu by Settings.PLAYER_ACTION_SHOW_MENU
    var showButtonPlayerSystemEqualizer by Settings.PLAYER_ACTION_OPEN_EQUALIZER
    var showButtonPlayerDiscover by Settings.PLAYER_ACTION_DISCOVER
    var playerEnableLyricsPopupMessage by Settings.PLAYER_ACTION_LYRICS_POPUP_MESSAGE
    var visualizerEnabled by Settings.PLAYER_VISUALIZER
    var showthumbnail by Settings.PLAYER_SHOW_THUMBNAIL
    /*  ViMusic Mode Settings  */

    var queueSwipeLeftAction by Settings.QUEUE_SWIPE_LEFT_ACTION
    var queueSwipeRightAction by Settings.QUEUE_SWIPE_RIGHT_ACTION
    var playlistSwipeLeftAction by Settings.PLAYLIST_SWIPE_LEFT_ACTION
    var playlistSwipeRightAction by Settings.PLAYLIST_SWIPE_RIGHT_ACTION
    var albumSwipeLeftAction by Settings.ALBUM_SWIPE_LEFT_ACTION
    var albumSwipeRightAction by Settings.ALBUM_SWIPE_RIGHT_ACTION

    var customColor by rememberPreference(customColorKey, Color.Green.hashCode())

    Column(
        modifier = Modifier
            .background(colorPalette().background0)
            //.fillMaxSize()
            .fillMaxHeight()
            .fillMaxWidth(
                if (navigationBarPosition == NavigationBarPosition.Left ||
                    navigationBarPosition == NavigationBarPosition.Top ||
                    navigationBarPosition == NavigationBarPosition.Bottom
                ) 1f
                else Dimensions.contentWidthRightBar
            )
            .verticalScroll(rememberScrollState())
            /*
            .padding(
                LocalPlayerAwareWindowInsets.current
                    .only(WindowInsetsSides.Vertical + WindowInsetsSides.End)
                    .asPaddingValues()
            )
             */
    ) {
        HeaderWithIcon(
            title = stringResource(R.string.user_interface),
            iconId = R.drawable.ui,
            enabled = false,
            showIcon = true,
            modifier = Modifier,
            onClick = {}
        )

        search.ToolBarButton()
        search.SearchBar( this )

        if (resetCustomLightThemeDialog) {
            ConfirmationDialog(
                text = stringResource(R.string.do_you_really_want_to_reset_the_custom_light_theme_colors),
                onDismiss = { resetCustomLightThemeDialog = false },
                onConfirm = {
                    resetCustomLightThemeDialog = false
                    customThemeLight_Background0 = DefaultLightColorPalette.background0.hashCode()
                    customThemeLight_Background1 = DefaultLightColorPalette.background1.hashCode()
                    customThemeLight_Background2 = DefaultLightColorPalette.background2.hashCode()
                    customThemeLight_Background3 = DefaultLightColorPalette.background3.hashCode()
                    customThemeLight_Background4 = DefaultLightColorPalette.background4.hashCode()
                    customThemeLight_Text = DefaultLightColorPalette.text.hashCode()
                    customThemeLight_TextSecondary = DefaultLightColorPalette.textSecondary.hashCode()
                    customThemeLight_TextDisabled = DefaultLightColorPalette.textDisabled.hashCode()
                    customThemeLight_IconButtonPlayer = DefaultLightColorPalette.iconButtonPlayer.hashCode()
                    customThemeLight_Accent = DefaultLightColorPalette.accent.hashCode()
                }
            )
        }

        if (resetCustomDarkThemeDialog) {
            ConfirmationDialog(
                text = stringResource(R.string.do_you_really_want_to_reset_the_custom_dark_theme_colors),
                onDismiss = { resetCustomDarkThemeDialog = false },
                onConfirm = {
                    resetCustomDarkThemeDialog = false
                    customThemeDark_Background0 = DefaultDarkColorPalette.background0.hashCode()
                    customThemeDark_Background1 = DefaultDarkColorPalette.background1.hashCode()
                    customThemeDark_Background2 = DefaultDarkColorPalette.background2.hashCode()
                    customThemeDark_Background3 = DefaultDarkColorPalette.background3.hashCode()
                    customThemeDark_Background4 = DefaultDarkColorPalette.background4.hashCode()
                    customThemeDark_Text = DefaultDarkColorPalette.text.hashCode()
                    customThemeDark_TextSecondary = DefaultDarkColorPalette.textSecondary.hashCode()
                    customThemeDark_TextDisabled = DefaultDarkColorPalette.textDisabled.hashCode()
                    customThemeDark_IconButtonPlayer = DefaultDarkColorPalette.iconButtonPlayer.hashCode()
                    customThemeDark_Accent = DefaultDarkColorPalette.accent.hashCode()
                }
            )
        }

        SettingsGroupSpacer()
        SettingsEntryGroupText(stringResource(R.string.user_interface))

        var uiType by Settings.MAIN_THEME
        if (search.inputValue.isBlank() || stringResource(R.string.interface_in_use).contains(search.inputValue,true))
            EnumValueSelectorSettingsEntry(
                title = stringResource(R.string.interface_in_use),
                selectedValue = uiType,
                onValueSelected = {
                    uiType = it
                    if (uiType == UiType.ViMusic) {
                        disablePlayerHorizontalSwipe = true
                        playerTimelineType = PlayerTimelineType.FakeAudioBar
                        visualizerEnabled = false
                        playerThumbnailSize = PlayerThumbnailSize.Medium
                        thumbnailTapEnabled = true
                        showSearchTab = true
                        showStatsInNavbar = true
                        navigationBarPosition = NavigationBarPosition.Left
                        showTopActionsBar = false
                        playerType = PlayerType.Modern
                        queueType = QueueType.Modern
                        fadingedge = false
                        carousel = true
                        carouselSize = CarouselSize.Medium
                        thumbnailType = ThumbnailType.Essential
                        playerTimelineSize = PlayerTimelineSize.Medium
                        playerInfoShowIcons = true
                        miniPlayerType = MiniPlayerType.Modern
                        playerSwapControlsWithTimeline = false
                        transparentBackgroundActionBarPlayer = false
                        playerControlsType = PlayerControlsType.Essential
                        playerPlayButtonType = PlayerPlayButtonType.Disabled
                        buttonzoomout = true
                        iconLikeType = IconLikeType.Essential
                        playerBackgroundColors = PlayerBackgroundColors.CoverColorGradient
                        blackgradient = true
                        showTotalTimeQueue = false
                        showRemainingSongTime = false
                        showNextSongsInPlayer = false
                        disableScrollingText = false
                        effectRotationEnabled = true
                        clickLyricsText = true
                        playerEnableLyricsPopupMessage = true
                        backgroundProgress = BackgroundProgress.MiniPlayer
                        transparentBackgroundActionBarPlayer = true
                        actionspacedevenly = false
                        tapqueue = false
                        swipeUpQueue = true
                        showButtonPlayerDiscover = false
                        showButtonPlayerDownload = false
                        showButtonPlayerAddToPlaylist = false
                        showButtonPlayerLoop = false
                        showButtonPlayerShuffle = false
                        showButtonPlayerLyrics = false
                        expandedplayertoggle = false
                        showButtonPlayerSleepTimer = false
                        showButtonPlayerSystemEqualizer = false
                        showButtonPlayerArrow = false
                        showButtonPlayerShuffle = false
                        showButtonPlayerMenu = true
                        showthumbnail = true
                        keepPlayerMinimized = false
                    } else {
                        disablePlayerHorizontalSwipe = false
                        navigationBarPosition = NavigationBarPosition.Bottom
                    }

                    RestartAppDialog.showDialog()
                },
                valueText = { it.name }
            )

        if (search.inputValue.isBlank() || stringResource(R.string.theme).contains(search.inputValue,true))
            EnumValueSelectorSettingsEntry(
                title = stringResource(R.string.theme),
                selectedValue = colorPaletteName,
                onValueSelected = {
                    colorPaletteName = it
                   when (it) {
                       ColorPaletteName.PureBlack,
                       ColorPaletteName.ModernBlack -> colorPaletteMode = ColorPaletteMode.System
                       else -> {}
                   }
                },
                valueText = { it.text }
            )

        AnimatedVisibility(visible = colorPaletteName == ColorPaletteName.CustomColor) {
            Column{
            ColorSettingEntry(
                title = stringResource(R.string.customcolor),
                text = "",
                color = Color(customColor),
                onColorSelected = {
                    customColor = it.hashCode()
                },
                modifier = Modifier
                    .padding(start = 25.dp)
            )
            ImportantSettingsDescription(
                text = stringResource(R.string.restarting_rimusic_is_required),
                modifier = Modifier
                .padding(start = 25.dp)
            )
            }
        }
        AnimatedVisibility(visible = colorPaletteName == ColorPaletteName.Customized) {
            Column {
                SettingsEntryGroupText(stringResource(R.string.title_customized_light_theme_colors))
                ButtonBarSettingEntry(
                    title = stringResource(R.string.title_reset_customized_light_colors),
                    text = stringResource(R.string.info_click_to_reset_default_light_colors),
                    icon = R.drawable.trash,
                    onClick = { resetCustomLightThemeDialog = true }
                )
                ColorSettingEntry(
                    title = stringResource(R.string.color_background_1),
                    text = "",
                    color = Color(customThemeLight_Background0),
                    onColorSelected = {
                        customThemeLight_Background0 = it.hashCode()
                    }
                )
                ColorSettingEntry(
                    title = stringResource(R.string.color_background_2),
                    text = "",
                    color = Color(customThemeLight_Background1),
                    onColorSelected = {
                        customThemeLight_Background1 = it.hashCode()
                    }
                )
                ColorSettingEntry(
                    title = stringResource(R.string.color_background_3),
                    text = "",
                    color = Color(customThemeLight_Background2),
                    onColorSelected = {
                        customThemeLight_Background2 = it.hashCode()
                    }
                )
                ColorSettingEntry(
                    title = stringResource(R.string.color_background_4),
                    text = "",
                    color = Color(customThemeLight_Background3),
                    onColorSelected = {
                        customThemeLight_Background3 = it.hashCode()
                    }
                )
                ColorSettingEntry(
                    title = stringResource(R.string.color_background_5),
                    text = "",
                    color = Color(customThemeLight_Background4),
                    onColorSelected = {
                        customThemeLight_Background4 = it.hashCode()
                    }
                )
                ColorSettingEntry(
                    title = stringResource(R.string.color_text),
                    text = "",
                    color = Color(customThemeLight_Text),
                    onColorSelected = {
                        customThemeLight_Text= it.hashCode()
                    }
                )
                ColorSettingEntry(
                    title = stringResource(R.string.color_text_secondary),
                    text = "",
                    color = Color(customThemeLight_TextSecondary),
                    onColorSelected = {
                        customThemeLight_TextSecondary = it.hashCode()
                    }
                )
                ColorSettingEntry(
                    title = stringResource(R.string.color_text_disabled),
                    text = "",
                    color = Color(customThemeLight_TextDisabled),
                    onColorSelected = {
                        customThemeLight_TextDisabled = it.hashCode()
                    }
                )
                ColorSettingEntry(
                    title = stringResource(R.string.color_icon_button_player),
                    text = "",
                    color = Color(customThemeLight_IconButtonPlayer),
                    onColorSelected = {
                        customThemeLight_IconButtonPlayer = it.hashCode()
                    }
                )
                ColorSettingEntry(
                    title = stringResource(R.string.color_accent),
                    text = "",
                    color = Color(customThemeLight_Accent),
                    onColorSelected = {
                        customThemeLight_Accent = it.hashCode()
                    }
                )

                SettingsEntryGroupText(stringResource(R.string.title_customized_dark_theme_colors))
                ButtonBarSettingEntry(
                    title = stringResource(R.string.title_reset_customized_dark_colors),
                    text = stringResource(R.string.click_to_reset_default_dark_colors),
                    icon = R.drawable.trash,
                    onClick = { resetCustomDarkThemeDialog = true }
                )
                ColorSettingEntry(
                    title = stringResource(R.string.color_background_1),
                    text = "",
                    color = Color(customThemeDark_Background0),
                    onColorSelected = {
                        customThemeDark_Background0 = it.hashCode()
                    }
                )
                ColorSettingEntry(
                    title = stringResource(R.string.color_background_2),
                    text = "",
                    color = Color(customThemeDark_Background1),
                    onColorSelected = {
                        customThemeDark_Background1 = it.hashCode()
                    }
                )
                ColorSettingEntry(
                    title = stringResource(R.string.color_background_3),
                    text = "",
                    color = Color(customThemeDark_Background2),
                    onColorSelected = {
                        customThemeDark_Background2 = it.hashCode()
                    }
                )
                ColorSettingEntry(
                    title = stringResource(R.string.color_background_4),
                    text = "",
                    color = Color(customThemeDark_Background3),
                    onColorSelected = {
                        customThemeDark_Background3 = it.hashCode()
                    }
                )
                ColorSettingEntry(
                    title = stringResource(R.string.color_background_5),
                    text = "",
                    color = Color(customThemeDark_Background4),
                    onColorSelected = {
                        customThemeDark_Background4 = it.hashCode()
                    }
                )
                ColorSettingEntry(
                    title = stringResource(R.string.color_text),
                    text = "",
                    color = Color(customThemeDark_Text),
                    onColorSelected = {
                        customThemeDark_Text= it.hashCode()
                    }
                )
                ColorSettingEntry(
                    title = stringResource(R.string.color_text_secondary),
                    text = "",
                    color = Color(customThemeDark_TextSecondary),
                    onColorSelected = {
                        customThemeDark_TextSecondary = it.hashCode()
                    }
                )
                ColorSettingEntry(
                    title = stringResource(R.string.color_text_disabled),
                    text = "",
                    color = Color(customThemeDark_TextDisabled),
                    onColorSelected = {
                        customThemeDark_TextDisabled = it.hashCode()
                    }
                )
                ColorSettingEntry(
                    title = stringResource(R.string.color_icon_button_player),
                    text = "",
                    color = Color(customThemeDark_IconButtonPlayer),
                    onColorSelected = {
                        customThemeDark_IconButtonPlayer = it.hashCode()
                    }
                )
                ColorSettingEntry(
                    title = stringResource(R.string.color_accent),
                    text = "",
                    color = Color(customThemeDark_Accent),
                    onColorSelected = {
                        customThemeDark_Accent = it.hashCode()
                    }
                )
            }
        }

        if (search.inputValue.isBlank() || stringResource(R.string.theme_mode).contains(search.inputValue,true))
            EnumValueSelectorSettingsEntry(
                title = stringResource(R.string.theme_mode),
                selectedValue = colorPaletteMode,
                isEnabled = when (colorPaletteName) {
                    ColorPaletteName.PureBlack -> false
                    ColorPaletteName.ModernBlack -> false
                    else -> { true }
                },
                onValueSelected = {
                    colorPaletteMode = it
                    //if (it == ColorPaletteMode.PitchBlack) colorPaletteName = ColorPaletteName.ModernBlack
                },
                valueText = { it.text }
            )

        if (search.inputValue.isBlank() || stringResource(R.string.navigation_bar_position).contains(search.inputValue,true))
            EnumValueSelectorSettingsEntry(
                title = stringResource(R.string.navigation_bar_position),
                selectedValue = navigationBarPosition,
                onValueSelected = { navigationBarPosition = it },
                // As of version 0.6.53, changing navigation bar to top or bottom
                // while using ViMusic theme breaks the UI
                isEnabled = uiType != UiType.ViMusic,
                valueText = { it.text }
            )

        if (search.inputValue.isBlank() || stringResource(R.string.navigation_bar_type).contains(search.inputValue,true))
            EnumValueSelectorSettingsEntry(
                title = stringResource(R.string.navigation_bar_type),
                selectedValue = navigationBarType,
                onValueSelected = { navigationBarType = it },
                valueText = { it.text }
            )

        if (search.inputValue.isBlank() || stringResource(R.string.player_position).contains(search.inputValue,true))
            EnumValueSelectorSettingsEntry(
                title = stringResource(R.string.player_position),
                selectedValue = playerPosition,
                onValueSelected = { playerPosition = it },
                valueText = { it.text }
            )

        if (search.inputValue.isBlank() || stringResource(R.string.menu_style).contains(search.inputValue,true))
            EnumValueSelectorSettingsEntry(
                title = stringResource(R.string.menu_style),
                selectedValue = menuStyle,
                onValueSelected = { menuStyle = it },
                valueText = { it.text }
            )

        if (search.inputValue.isBlank() || stringResource(R.string.default_page).contains(search.inputValue,true))
            EnumValueSelectorSettingsEntry(
                title = stringResource(R.string.default_page),
                selectedValue = indexNavigationTab,
                onValueSelected = {indexNavigationTab = it},
                valueText = { it.text }
            )

        if (search.inputValue.isBlank() || stringResource(R.string.transition_effect).contains(search.inputValue,true))
            EnumValueSelectorSettingsEntry(
                title = stringResource(R.string.transition_effect),
                selectedValue = transitionEffect,
                onValueSelected = { transitionEffect = it },
                valueText = { it.text }
            )

        if ( UiType.ViMusic.isCurrent() ) {
            if (search.inputValue.isBlank() || stringResource(R.string.vimusic_show_search_button_in_navigation_bar).contains(
                    search.inputValue,
                    true
                )
            )
                SwitchSettingEntry(
                    title = stringResource(R.string.vimusic_show_search_button_in_navigation_bar),
                    text = stringResource(R.string.vismusic_only_in_left_right_navigation_bar),
                    isChecked = showSearchTab,
                    onCheckedChange = { showSearchTab = it }
                )



            if (search.inputValue.isBlank() || stringResource(R.string.show_statistics_in_navigation_bar).contains(
                    search.inputValue,
                    true
                )
            )
                SwitchSettingEntry(
                    title = stringResource(R.string.show_statistics_in_navigation_bar),
                    text = "",
                    isChecked = showStatsInNavbar,
                    onCheckedChange = { showStatsInNavbar = it }
                )
        }

        if (search.inputValue.isBlank() || stringResource(R.string.show_floating_icon).contains(search.inputValue,true))
            SwitchSettingEntry(
                title = stringResource(R.string.show_floating_icon),
                text = "",
                isChecked = showFloatingIcon,
                onCheckedChange = { showFloatingIcon = it }
            )



        if (search.inputValue.isBlank() || stringResource(R.string.settings_use_font_type).contains(search.inputValue,true))
            EnumValueSelectorSettingsEntry(
                title = stringResource(R.string.settings_use_font_type),
                selectedValue = fontType,
                onValueSelected = { fontType = it },
                valueText = { it.name }
            )

        if (search.inputValue.isBlank() || stringResource(R.string.use_system_font).contains(search.inputValue,true))
            SwitchSettingEntry(
                title = stringResource(R.string.use_system_font),
                text = stringResource(R.string.use_font_by_the_system),
                isChecked = useSystemFont,
                onCheckedChange = { useSystemFont = it }
            )

        if (search.inputValue.isBlank() || stringResource(R.string.apply_font_padding).contains(search.inputValue,true))
            SwitchSettingEntry(
                title = stringResource(R.string.apply_font_padding),
                text = stringResource(R.string.add_spacing_around_texts),
                isChecked = applyFontPadding,
                onCheckedChange = { applyFontPadding = it }
            )


        if (search.inputValue.isBlank() || stringResource(R.string.swipe_to_action).contains(search.inputValue,true))
        {
            SwitchSettingEntry(
                title = stringResource(R.string.swipe_to_action),
                text = stringResource(R.string.activate_the_action_menu_by_swiping_the_song_left_or_right),
                isChecked = isSwipeToActionEnabled,
                onCheckedChange = { isSwipeToActionEnabled = it }
            )

            AnimatedVisibility(visible = isSwipeToActionEnabled) {
                Column(
                    modifier = Modifier.padding(start = 25.dp)
                ) {
                    EnumValueSelectorSettingsEntry<QueueSwipeAction>(
                        title = stringResource(R.string.queue_and_local_playlists_left_swipe),
                        selectedValue = queueSwipeLeftAction,
                        onValueSelected = {
                            queueSwipeLeftAction = it
                        },
                        valueText = { it.text },
                    )
                    EnumValueSelectorSettingsEntry<QueueSwipeAction>(
                        title = stringResource(R.string.queue_and_local_playlists_right_swipe),
                        selectedValue = queueSwipeRightAction,
                        onValueSelected = {
                            queueSwipeRightAction = it
                        },
                        valueText = { it.text },
                    )
                    EnumValueSelectorSettingsEntry<PlaylistSwipeAction>(
                        title = stringResource(R.string.playlist_left_swipe),
                        selectedValue = playlistSwipeLeftAction,
                        onValueSelected = {
                            playlistSwipeLeftAction = it
                        },
                        valueText = { it.text },
                    )
                    EnumValueSelectorSettingsEntry<PlaylistSwipeAction>(
                        title = stringResource(R.string.playlist_right_swipe),
                        selectedValue = playlistSwipeRightAction,
                        onValueSelected = {
                            playlistSwipeRightAction = it
                        },
                        valueText = { it.text },
                    )
                    EnumValueSelectorSettingsEntry<AlbumSwipeAction>(
                        title = stringResource(R.string.album_left_swipe),
                        selectedValue = albumSwipeLeftAction,
                        onValueSelected = {
                            albumSwipeLeftAction = it
                        },
                        valueText = { it.text },
                    )
                    EnumValueSelectorSettingsEntry<AlbumSwipeAction>(
                        title = stringResource(R.string.album_right_swipe),
                        selectedValue = albumSwipeRightAction,
                        onValueSelected = {
                            albumSwipeRightAction = it
                        },
                        valueText = { it.text },
                    )
                }
            }
        }

        SettingsGroupSpacer()
        SettingsEntryGroupText(title = stringResource(R.string.songs).uppercase())

        if (search.inputValue.isBlank() || "${stringResource(R.string.show)} ${stringResource(R.string.favorites)}".contains(search.inputValue,true))
            SwitchSettingEntry(
                title = "${stringResource(R.string.show)} ${stringResource(R.string.favorites)}",
                text = "",
                isChecked = showFavoritesPlaylist,
                onCheckedChange = { showFavoritesPlaylist = it }
            )

        if (search.inputValue.isBlank() || "${stringResource(R.string.show)} ${stringResource(R.string.cached)}".contains(search.inputValue,true))
            SwitchSettingEntry(
                title = "${stringResource(R.string.show)} ${stringResource(R.string.cached)}",
                text = "",
                isChecked = showCachedPlaylist,
                onCheckedChange = { showCachedPlaylist = it }
            )

        if (search.inputValue.isBlank() || "${stringResource(R.string.show)} ${stringResource(R.string.downloaded)}".contains(search.inputValue,true))
            SwitchSettingEntry(
                title = "${stringResource(R.string.show)} ${stringResource(R.string.downloaded)}",
                text = "",
                isChecked = showDownloadedPlaylist,
                onCheckedChange = { showDownloadedPlaylist = it }
            )
        if (search.inputValue.isBlank() || "${stringResource(R.string.show)} ${stringResource(R.string.my_playlist_top)}".contains(search.inputValue,true))
            SwitchSettingEntry(
                title = "${stringResource(R.string.show)} ${stringResource(R.string.my_playlist_top).format(maxTopPlaylistItems)}",
                text = "",
                isChecked = showMyTopPlaylist,
                onCheckedChange = { showMyTopPlaylist = it }
            )
        if (search.inputValue.isBlank() || "${stringResource(R.string.show)} ${stringResource(R.string.on_device)}".contains(search.inputValue,true))
            SwitchSettingEntry(
                title = "${stringResource(R.string.show)} ${stringResource(R.string.on_device)}",
                text = "",
                isChecked = showOnDevicePlaylist,
                onCheckedChange = { showOnDevicePlaylist = it }
            )

        /*
        SettingsGroupSpacer()
        SettingsEntryGroupText(title = stringResource(R.string.playlists).uppercase())

        if (filter.isNullOrBlank() || "${stringResource(R.string.show)} ${stringResource(R.string.playlists)}".contains(filterCharSequence,true))
            SwitchSettingEntry(
                title = "${stringResource(R.string.show)} ${stringResource(R.string.playlists)}",
                text = "",
                isChecked = showPlaylists,
                onCheckedChange = { showPlaylists = it }
            )
        if (filter.isNullOrBlank() || "${stringResource(R.string.show)} ${stringResource(R.string.monthly_playlists)}".contains(filterCharSequence,true))
            SwitchSettingEntry(
                title = "${stringResource(R.string.show)} ${stringResource(R.string.monthly_playlists)}",
                text = "",
                isChecked = showMonthlyPlaylistInLibrary,
                onCheckedChange = { showMonthlyPlaylistInLibrary = it }
            )
         */

        SettingsGroupSpacer()
        SettingsEntryGroupText(title = stringResource(R.string.playlists).uppercase())

        if (search.inputValue.isBlank() || "${stringResource(R.string.show)} ${stringResource(R.string.piped_playlists)}".contains(search.inputValue,true))
            SwitchSettingEntry(
                title = "${stringResource(R.string.show)} ${stringResource(R.string.piped_playlists)}",
                text = "",
                isChecked = showPipedPlaylists,
                onCheckedChange = { showPipedPlaylists = it }
            )

        if (search.inputValue.isBlank() || "${stringResource(R.string.show)} ${stringResource(R.string.pinned_playlists)}".contains(search.inputValue,true))
            SwitchSettingEntry(
                title = "${stringResource(R.string.show)} ${stringResource(R.string.pinned_playlists)}",
                text = "",
                isChecked = showPinnedPlaylists,
                onCheckedChange = { showPinnedPlaylists = it }
            )

        if (search.inputValue.isBlank() || "${stringResource(R.string.show)} ${stringResource(R.string.monthly_playlists)}".contains(search.inputValue,true))
            SwitchSettingEntry(
                title = "${stringResource(R.string.show)} ${stringResource(R.string.monthly_playlists)}",
                text = "",
                isChecked = showMonthlyPlaylists,
                onCheckedChange = { showMonthlyPlaylists = it }
            )

        SettingsGroupSpacer()
        SettingsEntryGroupText(stringResource(R.string.monthly_playlists).uppercase())

        if (search.inputValue.isBlank() || stringResource(R.string.monthly_playlists).contains(search.inputValue,true))
            SwitchSettingEntry(
                title = stringResource(R.string.enable_monthly_playlists_creation),
                text = "",
                isChecked = enableCreateMonthlyPlaylists,
                onCheckedChange = {
                    enableCreateMonthlyPlaylists = it
                }
            )

        SettingsGroupSpacer()
        SettingsEntryGroupText(stringResource(R.string.smart_recommendations))

        if (search.inputValue.isBlank() || stringResource(R.string.statistics_max_number_of_items).contains(search.inputValue,true))
            EnumValueSelectorSettingsEntry(
                title = stringResource(R.string.statistics_max_number_of_items),
                selectedValue = recommendationsNumber,
                onValueSelected = { recommendationsNumber = it },
                valueText = { it.name }
            )

        SettingsGroupSpacer()
        SettingsEntryGroupText(stringResource(R.string.statistics))

        if (search.inputValue.isBlank() || stringResource(R.string.statistics_max_number_of_items).contains(search.inputValue,true))
            EnumValueSelectorSettingsEntry(
                title = stringResource(R.string.statistics_max_number_of_items),
                selectedValue = maxStatisticsItems,
                onValueSelected = { maxStatisticsItems = it },
                valueText = { it.name }
            )

        if (search.inputValue.isBlank() || stringResource(R.string.listening_time).contains(search.inputValue,true))
            SwitchSettingEntry(
                title = stringResource(R.string.listening_time),
                text = stringResource(R.string.shows_the_number_of_songs_heard_and_their_listening_time),
                isChecked = showStatsListeningTime,
                onCheckedChange = {
                    showStatsListeningTime = it
                }
            )

        SettingsGroupSpacer()
        SettingsEntryGroupText(stringResource(R.string.playlist_top))

        if (search.inputValue.isBlank() || stringResource(R.string.statistics_max_number_of_items).contains(search.inputValue,true))
            EnumValueSelectorSettingsEntry(
                title = stringResource(R.string.statistics_max_number_of_items),
                selectedValue = maxTopPlaylistItems,
                onValueSelected = { maxTopPlaylistItems = it },
                valueText = { it.name }
            )

        var resetToDefault by remember { mutableStateOf(false) }
        val context = LocalContext.current
        ButtonBarSettingEntry(
            title = stringResource(R.string.settings_reset),
            text = stringResource(R.string.settings_restore_default_settings),
            icon = R.drawable.refresh,
            iconColor = colorPalette().text,
            onClick = { resetToDefault = true },
        )
        if (resetToDefault) {
            DefaultUiSettings()
            resetToDefault = false
            navController.popBackStack()
            Toaster.done()
        }

        SettingsGroupSpacer(
            modifier = Modifier.height(Dimensions.bottomSpacer)
        )

    }
}
