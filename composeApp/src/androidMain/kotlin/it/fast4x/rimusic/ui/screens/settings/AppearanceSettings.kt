package it.fast4x.rimusic.ui.screens.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavController
import app.kreate.android.R
import app.kreate.android.Settings
import it.fast4x.rimusic.colorPalette
import it.fast4x.rimusic.enums.AnimatedGradient
import it.fast4x.rimusic.enums.BackgroundProgress
import it.fast4x.rimusic.enums.CarouselSize
import it.fast4x.rimusic.enums.ColorPaletteMode
import it.fast4x.rimusic.enums.ColorPaletteName
import it.fast4x.rimusic.enums.IconLikeType
import it.fast4x.rimusic.enums.MiniPlayerType
import it.fast4x.rimusic.enums.NavigationBarPosition
import it.fast4x.rimusic.enums.PlayerBackgroundColors
import it.fast4x.rimusic.enums.PlayerControlsType
import it.fast4x.rimusic.enums.PlayerInfoType
import it.fast4x.rimusic.enums.PlayerPlayButtonType
import it.fast4x.rimusic.enums.PlayerThumbnailSize
import it.fast4x.rimusic.enums.PlayerTimelineSize
import it.fast4x.rimusic.enums.PlayerTimelineType
import it.fast4x.rimusic.enums.PlayerType
import it.fast4x.rimusic.enums.QueueType
import it.fast4x.rimusic.enums.SongsNumber
import it.fast4x.rimusic.enums.SwipeAnimationNoThumbnail
import it.fast4x.rimusic.enums.ThumbnailRoundness
import it.fast4x.rimusic.enums.ThumbnailType
import it.fast4x.rimusic.typography
import it.fast4x.rimusic.ui.components.themed.AppearancePresetDialog
import it.fast4x.rimusic.ui.components.themed.HeaderWithIcon
import it.fast4x.rimusic.ui.styling.Dimensions
import it.fast4x.rimusic.utils.RestartPlayerService
import it.fast4x.rimusic.utils.isAtLeastAndroid7
import it.fast4x.rimusic.utils.isLandscape
import it.fast4x.rimusic.utils.semiBold
import me.knighthat.component.tab.Search
import me.knighthat.utils.Toaster

@Composable
fun DefaultAppearanceSettings() {
    var showthumbnail by Settings.PLAYER_SHOW_THUMBNAIL
    showthumbnail = true
    var transparentbar by Settings.TRANSPARENT_TIMELINE
    transparentbar = true
    var blackgradient by Settings.BLACK_GRADIENT
    blackgradient = false
    var showlyricsthumbnail by Settings.LYRICS_SHOW_THUMBNAIL
    showlyricsthumbnail = false
    var playerPlayButtonType by Settings.PLAYER_PLAY_BUTTON_TYPE
    playerPlayButtonType = PlayerPlayButtonType.Disabled
    var bottomgradient by Settings.PLAYER_BOTTOM_GRADIENT
    bottomgradient = false
    var textoutline by Settings.TEXT_OUTLINE
    textoutline = false
    var disablePlayerHorizontalSwipe by Settings.PLAYER_THUMBNAIL_HORIZONTAL_SWIPE_DISABLED
    disablePlayerHorizontalSwipe = false
    var disableScrollingText by Settings.SCROLLING_TEXT_DISABLED
    disableScrollingText = false
    var visualizerEnabled by Settings.PLAYER_VISUALIZER
    visualizerEnabled = false
    var playerTimelineType by Settings.PLAYER_TIMELINE_TYPE
    playerTimelineType = PlayerTimelineType.FakeAudioBar
    var playerThumbnailSize by Settings.PLAYER_PORTRAIT_THUMBNAIL_SIZE
    playerThumbnailSize = PlayerThumbnailSize.Biggest
    var playerTimelineSize by Settings.PLAYER_TIMELINE_SIZE
    playerTimelineSize = PlayerTimelineSize.Biggest
    var effectRotationEnabled by Settings.ROTATION_EFFECT
    effectRotationEnabled = true
    var thumbnailTapEnabled by Settings.PLAYER_TAP_THUMBNAIL_FOR_LYRICS
    thumbnailTapEnabled = true
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
    var showButtonPlayerDiscover by Settings.PLAYER_ACTION_DISCOVER
    showButtonPlayerDiscover = false
    var showButtonPlayerVideo by Settings.PLAYER_ACTION_TOGGLE_VIDEO
    showButtonPlayerVideo = false
    var navigationBarPosition by Settings.NAVIGATION_BAR_POSITION
    navigationBarPosition = NavigationBarPosition.Bottom
    var showTotalTimeQueue by Settings.PLAYER_SHOW_TOTAL_QUEUE_TIME
    showTotalTimeQueue = true
    var backgroundProgress by Settings.MINI_PLAYER_PROGRESS_BAR
    backgroundProgress = BackgroundProgress.MiniPlayer
    var showNextSongsInPlayer by Settings.PLAYER_SHOW_NEXT_IN_QUEUE
    showNextSongsInPlayer = false
    var showRemainingSongTime by Settings.PLAYER_SHOW_SONGS_REMAINING_TIME
    showRemainingSongTime = true
    var clickLyricsText by Settings.LYRICS_JUMP_ON_TAP
    clickLyricsText = true
    var showBackgroundLyrics by Settings.LYRICS_SHOW_ACCENT_BACKGROUND
    showBackgroundLyrics = false
    var thumbnailRoundness by Settings.THUMBNAIL_BORDER_RADIUS
    thumbnailRoundness = ThumbnailRoundness.Heavy
    var miniPlayerType by Settings.MINI_PLAYER_TYPE
    miniPlayerType = MiniPlayerType.Modern
    var playerBackgroundColors by Settings.PLAYER_BACKGROUND
    playerBackgroundColors = PlayerBackgroundColors.BlurredCoverColor
    var showTopActionsBar by Settings.PLAYER_SHOW_TOP_ACTIONS_BAR
    showTopActionsBar = true
    var playerControlsType by Settings.PLAYER_CONTROLS_TYPE
    playerControlsType = PlayerControlsType.Modern
    var playerInfoType by Settings.PLAYER_INFO_TYPE
    playerInfoType = PlayerInfoType.Modern
    var transparentBackgroundActionBarPlayer by Settings.PLAYER_TRANSPARENT_ACTIONS_BAR
    transparentBackgroundActionBarPlayer = false
    var iconLikeType by Settings.LIKE_ICON
    iconLikeType = IconLikeType.Essential
    var playerSwapControlsWithTimeline by Settings.PLAYER_IS_CONTROL_AND_TIMELINE_SWAPPED
    playerSwapControlsWithTimeline = false
    var playerEnableLyricsPopupMessage by Settings.PLAYER_ACTION_LYRICS_POPUP_MESSAGE
    playerEnableLyricsPopupMessage = true
    var actionspacedevenly by Settings.PLAYER_ACTION_BUTTONS_SPACED_EVENLY
    actionspacedevenly = false
    var thumbnailType by Settings.THUMBNAIL_TYPE
    thumbnailType = ThumbnailType.Modern
    var showvisthumbnail by Settings.PLAYER_SHOW_THUMBNAIL_ON_VISUALIZER
    showvisthumbnail = false
    var buttonzoomout by Settings.ZOOM_OUT_ANIMATION
    buttonzoomout = false
    var thumbnailpause by Settings.PLAYER_SHRINK_THUMBNAIL_ON_PAUSE
    thumbnailpause = false
    var showsongs by Settings.MAX_NUMBER_OF_NEXT_IN_QUEUE
    showsongs = SongsNumber.`2`
    var showalbumcover by Settings.PLAYER_SHOW_NEXT_IN_QUEUE_THUMBNAIL
    showalbumcover = true
    var tapqueue by Settings.PLAYER_ACTIONS_BAR_TAP_TO_OPEN_QUEUE
    tapqueue = true
    var swipeUpQueue by Settings.PLAYER_ACTIONS_BAR_SWIPE_UP_TO_OPEN_QUEUE
    swipeUpQueue = true
    var statsfornerds by Settings.PLAYER_STATS_FOR_NERDS
    statsfornerds = false
    var playerType by Settings.PLAYER_TYPE
    playerType = PlayerType.Essential
    var queueType by Settings.QUEUE_TYPE
    queueType = QueueType.Essential
    var noblur by Settings.PLAYER_BACKGROUND_BLUR
    noblur = true
    var fadingedge by Settings.PLAYER_BACKGROUND_FADING_EDGE
    fadingedge = false
    var carousel by Settings.PLAYER_THUMBNAILS_CAROUSEL
    carousel = true
    var carouselSize by Settings.CAROUSEL_SIZE
    carouselSize = CarouselSize.Biggest
    var keepPlayerMinimized by Settings.PLAYER_KEEP_MINIMIZED
    keepPlayerMinimized = false
    var playerInfoShowIcons by Settings.PLAYER_SONG_INFO_ICON
    playerInfoShowIcons = true
}

@ExperimentalAnimationApi
@UnstableApi
@Composable
fun AppearanceSettings(
    navController: NavController,
) {

    var showthumbnail by Settings.PLAYER_SHOW_THUMBNAIL
    var transparentbar by Settings.TRANSPARENT_TIMELINE
    var blackgradient by Settings.BLACK_GRADIENT
    var showlyricsthumbnail by Settings.LYRICS_SHOW_THUMBNAIL
    var expandedplayer by Settings.PLAYER_EXPANDED
    var playerPlayButtonType by Settings.PLAYER_PLAY_BUTTON_TYPE
    var bottomgradient by Settings.PLAYER_BOTTOM_GRADIENT
    var textoutline by Settings.TEXT_OUTLINE
    var disablePlayerHorizontalSwipe by Settings.PLAYER_THUMBNAIL_HORIZONTAL_SWIPE_DISABLED
    var disableScrollingText by Settings.SCROLLING_TEXT_DISABLED
    var visualizerEnabled by Settings.PLAYER_VISUALIZER
    /*
    var playerVisualizerType by rememberPreference(
        playerVisualizerTypeKey,
        PlayerVisualizerType.Disabled
    )
    */
    var playerTimelineType by Settings.PLAYER_TIMELINE_TYPE
    var playerThumbnailSize by Settings.PLAYER_PORTRAIT_THUMBNAIL_SIZE
    var playerThumbnailSizeL by Settings.PLAYER_LANDSCAPE_THUMBNAIL_SIZE
    var playerTimelineSize by Settings.PLAYER_TIMELINE_SIZE
    //

    var effectRotationEnabled by Settings.ROTATION_EFFECT
    var thumbnailTapEnabled by Settings.PLAYER_TAP_THUMBNAIL_FOR_LYRICS
    var showButtonPlayerAddToPlaylist by Settings.PLAYER_ACTION_ADD_TO_PLAYLIST
    var showButtonPlayerArrow by Settings.PLAYER_ACTION_OPEN_QUEUE_ARROW
    var showButtonPlayerDownload by Settings.PLAYER_ACTION_DOWNLOAD
    var showButtonPlayerLoop by Settings.PLAYER_ACTION_LOOP
    var showButtonPlayerLyrics by Settings.PLAYER_ACTION_SHOW_LYRICS
    var expandedplayertoggle by Settings.PLAYER_ACTION_TOGGLE_EXPAND
    var showButtonPlayerShuffle by Settings.PLAYER_ACTION_SHUFFLE
    var showButtonPlayerSleepTimer by Settings.PLAYER_ACTION_SLEEP_TIMER
    var showButtonPlayerMenu by Settings.PLAYER_ACTION_SHOW_MENU
    var showButtonPlayerStartradio by Settings.PLAYER_ACTION_START_RADIO
    var showButtonPlayerSystemEqualizer by Settings.PLAYER_ACTION_OPEN_EQUALIZER
    var showButtonPlayerDiscover by Settings.PLAYER_ACTION_DISCOVER
    var showButtonPlayerVideo by Settings.PLAYER_ACTION_TOGGLE_VIDEO
    val navigationBarPosition by Settings.NAVIGATION_BAR_POSITION
    //var isGradientBackgroundEnabled by rememberPreference(isGradientBackgroundEnabledKey, false)
    var showTotalTimeQueue by Settings.PLAYER_SHOW_TOTAL_QUEUE_TIME
    var backgroundProgress by Settings.MINI_PLAYER_PROGRESS_BAR
    var showNextSongsInPlayer by Settings.PLAYER_SHOW_NEXT_IN_QUEUE
    var showRemainingSongTime by Settings.PLAYER_SHOW_SONGS_REMAINING_TIME
    var clickLyricsText by Settings.LYRICS_JUMP_ON_TAP
    var showBackgroundLyrics by Settings.LYRICS_SHOW_ACCENT_BACKGROUND

    val search = Search()

    var thumbnailRoundness by Settings.THUMBNAIL_BORDER_RADIUS

    var miniPlayerType by Settings.MINI_PLAYER_TYPE
    var playerBackgroundColors by Settings.PLAYER_BACKGROUND

    var showTopActionsBar by Settings.PLAYER_SHOW_TOP_ACTIONS_BAR
    var playerControlsType by Settings.PLAYER_CONTROLS_TYPE
    var playerInfoType by Settings.PLAYER_INFO_TYPE
    var transparentBackgroundActionBarPlayer by Settings.PLAYER_TRANSPARENT_ACTIONS_BAR
    var iconLikeType by Settings.LIKE_ICON
    var playerSwapControlsWithTimeline by Settings.PLAYER_IS_CONTROL_AND_TIMELINE_SWAPPED
    var playerEnableLyricsPopupMessage by Settings.PLAYER_ACTION_LYRICS_POPUP_MESSAGE
    var actionspacedevenly by Settings.PLAYER_ACTION_BUTTONS_SPACED_EVENLY
    var thumbnailType by Settings.THUMBNAIL_TYPE
    var showvisthumbnail by Settings.PLAYER_SHOW_THUMBNAIL_ON_VISUALIZER
    var buttonzoomout by Settings.ZOOM_OUT_ANIMATION
    var thumbnailpause by Settings.PLAYER_SHRINK_THUMBNAIL_ON_PAUSE
    var showsongs by Settings.MAX_NUMBER_OF_NEXT_IN_QUEUE
    var showalbumcover by Settings.PLAYER_SHOW_NEXT_IN_QUEUE_THUMBNAIL
    var tapqueue by Settings.PLAYER_ACTIONS_BAR_TAP_TO_OPEN_QUEUE
    var swipeUpQueue by Settings.PLAYER_ACTIONS_BAR_SWIPE_UP_TO_OPEN_QUEUE
    var statsfornerds by Settings.PLAYER_STATS_FOR_NERDS

    var playerType by Settings.PLAYER_TYPE
    var queueType by Settings.QUEUE_TYPE
    var noblur by Settings.PLAYER_BACKGROUND_BLUR
    var fadingedge by Settings.PLAYER_BACKGROUND_FADING_EDGE
    var carousel by Settings.PLAYER_THUMBNAILS_CAROUSEL
    var carouselSize by Settings.CAROUSEL_SIZE
    var playerInfoShowIcons by Settings.PLAYER_SONG_INFO_ICON
    var queueDurationExpanded by Settings.PLAYER_IS_QUEUE_DURATION_EXPANDED
    var titleExpanded by Settings.PLAYER_IS_TITLE_EXPANDED
    var timelineExpanded by Settings.PLAYER_IS_TIMELINE_EXPANDED
    var controlsExpanded by Settings.PLAYER_IS_CONTROLS_EXPANDED
    var miniQueueExpanded by Settings.PLAYER_IS_NEXT_IN_QUEUE_EXPANDED
    var statsExpanded by Settings.PLAYER_IS_STATS_FOR_NERDS_EXPANDED
    var restartService by rememberSaveable { mutableStateOf(false) }
    var showCoverThumbnailAnimation by Settings.PLAYER_THUMBNAIL_ANIMATION
    var coverThumbnailAnimation by Settings.PLAYER_THUMBNAIL_TYPE

    var notificationPlayerFirstIcon by Settings.MEDIA_NOTIFICATION_FIRST_ICON
    var notificationPlayerSecondIcon by Settings.MEDIA_NOTIFICATION_SECOND_ICON
    var enableWallpaper by Settings.ENABLE_WALLPAPER
    var wallpaperType by Settings.WALLPAPER_TYPE
    var topPadding by Settings.PLAYER_TOP_PADDING
    var animatedGradient by Settings.ANIMATED_GRADIENT
    var appearanceChooser by remember{ mutableStateOf(false)}
    var albumCoverRotation by Settings.PLAYER_THUMBNAIL_ROTATION

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
            title = stringResource(R.string.player_appearance),
            iconId = R.drawable.color_palette,
            enabled = false,
            showIcon = true,
            modifier = Modifier,
            onClick = {}
        )

        search.ToolBarButton()
        search.SearchBar( this )

        //SettingsEntryGroupText(stringResource(R.string.user_interface))

        //SettingsGroupSpacer()
        SettingsEntryGroupText(title = stringResource(R.string.player))

        if (playerBackgroundColors != PlayerBackgroundColors.BlurredCoverColor)
            showthumbnail = true
        if (!visualizerEnabled) showvisthumbnail = false
        if (!showthumbnail) {showlyricsthumbnail = false; showvisthumbnail = false}
        if (playerType == PlayerType.Modern) {
            showlyricsthumbnail = false
            showvisthumbnail = false
            thumbnailpause = false
            //keepPlayerMinimized = false
        }
        var blurStrength by Settings.PLAYER_BACKGROUND_BLUR_STRENGTH
        var thumbnailFadeEx  by Settings.PLAYER_THUMBNAIL_FADE_EX
        var thumbnailFade  by Settings.PLAYER_THUMBNAIL_FADE
        var thumbnailSpacing  by Settings.PLAYER_THUMBNAIL_SPACING
        var colorPaletteName by Settings.COLOR_PALETTE
        var colorPaletteMode by Settings.THEME_MODE
        var swipeAnimationNoThumbnail by Settings.PLAYER_NO_THUMBNAIL_SWIPE_ANIMATION

        if (appearanceChooser){
            AppearancePresetDialog(
            onDismiss = {appearanceChooser = false},
            onClick0 = {
                showTopActionsBar = true
                showthumbnail = true
                playerBackgroundColors = PlayerBackgroundColors.BlurredCoverColor
                blurStrength = 50f
                thumbnailRoundness = ThumbnailRoundness.None
                playerInfoType = PlayerInfoType.Essential
                playerTimelineType = PlayerTimelineType.ThinBar
                playerTimelineSize = PlayerTimelineSize.Biggest
                playerControlsType = PlayerControlsType.Essential
                playerPlayButtonType = PlayerPlayButtonType.Disabled
                transparentbar = true
                playerType = PlayerType.Essential
                showlyricsthumbnail = false
                expandedplayer = true
                thumbnailType = ThumbnailType.Modern
                playerThumbnailSize = PlayerThumbnailSize.Big
                showTotalTimeQueue = false
                bottomgradient = true
                showRemainingSongTime = true
                showNextSongsInPlayer = false
                colorPaletteName = ColorPaletteName.Dynamic
                colorPaletteMode = ColorPaletteMode.System
                ///////ACTION BAR BUTTONS////////////////
                transparentBackgroundActionBarPlayer = true
                actionspacedevenly = true
                showButtonPlayerVideo = false
                showButtonPlayerDiscover = false
                showButtonPlayerDownload = false
                showButtonPlayerAddToPlaylist = true
                showButtonPlayerLoop = false
                showButtonPlayerShuffle = true
                showButtonPlayerLyrics = false
                expandedplayertoggle = false
                showButtonPlayerSleepTimer = false
                visualizerEnabled = false
                appearanceChooser = false
                showButtonPlayerArrow = false
                showButtonPlayerStartradio = false
                showButtonPlayerMenu = true
                ///////////////////////////
                appearanceChooser = false
            },
            onClick1 = {
                showTopActionsBar = true
                showthumbnail = true
                playerBackgroundColors = PlayerBackgroundColors.BlurredCoverColor
                blurStrength = 50f
                playerInfoType = PlayerInfoType.Essential
                playerPlayButtonType = PlayerPlayButtonType.Disabled
                playerTimelineType = PlayerTimelineType.ThinBar
                playerControlsType = PlayerControlsType.Essential
                transparentbar = true
                playerType = PlayerType.Modern
                expandedplayer = true
                fadingedge = true
                thumbnailFadeEx = 4f
                thumbnailSpacing = -32f
                thumbnailType = ThumbnailType.Essential
                carouselSize = CarouselSize.Big
                playerThumbnailSize = PlayerThumbnailSize.Biggest
                showTotalTimeQueue = false
                transparentBackgroundActionBarPlayer = true
                showRemainingSongTime = true
                bottomgradient = true
                showlyricsthumbnail = false
                thumbnailRoundness = ThumbnailRoundness.Medium
                showNextSongsInPlayer = true
                colorPaletteName = ColorPaletteName.Dynamic
                colorPaletteMode = ColorPaletteMode.System
                ///////ACTION BAR BUTTONS////////////////
                transparentBackgroundActionBarPlayer = true
                actionspacedevenly = true
                showButtonPlayerVideo = false
                showButtonPlayerDiscover = false
                showButtonPlayerDownload = false
                showButtonPlayerAddToPlaylist = true
                showButtonPlayerLoop = false
                showButtonPlayerShuffle = false
                showButtonPlayerLyrics = false
                expandedplayertoggle = true
                showButtonPlayerSleepTimer = false
                visualizerEnabled = false
                appearanceChooser = false
                showButtonPlayerArrow = false
                showButtonPlayerStartradio = false
                showButtonPlayerMenu = true
                ///////////////////////////
                appearanceChooser = false
            },
            onClick2 = {
                showTopActionsBar = false
                showthumbnail = false
                noblur = true
                topPadding = false
                playerBackgroundColors = PlayerBackgroundColors.BlurredCoverColor
                blurStrength = 50f
                playerPlayButtonType = PlayerPlayButtonType.Disabled
                playerInfoType = PlayerInfoType.Modern
                playerInfoShowIcons = false
                playerTimelineType = PlayerTimelineType.ThinBar
                playerControlsType = PlayerControlsType.Essential
                transparentbar = true
                playerType = PlayerType.Modern
                expandedplayer = true
                showTotalTimeQueue = false
                transparentBackgroundActionBarPlayer = true
                showRemainingSongTime = true
                bottomgradient = true
                showlyricsthumbnail = false
                showNextSongsInPlayer = false
                colorPaletteName = ColorPaletteName.Dynamic
                colorPaletteMode = ColorPaletteMode.System
                ///////ACTION BAR BUTTONS////////////////
                transparentBackgroundActionBarPlayer = true
                actionspacedevenly = true
                showButtonPlayerVideo = false
                showButtonPlayerDiscover = false
                showButtonPlayerDownload = false
                showButtonPlayerAddToPlaylist = false
                showButtonPlayerLoop = false
                showButtonPlayerShuffle = false
                showButtonPlayerLyrics = false
                expandedplayertoggle = false
                showButtonPlayerSleepTimer = false
                visualizerEnabled = false
                appearanceChooser = false
                showButtonPlayerArrow = false
                showButtonPlayerStartradio = false
                showButtonPlayerMenu = true
                ///////////////////////////
                appearanceChooser = false
            },
            onClick3 = {
                showTopActionsBar = false
                topPadding = false
                showthumbnail = true
                playerBackgroundColors = PlayerBackgroundColors.BlurredCoverColor
                blurStrength = 50f
                playerInfoType = PlayerInfoType.Essential
                playerTimelineType = PlayerTimelineType.FakeAudioBar
                playerTimelineSize = PlayerTimelineSize.Biggest
                playerControlsType = PlayerControlsType.Modern
                playerPlayButtonType = PlayerPlayButtonType.Disabled
                colorPaletteName = ColorPaletteName.PureBlack
                transparentbar = false
                playerType = PlayerType.Essential
                expandedplayer = false
                playerThumbnailSize = PlayerThumbnailSize.Expanded
                showTotalTimeQueue = false
                transparentBackgroundActionBarPlayer = true
                showRemainingSongTime = true
                bottomgradient = true
                showlyricsthumbnail = false
                thumbnailType = ThumbnailType.Essential
                thumbnailRoundness = ThumbnailRoundness.Light
                playerType = PlayerType.Modern
                fadingedge = true
                thumbnailFade = 5f
                showNextSongsInPlayer = false
                ///////ACTION BAR BUTTONS////////////////
                transparentBackgroundActionBarPlayer = true
                actionspacedevenly = true
                showButtonPlayerVideo = false
                showButtonPlayerDiscover = false
                showButtonPlayerDownload = false
                showButtonPlayerAddToPlaylist = false
                showButtonPlayerLoop = true
                showButtonPlayerShuffle = true
                showButtonPlayerLyrics = false
                expandedplayertoggle = false
                showButtonPlayerSleepTimer = false
                visualizerEnabled = false
                appearanceChooser = false
                showButtonPlayerArrow = true
                showButtonPlayerStartradio = false
                showButtonPlayerMenu = true
                ///////////////////////////
                appearanceChooser = false
            },
            onClick4 = {
                showTopActionsBar = false
                topPadding = true
                showthumbnail = true
                playerBackgroundColors = PlayerBackgroundColors.AnimatedGradient
                animatedGradient = AnimatedGradient.Linear
                playerInfoType = PlayerInfoType.Essential
                playerTimelineType = PlayerTimelineType.PinBar
                playerTimelineSize = PlayerTimelineSize.Biggest
                playerControlsType = PlayerControlsType.Essential
                playerPlayButtonType = PlayerPlayButtonType.Square
                colorPaletteName = ColorPaletteName.Dynamic
                colorPaletteMode = ColorPaletteMode.PitchBlack
                transparentbar = false
                playerType = PlayerType.Modern
                expandedplayer = false
                playerThumbnailSize = PlayerThumbnailSize.Biggest
                showTotalTimeQueue = false
                transparentBackgroundActionBarPlayer = true
                showRemainingSongTime = true
                showlyricsthumbnail = false
                thumbnailType = ThumbnailType.Modern
                thumbnailRoundness = ThumbnailRoundness.Heavy
                fadingedge = true
                thumbnailFade = 0f
                thumbnailFadeEx = 5f
                thumbnailSpacing = -32f
                showNextSongsInPlayer = false
                ///////ACTION BAR BUTTONS////////////////
                transparentBackgroundActionBarPlayer = true
                actionspacedevenly = true
                showButtonPlayerVideo = false
                showButtonPlayerDiscover = false
                showButtonPlayerDownload = true
                showButtonPlayerAddToPlaylist = false
                showButtonPlayerLoop = false
                showButtonPlayerShuffle = false
                showButtonPlayerLyrics = false
                expandedplayertoggle = true
                showButtonPlayerSleepTimer = false
                visualizerEnabled = false
                appearanceChooser = false
                showButtonPlayerArrow =false
                showButtonPlayerStartradio = false
                showButtonPlayerMenu = true
                ///////////////////////////
                appearanceChooser = false
            },
            onClick5 = {
                showTopActionsBar = true
                showthumbnail = true
                playerBackgroundColors = PlayerBackgroundColors.CoverColorGradient
                playerInfoType = PlayerInfoType.Essential
                playerTimelineType = PlayerTimelineType.Wavy
                playerTimelineSize = PlayerTimelineSize.Biggest
                playerControlsType = PlayerControlsType.Essential
                playerPlayButtonType = PlayerPlayButtonType.CircularRibbed
                colorPaletteName = ColorPaletteName.Dynamic
                colorPaletteMode = ColorPaletteMode.System
                transparentbar = false
                playerType = PlayerType.Essential
                expandedplayer = true
                playerThumbnailSize = PlayerThumbnailSize.Big
                showTotalTimeQueue = false
                transparentBackgroundActionBarPlayer = true
                showRemainingSongTime = true
                showlyricsthumbnail = false
                thumbnailType = ThumbnailType.Modern
                thumbnailRoundness = ThumbnailRoundness.Heavy
                showNextSongsInPlayer = false
                ///////ACTION BAR BUTTONS////////////////
                transparentBackgroundActionBarPlayer = true
                actionspacedevenly = true
                showButtonPlayerVideo = false
                showButtonPlayerDiscover = false
                showButtonPlayerDownload = false
                showButtonPlayerAddToPlaylist = false
                showButtonPlayerLoop = false
                showButtonPlayerShuffle = true
                showButtonPlayerLyrics = true
                expandedplayertoggle = false
                showButtonPlayerSleepTimer = false
                visualizerEnabled = false
                appearanceChooser = false
                showButtonPlayerArrow =false
                showButtonPlayerStartradio = false
                showButtonPlayerMenu = true
                ///////////////////////////
                appearanceChooser = false
            }
            )
        }

        if (!isLandscape) {
            Column {
                BasicText(
                    text = stringResource(R.string.appearancepresets),
                    style = typography().m.semiBold.copy(color = colorPalette().text),
                    modifier = Modifier
                        .padding(all = 12.dp)
                        .clickable(onClick = { appearanceChooser = true })
                )
                BasicText(
                    text = stringResource(R.string.appearancepresetssecondary),
                    style = typography().xs.semiBold.copy(color = colorPalette().textSecondary),
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .padding(bottom = 10.dp)
                )
            }

            if (search.inputValue.isBlank() || stringResource(R.string.show_player_top_actions_bar).contains(
                    search.inputValue,
                    true
                )
            )
                SwitchSettingEntry(
                    title = stringResource(R.string.show_player_top_actions_bar),
                    text = "",
                    isChecked = showTopActionsBar,
                    onCheckedChange = { showTopActionsBar = it }
                )

            if (!showTopActionsBar) {
                if (search.inputValue.isBlank() || stringResource(R.string.blankspace).contains(
                        search.inputValue,
                        true
                    )
                )
                    SwitchSettingEntry(
                        title = stringResource(R.string.blankspace),
                        text = "",
                        isChecked = topPadding,
                        onCheckedChange = { topPadding = it }
                    )
            }
        }
        if (search.inputValue.isBlank() || stringResource(R.string.playertype).contains(
                search.inputValue,
                true
            )
        )
            EnumValueSelectorSettingsEntry(
                title = stringResource(R.string.playertype),
                selectedValue = playerType,
                onValueSelected = {
                    playerType = it
                },
                valueText = { it.text },
            )

        if (search.inputValue.isBlank() || stringResource(R.string.queuetype).contains(
                search.inputValue,
                true
            )
        )
            EnumValueSelectorSettingsEntry(
                title = stringResource(R.string.queuetype),
                selectedValue = queueType,
                onValueSelected = {
                    queueType = it
                },
                valueText = { it.text },
            )

        if (playerBackgroundColors == PlayerBackgroundColors.BlurredCoverColor) {
            if (search.inputValue.isBlank() || stringResource(R.string.show_thumbnail).contains(
                    search.inputValue,
                    true
                )
            )
                SwitchSettingEntry(
                    title = stringResource(R.string.show_thumbnail),
                    text = "",
                    isChecked = showthumbnail,
                    onCheckedChange = {showthumbnail = it},
                )
        }
        AnimatedVisibility(visible = !showthumbnail && playerType == PlayerType.Modern && !isLandscape) {
            if (search.inputValue.isBlank() || stringResource(R.string.swipe_Animation_No_Thumbnail).contains(
                    search.inputValue,
                    true
                )
            )
                EnumValueSelectorSettingsEntry(
                    title = stringResource(R.string.swipe_Animation_No_Thumbnail),
                    selectedValue = swipeAnimationNoThumbnail,
                    onValueSelected = { swipeAnimationNoThumbnail = it },
                    valueText = {
                        when (it) {
                            SwipeAnimationNoThumbnail.Sliding -> stringResource(R.string.te_slide_vertical)
                            SwipeAnimationNoThumbnail.Fade -> stringResource(R.string.te_fade)
                            SwipeAnimationNoThumbnail.Scale -> stringResource(R.string.te_scale)
                            SwipeAnimationNoThumbnail.Carousel -> stringResource(R.string.carousel)
                            SwipeAnimationNoThumbnail.Circle -> stringResource(R.string.vt_circular)
                        }
                    },
                    modifier = Modifier.padding(start = if (playerBackgroundColors == PlayerBackgroundColors.BlurredCoverColor) 25.dp else 0.dp)
                )
        }
        AnimatedVisibility(visible = showthumbnail) {
            Column {
                if (playerType == PlayerType.Modern) {
                    if (search.inputValue.isBlank() || stringResource(R.string.fadingedge).contains(
                            search.inputValue,
                            true
                        )
                    )
                        SwitchSettingEntry(
                            title = stringResource(R.string.fadingedge),
                            text = "",
                            isChecked = fadingedge,
                            onCheckedChange = { fadingedge = it },
                            modifier = Modifier.padding(start = if (playerBackgroundColors == PlayerBackgroundColors.BlurredCoverColor) 25.dp else 0.dp)
                        )
                }

                if (playerType == PlayerType.Modern && !isLandscape && (expandedplayertoggle || expandedplayer)) {
                    if (search.inputValue.isBlank() || stringResource(R.string.carousel).contains(
                            search.inputValue,
                            true
                        )
                    )
                        SwitchSettingEntry(
                            title = stringResource(R.string.carousel),
                            text = "",
                            isChecked = carousel,
                            onCheckedChange = { carousel = it },
                            modifier = Modifier.padding(start = if (playerBackgroundColors == PlayerBackgroundColors.BlurredCoverColor) 25.dp else 0.dp)
                        )

                    if (search.inputValue.isBlank() || stringResource(R.string.carouselsize).contains(
                            search.inputValue,
                            true
                        )
                    )
                        EnumValueSelectorSettingsEntry(
                            title = stringResource(R.string.carouselsize),
                            selectedValue = carouselSize,
                            onValueSelected = { carouselSize = it },
                            valueText = { it.text },
                            modifier = Modifier.padding(start = if (playerBackgroundColors == PlayerBackgroundColors.BlurredCoverColor) 25.dp else 0.dp)
                        )
                }
                if (playerType == PlayerType.Essential) {

                    if (search.inputValue.isBlank() || stringResource(R.string.thumbnailpause).contains(
                            search.inputValue,
                            true
                        )
                    )
                        SwitchSettingEntry(
                            title = stringResource(R.string.thumbnailpause),
                            text = "",
                            isChecked = thumbnailpause,
                            onCheckedChange = { thumbnailpause = it },
                            modifier = Modifier.padding(start = if (playerBackgroundColors == PlayerBackgroundColors.BlurredCoverColor) 25.dp else 0.dp)
                        )

                    if (search.inputValue.isBlank() || stringResource(R.string.show_lyrics_thumbnail).contains(
                            search.inputValue,
                            true
                        )
                    )
                        SwitchSettingEntry(
                            title = stringResource(R.string.show_lyrics_thumbnail),
                            text = "",
                            isChecked = showlyricsthumbnail,
                            onCheckedChange = { showlyricsthumbnail = it },
                            modifier = Modifier.padding(start = if (playerBackgroundColors == PlayerBackgroundColors.BlurredCoverColor) 25.dp else 0.dp)
                        )
                    if (visualizerEnabled) {
                        if (search.inputValue.isBlank() || stringResource(R.string.showvisthumbnail).contains(
                                search.inputValue,
                                true
                            )
                        )
                            SwitchSettingEntry(
                                title = stringResource(R.string.showvisthumbnail),
                                text = "",
                                isChecked = showvisthumbnail,
                                onCheckedChange = { showvisthumbnail = it },
                                modifier = Modifier.padding(start = if (playerBackgroundColors == PlayerBackgroundColors.BlurredCoverColor) 25.dp else 0.dp)
                            )
                    }
                }

                if (search.inputValue.isBlank() || stringResource(R.string.show_cover_thumbnail_animation).contains(
                        search.inputValue,
                        true
                    )
                ) {
                    SwitchSettingEntry(
                        title = stringResource(R.string.show_cover_thumbnail_animation),
                        text = "",
                        isChecked = showCoverThumbnailAnimation,
                        onCheckedChange = { showCoverThumbnailAnimation = it },
                        modifier = Modifier.padding(start = if (playerBackgroundColors == PlayerBackgroundColors.BlurredCoverColor) 25.dp else 0.dp)
                    )
                    AnimatedVisibility(visible = showCoverThumbnailAnimation) {
                        Column {
                            EnumValueSelectorSettingsEntry(
                                title = stringResource(R.string.cover_thumbnail_animation_type),
                                selectedValue = coverThumbnailAnimation,
                                onValueSelected = { coverThumbnailAnimation = it },
                                valueText = { it.text },
                                modifier = Modifier.padding(start = if (playerBackgroundColors == PlayerBackgroundColors.BlurredCoverColor) 50.dp else 25.dp)
                            )
                        }
                    }
                }

                if (isLandscape) {
                    if (search.inputValue.isBlank() || stringResource(R.string.player_thumbnail_size).contains(
                            search.inputValue,
                            true
                        )
                    )
                        EnumValueSelectorSettingsEntry(
                            title = stringResource(R.string.player_thumbnail_size),
                            selectedValue = playerThumbnailSizeL,
                            onValueSelected = { playerThumbnailSizeL = it },
                            valueText = { it.text },
                            modifier = Modifier.padding(start = if (playerBackgroundColors == PlayerBackgroundColors.BlurredCoverColor) 25.dp else 0.dp)
                        )
                } else {
                    if (search.inputValue.isBlank() || stringResource(R.string.player_thumbnail_size).contains(
                            search.inputValue,
                            true
                        )
                    )
                        EnumValueSelectorSettingsEntry(
                            title = stringResource(R.string.player_thumbnail_size),
                            selectedValue = playerThumbnailSize,
                            onValueSelected = { playerThumbnailSize = it },
                            valueText = { it.text },
                            modifier = Modifier.padding(start = if (playerBackgroundColors == PlayerBackgroundColors.BlurredCoverColor) 25.dp else 0.dp)
                        )
                }
                if (search.inputValue.isBlank() || stringResource(R.string.thumbnailtype).contains(
                        search.inputValue,
                        true
                    )
                )
                    EnumValueSelectorSettingsEntry(
                        title = stringResource(R.string.thumbnailtype),
                        selectedValue = thumbnailType,
                        onValueSelected = {
                            thumbnailType = it
                        },
                        valueText = { it.text },
                        modifier = Modifier.padding(start = if (playerBackgroundColors == PlayerBackgroundColors.BlurredCoverColor) 25.dp else 0.dp)
                    )

                if (search.inputValue.isBlank() || stringResource(R.string.thumbnail_roundness).contains(
                        search.inputValue,
                        true
                    )
                )
                    EnumValueSelectorSettingsEntry(
                        title = stringResource(R.string.thumbnail_roundness),
                        selectedValue = thumbnailRoundness,
                        onValueSelected = { thumbnailRoundness = it },
                        trailingContent = {
                            Spacer(
                                modifier = Modifier
                                    .border(
                                        width = 1.dp,
                                        color = colorPalette().accent,
                                        shape = thumbnailRoundness.shape
                                    )
                                    .background(
                                        color = colorPalette().background1,
                                        shape = thumbnailRoundness.shape
                                    )
                                    .size(36.dp)
                            )
                        },
                        valueText = { it.text },
                        modifier = Modifier.padding(start = if (playerBackgroundColors == PlayerBackgroundColors.BlurredCoverColor) 25.dp else 0.dp)
                    )
            }
        }

        if (!showthumbnail) {
            if (search.inputValue.isBlank() || stringResource(R.string.noblur).contains(
                    search.inputValue,
                    true
                )
            )
                SwitchSettingEntry(
                    title = stringResource(R.string.noblur),
                    text = "",
                    isChecked = noblur,
                    onCheckedChange = { noblur = it }
                )


        }

        if (!(showthumbnail && playerType == PlayerType.Essential)){
            if (search.inputValue.isBlank() || stringResource(R.string.statsfornerdsplayer).contains(
                    search.inputValue,
                    true
                )
            )
                SwitchSettingEntry(
                    title = stringResource(R.string.statsfornerdsplayer),
                    text = "",
                    isChecked = statsfornerds,
                    onCheckedChange = { statsfornerds = it }
                )
        }

        if (search.inputValue.isBlank() || stringResource(R.string.timelinesize).contains(
                search.inputValue,
                true
            )
        )
            EnumValueSelectorSettingsEntry(
                title = stringResource(R.string.timelinesize),
                selectedValue = playerTimelineSize,
                onValueSelected = { playerTimelineSize = it },
                valueText = { it.text }
            )

        if (search.inputValue.isBlank() || stringResource(R.string.pinfo_type).contains(
                search.inputValue,
                true
            )
        ) {
            EnumValueSelectorSettingsEntry(
                title = stringResource(R.string.pinfo_type),
                selectedValue = playerInfoType,
                onValueSelected = {
                    playerInfoType = it
                },
                valueText = { it.text },
            )
            SettingsDescription(text = stringResource(R.string.pinfo_album_and_artist_name))

            AnimatedVisibility( visible = playerInfoType == PlayerInfoType.Modern) {
                Column {
                    if (search.inputValue.isBlank() || stringResource(R.string.pinfo_show_icons).contains(
                            search.inputValue,
                            true
                        )
                    )
                        SwitchSettingEntry(
                            title = stringResource(R.string.pinfo_show_icons),
                            text = "",
                            isChecked = playerInfoShowIcons,
                            onCheckedChange = { playerInfoShowIcons = it },
                            modifier = Modifier
                                .padding(start = 25.dp)
                        )
                }
            }

        }



        if (search.inputValue.isBlank() || stringResource(R.string.miniplayertype).contains(
                search.inputValue,
                true
            )
        )
            EnumValueSelectorSettingsEntry(
                title = stringResource(R.string.miniplayertype),
                selectedValue = miniPlayerType,
                onValueSelected = {
                    miniPlayerType = it
                },
                valueText = { it.text },
            )

        if (search.inputValue.isBlank() || stringResource(R.string.player_swap_controls_with_timeline).contains(
                search.inputValue,
                true
            )
        )
            SwitchSettingEntry(
                title = stringResource(R.string.player_swap_controls_with_timeline),
                text = "",
                isChecked = playerSwapControlsWithTimeline,
                onCheckedChange = { playerSwapControlsWithTimeline = it }
            )

        if (search.inputValue.isBlank() || stringResource(R.string.timeline).contains(
                search.inputValue,
                true
            )
        )
            EnumValueSelectorSettingsEntry(
                title = stringResource(R.string.timeline),
                selectedValue = playerTimelineType,
                onValueSelected = { playerTimelineType = it },
                valueText = { it.text }
            )

        if (search.inputValue.isBlank() || stringResource(R.string.transparentbar).contains(
                search.inputValue,
                true
            )
        )
            SwitchSettingEntry(
                title = stringResource(R.string.transparentbar),
                text = "",
                isChecked = transparentbar,
                onCheckedChange = { transparentbar = it }
            )

        if (search.inputValue.isBlank() || stringResource(R.string.pcontrols_type).contains(
                search.inputValue,
                true
            )
        )
            EnumValueSelectorSettingsEntry(
                title = stringResource(R.string.pcontrols_type),
                selectedValue = playerControlsType,
                onValueSelected = {
                    playerControlsType = it
                },
                valueText = { it.text }
            )


        if (search.inputValue.isBlank() || stringResource(R.string.play_button).contains(
                search.inputValue,
                true
            )
        )
            EnumValueSelectorSettingsEntry(
                title = stringResource(R.string.play_button),
                selectedValue = playerPlayButtonType,
                onValueSelected = {
                    playerPlayButtonType = it
                },
                valueText = { it.text }
            )

        if (search.inputValue.isBlank() || stringResource(R.string.buttonzoomout).contains(
                search.inputValue,
                true
            )
        )
            SwitchSettingEntry(
                title = stringResource(R.string.buttonzoomout),
                text = "",
                isChecked = buttonzoomout,
                onCheckedChange = { buttonzoomout = it }
            )


        if (search.inputValue.isBlank() || stringResource(R.string.play_button).contains(
                search.inputValue,
                true
            )
        )
            EnumValueSelectorSettingsEntry(
                title = stringResource(R.string.icon_like_button),
                selectedValue = iconLikeType,
                onValueSelected = {
                    iconLikeType = it
                },
                valueText = { it.text },
            )

        /*

        if (filter.isNullOrBlank() || stringResource(R.string.use_gradient_background).contains(filterCharSequence,true))
            SwitchSettingEntry(
                title = stringResource(R.string.use_gradient_background),
                text = "",
                isChecked = isGradientBackgroundEnabled,
                onCheckedChange = { isGradientBackgroundEnabled = it }
            )
         */

        if (search.inputValue.isBlank() || stringResource(R.string.background_colors).contains(
                search.inputValue,
                true
            )
        )
            EnumValueSelectorSettingsEntry(
                title = stringResource(R.string.background_colors),
                selectedValue = playerBackgroundColors,
                onValueSelected = {
                    playerBackgroundColors = it
                },
                valueText = { it.text }
            )

        AnimatedVisibility(visible = playerBackgroundColors == PlayerBackgroundColors.AnimatedGradient) {
            if (search.inputValue.isBlank() || stringResource(R.string.gradienttype).contains(
                    search.inputValue,
                    true
                )
            )
                EnumValueSelectorSettingsEntry(
                    title = stringResource(R.string.gradienttype),
                    selectedValue = animatedGradient,
                    onValueSelected = {
                        animatedGradient = it
                    },
                    valueText = { it.text },
                    modifier = Modifier.padding(start = if (playerBackgroundColors == PlayerBackgroundColors.AnimatedGradient) 25.dp else 0.dp)
                )
        }
        var isRotatingCoverEnabled by Settings.PLAYER_ROTATING_ALBUM_COVER
        AnimatedVisibility( playerBackgroundColors == PlayerBackgroundColors.BlurredCoverColor ) {
            if ( search.inputValue.isBlank() || stringResource( R.string.rotating_cover_title ).contains(search.inputValue, true) )
                SwitchSettingEntry(
                    title = stringResource( R.string.rotating_cover_title ),
                    text = "",
                    isChecked = isRotatingCoverEnabled,
                    onCheckedChange = { isRotatingCoverEnabled = it },
                    modifier = Modifier.padding( start = 25.dp )
                )
        }


        if ((playerBackgroundColors == PlayerBackgroundColors.CoverColorGradient) || (playerBackgroundColors == PlayerBackgroundColors.ThemeColorGradient))
            if (search.inputValue.isBlank() || stringResource(R.string.blackgradient).contains(
                    search.inputValue,
                    true
                )
            )
                SwitchSettingEntry(
                    title = stringResource(R.string.blackgradient),
                    text = "",
                    isChecked = blackgradient,
                    onCheckedChange = { blackgradient = it }
                )

        if ((playerBackgroundColors == PlayerBackgroundColors.BlurredCoverColor) && (playerType == PlayerType.Modern))
            if (search.inputValue.isBlank() || stringResource(R.string.albumCoverRotation).contains(
                    search.inputValue,
                    true
                )
            )
                SwitchSettingEntry(
                    title = stringResource(R.string.albumCoverRotation),
                    text = "",
                    isChecked = albumCoverRotation,
                    onCheckedChange = { albumCoverRotation = it },
                    modifier = Modifier
                        .padding(start = 25.dp)
                )

        if (playerBackgroundColors == PlayerBackgroundColors.BlurredCoverColor)
            if (search.inputValue.isBlank() || stringResource(R.string.bottomgradient).contains(
                    search.inputValue,
                    true
                )
            )
                SwitchSettingEntry(
                    title = stringResource(R.string.bottomgradient),
                    text = "",
                    isChecked = bottomgradient,
                    onCheckedChange = { bottomgradient = it }
                )
        if (search.inputValue.isBlank() || stringResource(R.string.textoutline).contains(
              search.inputValue,
              true
              )
         )
             SwitchSettingEntry(
                 title = stringResource(R.string.textoutline),
                 text = "",
                 isChecked = textoutline,
                 onCheckedChange = { textoutline = it }
             )

       if (search.inputValue.isBlank() || stringResource(R.string.show_total_time_of_queue).contains(
                search.inputValue,
                true
            )
        )
            SwitchSettingEntry(
                title = stringResource(R.string.show_total_time_of_queue),
                text = "",
                isChecked = showTotalTimeQueue,
                onCheckedChange = { showTotalTimeQueue = it }
            )

        if (search.inputValue.isBlank() || stringResource(R.string.show_remaining_song_time).contains(
                search.inputValue,
                true
            )
        )
            SwitchSettingEntry(
                title = stringResource(R.string.show_remaining_song_time),
                text = "",
                isChecked = showRemainingSongTime,
                onCheckedChange = { showRemainingSongTime = it }
            )

        if (search.inputValue.isBlank() || stringResource(R.string.show_next_songs_in_player).contains(
                search.inputValue,
                true
            )
        )
            SwitchSettingEntry(
                title = stringResource(R.string.show_next_songs_in_player),
                text = "",
                isChecked = showNextSongsInPlayer,
                onCheckedChange = { showNextSongsInPlayer = it }
            )
        AnimatedVisibility( visible = showNextSongsInPlayer) {
          Column {
              if (search.inputValue.isBlank() || stringResource(R.string.showtwosongs).contains(search.inputValue,true))
                  EnumValueSelectorSettingsEntry(
                      title = stringResource(R.string.songs_number_to_show),
                      selectedValue = showsongs,
                      onValueSelected = {
                          showsongs = it
                      },
                      valueText = { it.name },
                      modifier = Modifier
                          .padding(start = 25.dp)
                  )


            if (search.inputValue.isBlank() || stringResource(R.string.showalbumcover).contains(
                    search.inputValue,
                    true
                )
            )
                SwitchSettingEntry(
                    title = stringResource(R.string.showalbumcover),
                    text = "",
                    isChecked = showalbumcover,
                    onCheckedChange = { showalbumcover = it },
                      modifier = Modifier.padding(start = 25.dp)
                  )
          }
        }

        if (search.inputValue.isBlank() || stringResource(R.string.disable_scrolling_text).contains(
                search.inputValue,
                true
            )
        )
            SwitchSettingEntry(
                title = stringResource(R.string.disable_scrolling_text),
                text = stringResource(R.string.scrolling_text_is_used_for_long_texts),
                isChecked = disableScrollingText,
                onCheckedChange = { disableScrollingText = it }
            )

        if (search.inputValue.isBlank() || stringResource(if (playerType == PlayerType.Modern && !isLandscape) R.string.disable_horizontal_swipe else R.string.disable_vertical_swipe).contains(
                search.inputValue,
                true
            )
        )
            SwitchSettingEntry(
                title = stringResource(if (playerType == PlayerType.Modern && !isLandscape) R.string.disable_vertical_swipe else R.string.disable_horizontal_swipe),
                text = stringResource(if (playerType == PlayerType.Modern && !isLandscape) R.string.disable_vertical_swipe_secondary else R.string.disable_song_switching_via_swipe),
                isChecked = disablePlayerHorizontalSwipe,
                onCheckedChange = { disablePlayerHorizontalSwipe = it }
            )

        if (search.inputValue.isBlank() || stringResource(R.string.player_rotating_buttons).contains(
                search.inputValue,
                true
            )
        )
            SwitchSettingEntry(
                title = stringResource(R.string.player_rotating_buttons),
                text = stringResource(R.string.player_enable_rotation_buttons),
                isChecked = effectRotationEnabled,
                onCheckedChange = { effectRotationEnabled = it }
            )

        if (search.inputValue.isBlank() || stringResource(R.string.toggle_lyrics).contains(
                search.inputValue,
                true
            )
        )
            SwitchSettingEntry(
                title = stringResource(R.string.toggle_lyrics),
                text = stringResource(R.string.by_tapping_on_the_thumbnail),
                isChecked = thumbnailTapEnabled,
                onCheckedChange = { thumbnailTapEnabled = it }
            )

        if (search.inputValue.isBlank() || stringResource(R.string.click_lyrics_text).contains(
                search.inputValue,
                true
            )
        )
            SwitchSettingEntry(
                title = stringResource(R.string.click_lyrics_text),
                text = "",
                isChecked = clickLyricsText,
                onCheckedChange = { clickLyricsText = it }
            )
        if (showlyricsthumbnail)
            if (search.inputValue.isBlank() || stringResource(R.string.show_background_in_lyrics).contains(
                    search.inputValue,
                    true
                )
            )
                SwitchSettingEntry(
                    title = stringResource(R.string.show_background_in_lyrics),
                    text = "",
                    isChecked = showBackgroundLyrics,
                    onCheckedChange = { showBackgroundLyrics = it }
                )

        if (search.inputValue.isBlank() || stringResource(R.string.player_enable_lyrics_popup_message).contains(
                search.inputValue,
                true
            )
        )
            SwitchSettingEntry(
                title = stringResource(R.string.player_enable_lyrics_popup_message),
                text = "",
                isChecked = playerEnableLyricsPopupMessage,
                onCheckedChange = { playerEnableLyricsPopupMessage = it }
            )

        if (search.inputValue.isBlank() || stringResource(R.string.background_progress_bar).contains(
                search.inputValue,
                true
            )
        )
            EnumValueSelectorSettingsEntry(
                title = stringResource(R.string.background_progress_bar),
                selectedValue = backgroundProgress,
                onValueSelected = {
                    backgroundProgress = it
                },
                valueText = { it.text },
            )


        if (search.inputValue.isBlank() || stringResource(R.string.visualizer).contains(
                search.inputValue,
                true
            )
        ) {
            SwitchSettingEntry(
                title = stringResource(R.string.visualizer),
                text = "",
                isChecked = visualizerEnabled,
                onCheckedChange = { visualizerEnabled = it }
            )
            /*
            EnumValueSelectorSettingsEntry(
                title = stringResource(R.string.visualizer),
                selectedValue = playerVisualizerType,
                onValueSelected = { playerVisualizerType = it },
                valueText = {
                    when (it) {
                        PlayerVisualizerType.Fancy -> stringResource(R.string.vt_fancy)
                        PlayerVisualizerType.Circular -> stringResource(R.string.vt_circular)
                        PlayerVisualizerType.Disabled -> stringResource(R.string.vt_disabled)
                        PlayerVisualizerType.Stacked -> stringResource(R.string.vt_stacked)
                        PlayerVisualizerType.Oneside -> stringResource(R.string.vt_one_side)
                        PlayerVisualizerType.Doubleside -> stringResource(R.string.vt_double_side)
                        PlayerVisualizerType.DoublesideCircular -> stringResource(R.string.vt_double_side_circular)
                        PlayerVisualizerType.Full -> stringResource(R.string.vt_full)
                    }
                }
            )
            */
            ImportantSettingsDescription(text = stringResource(R.string.visualizer_require_mic_permission))
        }

        SettingsGroupSpacer()
        SettingsEntryGroupText(title = stringResource(R.string.player_action_bar))

        if (search.inputValue.isBlank() || stringResource(R.string.action_bar_transparent_background).contains(
                search.inputValue,
                true
            )
        )
            SwitchSettingEntry(
                title = stringResource(R.string.action_bar_transparent_background),
                text = "",
                isChecked = transparentBackgroundActionBarPlayer,
                onCheckedChange = { transparentBackgroundActionBarPlayer = it }
            )

        if (search.inputValue.isBlank() || stringResource(R.string.actionspacedevenly).contains(
                search.inputValue,
                true
            )
        )
            SwitchSettingEntry(
                title = stringResource(R.string.actionspacedevenly),
                text = "",
                isChecked = actionspacedevenly,
                onCheckedChange = { actionspacedevenly = it }
            )

        if (search.inputValue.isBlank() || stringResource(R.string.tapqueue).contains(
                search.inputValue,
                true
            )
        )
            SwitchSettingEntry(
                title = stringResource(R.string.tapqueue),
                text = "",
                isChecked = tapqueue,
                onCheckedChange = { tapqueue = it }
            )

        if (search.inputValue.isBlank() || stringResource(R.string.swipe_up_to_open_the_queue).contains(
                search.inputValue,
                true
            )
        )
            SwitchSettingEntry(
                title = stringResource(R.string.swipe_up_to_open_the_queue),
                text = "",
                isChecked = swipeUpQueue,
                onCheckedChange = { swipeUpQueue = it }
            )

        if (search.inputValue.isBlank() || stringResource(R.string.action_bar_show_video_button).contains(
                search.inputValue,
                true
            )
        )
            SwitchSettingEntry(
                title = stringResource(R.string.action_bar_show_video_button),
                text = "",
                isChecked = showButtonPlayerVideo,
                onCheckedChange = { showButtonPlayerVideo = it }
            )

        if (search.inputValue.isBlank() || stringResource(R.string.action_bar_show_discover_button).contains(
                search.inputValue,
                true
            )
        )
            SwitchSettingEntry(
                title = stringResource(R.string.action_bar_show_discover_button),
                text = "",
                isChecked = showButtonPlayerDiscover,
                onCheckedChange = { showButtonPlayerDiscover = it }
            )

        if (search.inputValue.isBlank() || stringResource(R.string.action_bar_show_download_button).contains(
                search.inputValue,
                true
            )
        )
            SwitchSettingEntry(
                title = stringResource(R.string.action_bar_show_download_button),
                text = "",
                isChecked = showButtonPlayerDownload,
                onCheckedChange = { showButtonPlayerDownload = it }
            )

        if (search.inputValue.isBlank() || stringResource(R.string.action_bar_show_add_to_playlist_button).contains(
                search.inputValue,
                true
            )
        )
            SwitchSettingEntry(
                title = stringResource(R.string.action_bar_show_add_to_playlist_button),
                text = "",
                isChecked = showButtonPlayerAddToPlaylist,
                onCheckedChange = { showButtonPlayerAddToPlaylist = it }
            )

        if (search.inputValue.isBlank() || stringResource(R.string.action_bar_show_loop_button).contains(
                search.inputValue,
                true
            )
        )
            SwitchSettingEntry(
                title = stringResource(R.string.action_bar_show_loop_button),
                text = "",
                isChecked = showButtonPlayerLoop,
                onCheckedChange = { showButtonPlayerLoop = it }
            )

        if (search.inputValue.isBlank() || stringResource(R.string.action_bar_show_shuffle_button).contains(
                search.inputValue,
                true
            )
        )
            SwitchSettingEntry(
                title = stringResource(R.string.action_bar_show_shuffle_button),
                text = "",
                isChecked = showButtonPlayerShuffle,
                onCheckedChange = { showButtonPlayerShuffle = it }
            )

        if (search.inputValue.isBlank() || stringResource(R.string.action_bar_show_lyrics_button).contains(
                search.inputValue,
                true
            )
        )
            SwitchSettingEntry(
                title = stringResource(R.string.action_bar_show_lyrics_button),
                text = "",
                isChecked = showButtonPlayerLyrics,
                onCheckedChange = { showButtonPlayerLyrics = it }
            )
        if (!isLandscape || !showthumbnail) {
            if (!showlyricsthumbnail) {
                if (search.inputValue.isBlank() || stringResource(R.string.expandedplayer).contains(
                        search.inputValue,
                        true
                    )
                )
                    SwitchSettingEntry(
                        title = stringResource(R.string.expandedplayer),
                        text = "",
                        isChecked = expandedplayertoggle,
                        onCheckedChange = { expandedplayertoggle = it }
                    )
            }
        }

        if (search.inputValue.isBlank() || stringResource(R.string.action_bar_show_sleep_timer_button).contains(
                search.inputValue,
                true
            )
        )
            SwitchSettingEntry(
                title = stringResource(R.string.action_bar_show_sleep_timer_button),
                text = "",
                isChecked = showButtonPlayerSleepTimer,
                onCheckedChange = { showButtonPlayerSleepTimer = it }
            )

        if (search.inputValue.isBlank() || stringResource(R.string.show_equalizer).contains(
                search.inputValue,
                true
            )
        )
            SwitchSettingEntry(
                title = stringResource(R.string.show_equalizer),
                text = "",
                isChecked = showButtonPlayerSystemEqualizer,
                onCheckedChange = { showButtonPlayerSystemEqualizer = it }
            )

        if (search.inputValue.isBlank() || stringResource(R.string.action_bar_show_arrow_button_to_open_queue).contains(
                search.inputValue,
                true
            )
        )
            SwitchSettingEntry(
                title = stringResource(R.string.action_bar_show_arrow_button_to_open_queue),
                text = "",
                isChecked = showButtonPlayerArrow,
                onCheckedChange = { showButtonPlayerArrow = it }
            )

        if (search.inputValue.isBlank() || stringResource(R.string.action_bar_show_start_radio_button).contains(
                search.inputValue,
                true
            )
        )
            SwitchSettingEntry(
                title = stringResource(R.string.action_bar_show_start_radio_button),
                text = "",
                isChecked = showButtonPlayerStartradio,
                onCheckedChange = { showButtonPlayerStartradio = it }
            )

        if (search.inputValue.isBlank() || stringResource(R.string.action_bar_show_menu_button).contains(
                search.inputValue,
                true
            )
        )
            SwitchSettingEntry(
                title = stringResource(R.string.action_bar_show_menu_button),
                text = "",
                isChecked = showButtonPlayerMenu,
                onCheckedChange = { showButtonPlayerMenu = it }
            )

        if (!showlyricsthumbnail) {
            SettingsGroupSpacer()
            SettingsEntryGroupText(title = stringResource(R.string.full_screen_lyrics_components))

            if (showTotalTimeQueue) {
                if (search.inputValue.isBlank() || stringResource(R.string.show_total_time_of_queue).contains(
                        search.inputValue,
                        true
                    )
                )
                    SwitchSettingEntry(
                        title = stringResource(R.string.show_total_time_of_queue),
                        text = "",
                        isChecked = queueDurationExpanded,
                        onCheckedChange = { queueDurationExpanded = it }
                    )
            }

            if (search.inputValue.isBlank() || stringResource(R.string.titleartist).contains(
                    search.inputValue,
                    true
                )
            )
                SwitchSettingEntry(
                    title = stringResource(R.string.titleartist),
                    text = "",
                    isChecked = titleExpanded,
                    onCheckedChange = { titleExpanded = it }
                )

            if (search.inputValue.isBlank() || stringResource(R.string.timeline).contains(
                    search.inputValue,
                    true
                )
            )
                SwitchSettingEntry(
                    title = stringResource(R.string.timeline),
                    text = "",
                    isChecked = timelineExpanded,
                    onCheckedChange = { timelineExpanded = it }
                )

            if (search.inputValue.isBlank() || stringResource(R.string.controls).contains(
                    search.inputValue,
                    true
                )
            )
                SwitchSettingEntry(
                    title = stringResource(R.string.controls),
                    text = "",
                    isChecked = controlsExpanded,
                    onCheckedChange = { controlsExpanded = it }
                )

            if (statsfornerds && (!(showthumbnail && playerType == PlayerType.Essential))){
                if (search.inputValue.isBlank() || stringResource(R.string.statsfornerds).contains(
                        search.inputValue,
                        true
                    )
                )
                    SwitchSettingEntry(
                        title = stringResource(R.string.statsfornerds),
                        text = "",
                        isChecked = statsExpanded,
                        onCheckedChange = { statsExpanded = it }
                    )
            }

            var isActionsBarExpanded by Settings.PLAYER_IS_ACTIONS_BAR_EXPANDED
            if (
                showButtonPlayerDownload ||
                showButtonPlayerAddToPlaylist ||
                showButtonPlayerLoop ||
                showButtonPlayerShuffle ||
                showButtonPlayerLyrics ||
                showButtonPlayerSleepTimer ||
                showButtonPlayerSystemEqualizer ||
                showButtonPlayerArrow ||
                showButtonPlayerMenu ||
                expandedplayertoggle ||
                showButtonPlayerDiscover ||
                showButtonPlayerVideo
            ){

                if (search.inputValue.isBlank() || stringResource(R.string.actionbar).contains(
                        search.inputValue,
                        true
                    )
                )
                    SwitchSettingEntry(
                        title = stringResource(R.string.actionbar),
                        text = "",
                        isChecked = isActionsBarExpanded,
                        onCheckedChange = {
                            isActionsBarExpanded = it
                        }
                    )
            }
            if (showNextSongsInPlayer && isActionsBarExpanded) {
                if (search.inputValue.isBlank() || stringResource(R.string.miniqueue).contains(
                        search.inputValue,
                        true
                    )
                )
                    SwitchSettingEntry(
                        title = stringResource(R.string.miniqueue),
                        text = "",
                        isChecked = miniQueueExpanded,
                        onCheckedChange = { miniQueueExpanded = it }
                    )
            }

        }

        var showPlaybackSpeedButton by Settings.AUDIO_SPEED
        if( search.inputValue.isBlank() || stringResource( R.string.title_playback_speed ).contains( search.inputValue, true ) )
            SwitchSettingEntry(
                title = stringResource( R.string.title_playback_speed ),
                text = stringResource( R.string.description_playback_speed ),
                isChecked = showPlaybackSpeedButton,
                onCheckedChange = { showPlaybackSpeedButton = it }
            )

        SettingsGroupSpacer()
        SettingsEntryGroupText(title = stringResource(R.string.notification_player))

        if (search.inputValue.isBlank() || stringResource(R.string.notification_player).contains(
                search.inputValue,
                true
            )
        ) {
            EnumValueSelectorSettingsEntry(
                title = stringResource(R.string.notificationPlayerFirstIcon),
                selectedValue = notificationPlayerFirstIcon,
                onValueSelected = {
                    notificationPlayerFirstIcon = it
                    restartService = true
                },
                valueText = { it.text },
            )
            EnumValueSelectorSettingsEntry(
                title = stringResource(R.string.notificationPlayerSecondIcon),
                selectedValue = notificationPlayerSecondIcon,
                onValueSelected = {
                    notificationPlayerSecondIcon = it
                    restartService = true
                },
                valueText = { it.text },
            )
            RestartPlayerService(restartService, onRestart = { restartService = false })
        }


//        if (search.inputValue.isBlank() || stringResource(R.string.show_song_cover).contains(
//                search.inputValue,
//                true
//            )
//        )
//            if (!isAtLeastAndroid13) {
//                SettingsGroupSpacer()
//
//                SettingsEntryGroupText(title = stringResource(R.string.lockscreen))
//
//                SwitchSettingEntry(
//                    title = stringResource(R.string.show_song_cover),
//                    text = stringResource(R.string.use_song_cover_on_lockscreen),
//                    isChecked = isShowingThumbnailInLockscreen,
//                    onCheckedChange = { isShowingThumbnailInLockscreen = it }
//                )
//            }

        if (isAtLeastAndroid7) {
            SettingsGroupSpacer()
            SettingsEntryGroupText(title = stringResource(R.string.wallpaper))
            SwitchSettingEntry(
                title = stringResource(R.string.enable_wallpaper),
                text = "",
                isChecked = enableWallpaper,
                onCheckedChange = { enableWallpaper = it }
            )
            AnimatedVisibility(visible = enableWallpaper) {
                Column {
                    EnumValueSelectorSettingsEntry(
                        title = stringResource(R.string.set_cover_thumbnail_as_wallpaper),
                        selectedValue = wallpaperType,
                        onValueSelected = {
                            wallpaperType = it
                            restartService = true
                        },
                        valueText = { it.text },
                        modifier = Modifier.padding(start = 25.dp)
                    )
                    RestartPlayerService(restartService, onRestart = { restartService = false })
                }
            }
        }

        SettingsGroupSpacer()
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
            DefaultAppearanceSettings()
            resetToDefault = false
            navController.popBackStack()
            Toaster.done()
        }

        SettingsGroupSpacer(
            modifier = Modifier.height(Dimensions.bottomSpacer)
        )
    }
}
