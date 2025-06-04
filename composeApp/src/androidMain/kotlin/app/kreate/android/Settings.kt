package app.kreate.android

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SnapshotMutationPolicy
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.core.content.edit
import it.fast4x.rimusic.appContext
import it.fast4x.rimusic.enums.AlbumSortBy
import it.fast4x.rimusic.enums.AlbumSwipeAction
import it.fast4x.rimusic.enums.AlbumsType
import it.fast4x.rimusic.enums.AnimatedGradient
import it.fast4x.rimusic.enums.ArtistSortBy
import it.fast4x.rimusic.enums.ArtistsType
import it.fast4x.rimusic.enums.AudioQualityFormat
import it.fast4x.rimusic.enums.BackgroundProgress
import it.fast4x.rimusic.enums.BuiltInPlaylist
import it.fast4x.rimusic.enums.CarouselSize
import it.fast4x.rimusic.enums.CheckUpdateState
import it.fast4x.rimusic.enums.CoilDiskCacheMaxSize
import it.fast4x.rimusic.enums.ColorPaletteMode
import it.fast4x.rimusic.enums.ColorPaletteName
import it.fast4x.rimusic.enums.DurationInMilliseconds
import it.fast4x.rimusic.enums.DurationInMinutes
import it.fast4x.rimusic.enums.ExoPlayerCacheLocation
import it.fast4x.rimusic.enums.ExoPlayerDiskCacheMaxSize
import it.fast4x.rimusic.enums.ExoPlayerDiskDownloadCacheMaxSize
import it.fast4x.rimusic.enums.ExoPlayerMinTimeForEvent
import it.fast4x.rimusic.enums.FilterBy
import it.fast4x.rimusic.enums.FontType
import it.fast4x.rimusic.enums.HistoryType
import it.fast4x.rimusic.enums.HomeItemSize
import it.fast4x.rimusic.enums.HomeScreenTabs
import it.fast4x.rimusic.enums.IconLikeType
import it.fast4x.rimusic.enums.Languages
import it.fast4x.rimusic.enums.LyricsAlignment
import it.fast4x.rimusic.enums.LyricsBackground
import it.fast4x.rimusic.enums.LyricsColor
import it.fast4x.rimusic.enums.LyricsFontSize
import it.fast4x.rimusic.enums.LyricsHighlight
import it.fast4x.rimusic.enums.LyricsOutline
import it.fast4x.rimusic.enums.MaxSongs
import it.fast4x.rimusic.enums.MaxStatisticsItems
import it.fast4x.rimusic.enums.MaxTopPlaylistItems
import it.fast4x.rimusic.enums.MenuStyle
import it.fast4x.rimusic.enums.MiniPlayerType
import it.fast4x.rimusic.enums.MusicAnimationType
import it.fast4x.rimusic.enums.NavigationBarPosition
import it.fast4x.rimusic.enums.NavigationBarType
import it.fast4x.rimusic.enums.NotificationButtons
import it.fast4x.rimusic.enums.NotificationType
import it.fast4x.rimusic.enums.OnDeviceSongSortBy
import it.fast4x.rimusic.enums.PauseBetweenSongs
import it.fast4x.rimusic.enums.PipModule
import it.fast4x.rimusic.enums.PlayEventsType
import it.fast4x.rimusic.enums.PlayerBackgroundColors
import it.fast4x.rimusic.enums.PlayerControlsType
import it.fast4x.rimusic.enums.PlayerInfoType
import it.fast4x.rimusic.enums.PlayerPlayButtonType
import it.fast4x.rimusic.enums.PlayerPosition
import it.fast4x.rimusic.enums.PlayerThumbnailSize
import it.fast4x.rimusic.enums.PlayerTimelineSize
import it.fast4x.rimusic.enums.PlayerTimelineType
import it.fast4x.rimusic.enums.PlayerType
import it.fast4x.rimusic.enums.PlaylistSongSortBy
import it.fast4x.rimusic.enums.PlaylistSortBy
import it.fast4x.rimusic.enums.PlaylistSwipeAction
import it.fast4x.rimusic.enums.PlaylistsType
import it.fast4x.rimusic.enums.PresetsReverb
import it.fast4x.rimusic.enums.QueueLoopType
import it.fast4x.rimusic.enums.QueueSwipeAction
import it.fast4x.rimusic.enums.QueueType
import it.fast4x.rimusic.enums.RecommendationsNumber
import it.fast4x.rimusic.enums.Romanization
import it.fast4x.rimusic.enums.SongSortBy
import it.fast4x.rimusic.enums.SongsNumber
import it.fast4x.rimusic.enums.SortOrder
import it.fast4x.rimusic.enums.StatisticsCategory
import it.fast4x.rimusic.enums.StatisticsType
import it.fast4x.rimusic.enums.SwipeAnimationNoThumbnail
import it.fast4x.rimusic.enums.ThumbnailCoverType
import it.fast4x.rimusic.enums.ThumbnailRoundness
import it.fast4x.rimusic.enums.ThumbnailType
import it.fast4x.rimusic.enums.TransitionEffect
import it.fast4x.rimusic.enums.UiType
import it.fast4x.rimusic.enums.WallpaperType
import it.fast4x.rimusic.ui.styling.DefaultDarkColorPalette
import it.fast4x.rimusic.ui.styling.DefaultLightColorPalette
import it.fast4x.rimusic.utils.getDeviceVolume
import me.knighthat.innertube.Constants
import org.jetbrains.annotations.Blocking
import org.jetbrains.annotations.NonBlocking
import java.net.Proxy

/**
 * A set of lazily initialized singleton preferences.
 *
 * Unlike old implementation [it.fast4x.rimusic.utils.rememberPreference], each
 * call returns new [MutableState] that gets updated individually
 * and requires default value to be defined at call time. Such implementation
 * creates unwanted behavior when different "default value" is appointed.
 *
 * New implementation introduced to unified calls, main focus is to
 * make all calls return the same **mutable** object. Lazy init is another step
 * to make sure that unused preferences remain uninitialized so they won't
 * consume resources.
 */
object Settings {

    private lateinit var preferences: SharedPreferences

    //<editor-fold defaultstate="collapsed" desc="Item size">
    val HOME_ARTIST_ITEM_SIZE by lazy {
        Preference.EnumPreference( preferences, "HomeAristItemSize", "AristItemSizeEnum", HomeItemSize.SMALL )
    }
    val HOME_ALBUM_ITEM_SIZE by lazy {
        Preference.EnumPreference( preferences, "HomeAlbumItemSize", "AlbumItemSizeEnum", HomeItemSize.SMALL )
    }
    val HOME_LIBRARY_ITEM_SIZE by lazy {
        Preference.EnumPreference( preferences, "HomeLibraryItemSize", "LibraryItemSizeEnum", HomeItemSize.SMALL )
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Sort by">
    val HOME_SONGS_SORT_BY by lazy {
        Preference.EnumPreference( preferences, "HomeSongsSortBy", "", SongSortBy.Title )
    }
    val HOME_ON_DEVICE_SONGS_SORT_BY by lazy {
        Preference.EnumPreference( preferences, "HomeOnDeviceSongsSortBy", "", OnDeviceSongSortBy.Title )
    }
    val HOME_ARTISTS_SORT_BY by lazy {
        Preference.EnumPreference( preferences, "HomeArtistsSortBy", "", ArtistSortBy.Name )
    }
    val HOME_ALBUMS_SORT_BY by lazy {
        Preference.EnumPreference( preferences, "HomeAlbumsSortBy", "", AlbumSortBy.Title )
    }
    val HOME_LIBRARY_SORT_BY by lazy {
        Preference.EnumPreference( preferences, "HomeLibrarySortBy", "", PlaylistSortBy.SongCount )
    }
    val PLAYLIST_SONGS_SORT_BY by lazy {
        Preference.EnumPreference( preferences, "PlaylistSongsSortBy", "", PlaylistSongSortBy.Title )
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Sort order">
    val HOME_SONGS_SORT_ORDER by lazy {
        Preference.EnumPreference( preferences, "HomeSongsSortOrder", "", SortOrder.Ascending )
    }
    val HOME_ARTISTS_SORT_ORDER by lazy {
        Preference.EnumPreference( preferences, "PlaylistSongsSortOrder", "", SortOrder.Ascending )
    }
    val HOME_ALBUM_SORT_ORDER by lazy {
        Preference.EnumPreference( preferences, "PlaylistSongsSortOrder", "", SortOrder.Ascending )
    }
    val HOME_LIBRARY_SORT_ORDER by lazy {
        Preference.EnumPreference( preferences, "HomeLibrarySortOrder", "", SortOrder.Ascending )
    }
    val PLAYLIST_SONGS_SORT_ORDER by lazy {
        Preference.EnumPreference( preferences, "PlaylistSongsSortOrder", "", SortOrder.Ascending )
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Max # of ...">
    val MAX_NUMBER_OF_SMART_RECOMMENDATIONS by lazy {
        Preference.EnumPreference( preferences, "MaxNumberOfSmartRecommendations", "recommendationsNumber", RecommendationsNumber.`5` )
    }
    val MAX_NUMBER_OF_STATISTIC_ITEMS by lazy {
        Preference.EnumPreference( preferences, "MaxNumberOfStatisticItems", "maxStatisticsItems", MaxStatisticsItems.`10` )
    }
    val MAX_NUMBER_OF_TOP_PLAYED by lazy {
        Preference.EnumPreference( preferences, "MaxNumberOfTopPlayed", "MaxTopPlaylistItems", MaxTopPlaylistItems.`10` )
    }
    val MAX_NUMBER_OF_SONG_IN_QUEUE by lazy {
        Preference.EnumPreference( preferences, "MaxNumberOfTopPlayed", "MaxTopPlaylistItems", MaxSongs.Unlimited )
    }
    val MAX_NUMBER_OF_NEXT_IN_QUEUE by lazy {
        Preference.EnumPreference( preferences, "MaxNumberOfNextInQueue", "showsongs", SongsNumber.`2` )
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Swipe action">
    val ENABLE_SWIPE_ACTION by lazy {
        Preference.BooleanPreference( preferences, "EnableSwipeAction", "isSwipeToActionEnabled", true )
    }
    val QUEUE_SWIPE_LEFT_ACTION by lazy {
        Preference.EnumPreference( preferences, "QueueSwipeLeftAction", "queueSwipeLeftAction", QueueSwipeAction.RemoveFromQueue )
    }
    val QUEUE_SWIPE_RIGHT_ACTION by lazy {
        Preference.EnumPreference( preferences, "QueueSwipeRightAction", "queueSwipeRightAction", QueueSwipeAction.PlayNext )
    }
    val PLAYLIST_SWIPE_LEFT_ACTION by lazy {
        Preference.EnumPreference( preferences, "PlaylistSwipeLeftAction", "playlistSwipeLeftAction", PlaylistSwipeAction.Favourite )
    }
    val PLAYLIST_SWIPE_RIGHT_ACTION by lazy {
        Preference.EnumPreference( preferences, "PlaylistSwipeRightAction", "playlistSwipeRightAction", PlaylistSwipeAction.PlayNext )
    }
    val ALBUM_SWIPE_LEFT_ACTION by lazy {
        Preference.EnumPreference( preferences, "AlbumSwipeLeftAction", "albumSwipeLeftAction", AlbumSwipeAction.PlayNext )
    }
    val ALBUM_SWIPE_RIGHT_ACTION by lazy {
        Preference.EnumPreference( preferences, "AlbumSwipeRightAction", "albumSwipeRightAction", AlbumSwipeAction.Bookmark )
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Mini player">
    val MINI_PLAYER_POSITION by lazy {
        Preference.EnumPreference( preferences, "MiniPlayerPosition", "playerPosition", PlayerPosition.Bottom )
    }
    val MINI_PLAYER_TYPE by lazy {
        Preference.EnumPreference( preferences, "MiniPlayerType", "miniPlayerType", MiniPlayerType.Modern )
    }
    val MINI_PLAYER_PROGRESS_BAR by lazy {
        Preference.EnumPreference( preferences, "MiniPlayerProgressBar", "backgroundProgress", BackgroundProgress.MiniPlayer )
    }
    val MINI_DISABLE_SWIPE_DOWN_TO_DISMISS by lazy {
        Preference.BooleanPreference( preferences, "MiniPlayerDisableSwipeDownToDismiss", "disableClosingPlayerSwipingDown", false )
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Player">
    val PLAYER_CONTROLS_TYPE by lazy {
        Preference.EnumPreference( preferences, "PlayerControlsType", "playerControlsType", PlayerControlsType.Essential )
    }
    val PLAYER_IS_CONTROLS_EXPANDED by lazy {
        Preference.BooleanPreference( preferences, "PlayerIsControlsExpanded", "controlsExpanded", true )
    }
    val PLAYER_INFO_TYPE by lazy {
        Preference.EnumPreference( preferences, "PlayerInfoType", "playerInfoType", PlayerInfoType.Essential )
    }
    val PLAYER_TYPE by lazy {
        Preference.EnumPreference( preferences, "PlayerType", "playerType", PlayerType.Essential )
    }
    val PLAYER_TIMELINE_TYPE by lazy {
        Preference.EnumPreference( preferences, "PlayerTimelineType", "playerTimelineType", PlayerTimelineType.FakeAudioBar )
    }
    val PLAYER_PORTRAIT_THUMBNAIL_SIZE by lazy {
        Preference.EnumPreference( preferences, "PlayerThumbnailSize", "playerThumbnailSize", PlayerThumbnailSize.Biggest )
    }
    val PLAYER_LANDSCAPE_THUMBNAIL_SIZE by lazy {
        Preference.EnumPreference( preferences, "PlayerLandscapeThumbnailSize", "playerThumbnailSizeL", PlayerThumbnailSize.Biggest )
    }
    val PLAYER_TIMELINE_SIZE by lazy {
        Preference.EnumPreference( preferences, "PlayerTimelineSize", "playerTimelineSize", PlayerTimelineSize.Biggest )
    }
    val PLAYER_PLAY_BUTTON_TYPE by lazy {
        Preference.EnumPreference( preferences, "PlayerPlayButtonType", "playerPlayButtonType", PlayerPlayButtonType.Disabled )
    }
    val PLAYER_BACKGROUND by lazy {
        Preference.EnumPreference( preferences, "PlayerBackground", "playerBackgroundColors", PlayerBackgroundColors.BlurredCoverColor )
    }
    val PLAYER_THUMBNAIL_TYPE by lazy {
        Preference.EnumPreference( preferences, "PlayerThumbnailType", "coverThumbnailAnimation", ThumbnailCoverType.Vinyl )
    }
    val PLAYER_THUMBNAIL_VINYL_SIZE by lazy {
        Preference.FloatPreference( preferences, "PlayerThumbnailVinylSize", "VinylSize", 50F )
    }
    val PLAYER_NO_THUMBNAIL_SWIPE_ANIMATION by lazy {
        Preference.EnumPreference( preferences, "PlayerNoThumbnailSwipeAnimation", "swipeAnimationsNoThumbnail", SwipeAnimationNoThumbnail.Sliding )
    }
    val PLAYER_SHOW_THUMBNAIL by lazy {
        Preference.BooleanPreference( preferences, "PlayerShowThumbnail", "showthumbnail", true )
    }
    val PLAYER_BOTTOM_GRADIENT by lazy {
        Preference.BooleanPreference( preferences, "PlayerBottomGradient", "bottomgradient", false )
    }
    val PLAYER_EXPANDED by lazy {
        Preference.BooleanPreference( preferences, "PlayerExpanded", "expandedplayer", false )
    }
    val PLAYER_THUMBNAIL_HORIZONTAL_SWIPE_DISABLED by lazy {
        Preference.BooleanPreference( preferences, "PlayerThumbnailHorizontalSwipe", "disablePlayerHorizontalSwipe", false )
    }
    val PLAYER_THUMBNAIL_FADE by lazy {
        Preference.FloatPreference( preferences, "PlayerThumbnailFade", "thumbnailFade", 5F )
    }
    val PLAYER_THUMBNAIL_FADE_EX by lazy {
        Preference.FloatPreference( preferences, "PlayerThumbnailFadeEx", "thumbnailFadeEx", 5F )
    }
    val PLAYER_THUMBNAIL_SPACING by lazy {
        Preference.FloatPreference( preferences, "PlayerThumbnailSpacing", "thumbnailSpacing", 0F )
    }
    val PLAYER_THUMBNAIL_SPACING_LANDSCAPE by lazy {
        Preference.FloatPreference( preferences, "PlayerThumbnailSpacingLandscape", "thumbnailSpacingL", 0F )
    }
    val PLAYER_VISUALIZER by lazy {
        Preference.BooleanPreference( preferences, "PlayerVisualizer", "visualizerEnabled", false )
    }
    val PLAYER_CURRENT_VISUALIZER  by lazy {
        Preference.IntPreference( preferences, "PlayerCurrentVisualizer", "currentVisualizerKey", 0 )
    }
    val PLAYER_TAP_THUMBNAIL_FOR_LYRICS by lazy {
        Preference.BooleanPreference( preferences, "PlayerTapThumbnailForLyrics", "thumbnailTapEnabled", true )
    }
    val PLAYER_ACTION_ADD_TO_PLAYLIST by lazy {
        Preference.BooleanPreference( preferences, "PlayerActionAddToPlaylist", "showButtonPlayerAddToPlaylist", true )
    }
    val PLAYER_ACTION_OPEN_QUEUE_ARROW by lazy {
        Preference.BooleanPreference( preferences, "PlayerActionOpenQueueArrow", "showButtonPlayerArrow", true )
    }
    val PLAYER_ACTION_DOWNLOAD by lazy {
        Preference.BooleanPreference( preferences, "PlayerActionDownload", "showButtonPlayerDownload", true )
    }
    val PLAYER_ACTION_LOOP by lazy {
        Preference.BooleanPreference( preferences, "PlayerActionLoop", "showButtonPlayerLoop", true )
    }
    val PLAYER_ACTION_SHOW_LYRICS by lazy {
        Preference.BooleanPreference( preferences, "PlayerActionShowLyrics", "showButtonPlayerLyrics", true )
    }
    val PLAYER_ACTION_TOGGLE_EXPAND by lazy {
        Preference.BooleanPreference( preferences, "PlayerActionToggleExpand", "expandedplayertoggle", true )
    }
    val PLAYER_ACTION_SHUFFLE by lazy {
        Preference.BooleanPreference( preferences, "PlayerActionShuffle", "showButtonPlayerShuffle", true )
    }
    val PLAYER_ACTION_SLEEP_TIMER by lazy {
        Preference.BooleanPreference( preferences, "PlayerActionSleepTimer", "showButtonPlayerSleepTimer", false )
    }
    val PLAYER_ACTION_SHOW_MENU by lazy {
        Preference.BooleanPreference( preferences, "PlayerActionShowMenu", "showButtonPlayerMenu", false )
    }
    val PLAYER_ACTION_START_RADIO by lazy {
        Preference.BooleanPreference( preferences, "PlayerActionStartRadio", "showButtonPlayerStartRadio", false )
    }
    val PLAYER_ACTION_OPEN_EQUALIZER by lazy {
        Preference.BooleanPreference( preferences, "PlayerActionOpenEqualizer", "showButtonPlayerSystemEqualizer", false )
    }
    val PLAYER_ACTION_DISCOVER by lazy {
        Preference.BooleanPreference( preferences, "PlayerActionDiscover", "showButtonPlayerDiscover", false )
    }
    val PLAYER_ACTION_TOGGLE_VIDEO by lazy {
        Preference.BooleanPreference( preferences, "PlayerActionToggleVideo", "showButtonPlayerVideo", false )
    }
    val PLAYER_ACTION_LYRICS_POPUP_MESSAGE by lazy {
        Preference.BooleanPreference( preferences, "PlayerActionLyricsPopupMessage", "playerEnableLyricsPopupMessage", true )
    }
    val PLAYER_TRANSPARENT_ACTIONS_BAR by lazy {
        Preference.BooleanPreference( preferences, "PlayerTransparentActionsBar", "transparentBackgroundPlayerActionBar", false )
    }
    val PLAYER_ACTION_BUTTONS_SPACED_EVENLY by lazy {
        Preference.BooleanPreference( preferences, "PlayerActionButtonsSpacedEvenly", "actionspacedevenly", false )
    }
    val PLAYER_ACTIONS_BAR_TAP_TO_OPEN_QUEUE by lazy {
        Preference.BooleanPreference( preferences, "PlayerActionsBarTapToOpenQueue", "tapqueue", true )
    }
    val PLAYER_ACTIONS_BAR_SWIPE_UP_TO_OPEN_QUEUE by lazy {
        Preference.BooleanPreference( preferences, "PlayerIsActionsBarExpanded", "actionExpanded", true )
    }
    val PLAYER_IS_ACTIONS_BAR_EXPANDED by lazy {
        Preference.BooleanPreference( preferences, "PlayerActionsBarSwipeUpToOpenQueue", "swipeUpQueue", true )
    }
    val PLAYER_SHOW_TOTAL_QUEUE_TIME by lazy {
        Preference.BooleanPreference( preferences, "PlayerShowTotalQueueTime", "showTotalTimeQueue", true )
    }
    val PLAYER_IS_QUEUE_DURATION_EXPANDED by lazy {
        Preference.BooleanPreference( preferences, "PlayerIsQueueDurationExpanded", "queueDurationExpanded", true )
    }
    val PLAYER_SHOW_NEXT_IN_QUEUE by lazy {
        Preference.BooleanPreference( preferences, "PlayerShowNextInQueue", "showNextSongsInPlayer", false )
    }
    val PLAYER_IS_NEXT_IN_QUEUE_EXPANDED by lazy {
        Preference.BooleanPreference( preferences, "PlayerIsNextInQueueExpanded", "miniQueueExpanded", true )
    }
    val PLAYER_SHOW_NEXT_IN_QUEUE_THUMBNAIL by lazy {
        Preference.BooleanPreference( preferences, "PlayerShowNextInQueueThumbnail", "showalbumcover", true )
    }
    val PLAYER_SHOW_SONGS_REMAINING_TIME by lazy {
        Preference.BooleanPreference( preferences, "PlayerShowSongsRemainingTime", "showRemainingSongTime", true )
    }
    val PLAYER_SHOW_TOP_ACTIONS_BAR by lazy {
        Preference.BooleanPreference( preferences, "PlayerShowTopActionsBar", "showTopActionsBar", true )
    }
    val PLAYER_IS_CONTROL_AND_TIMELINE_SWAPPED by lazy {
        Preference.BooleanPreference( preferences, "PlayerIsControlAndTimelineSwapped", "playerSwapControlsWithTimeline", false )
    }
    val PLAYER_SHOW_THUMBNAIL_ON_VISUALIZER by lazy {
        Preference.BooleanPreference( preferences, "PlayerShowThumbnailOnVisualizer", "showvisthumbnail", false )
    }
    val PLAYER_SHRINK_THUMBNAIL_ON_PAUSE by lazy {
        Preference.BooleanPreference( preferences, "PlayerShrinkThumbnailOnPause", "thumbnailpause", false )
    }
    val PLAYER_KEEP_MINIMIZED by lazy {
        Preference.BooleanPreference( preferences, "PlayerKeepMinimized", "keepPlayerMinimized", false )
    }
    val PLAYER_BACKGROUND_BLUR by lazy {
        Preference.BooleanPreference( preferences, "PlayerBackgroundBlur", "noblur", true )
    }
    val PLAYER_BACKGROUND_BLUR_STRENGTH by lazy {
        Preference.FloatPreference( preferences, "PlayerBackgroundBlurStrength", "blurScale", 25F )
    }
    val PLAYER_BACKGROUND_BACK_DROP by lazy {
        Preference.FloatPreference( preferences, "PlayerBackgroundBackDrop", "playerBackdrop", 0F )
    }
    val PLAYER_BACKGROUND_FADING_EDGE by lazy {
        Preference.BooleanPreference( preferences, "PlayerBackgroundFadingEdge", "fadingedge", false )
    }
    val PLAYER_STATS_FOR_NERDS by lazy {
        Preference.BooleanPreference( preferences, "PlayerStatsForNerds", "statsfornerds", false )
    }
    val PLAYER_IS_STATS_FOR_NERDS_EXPANDED by lazy {
        Preference.BooleanPreference( preferences, "PlayerIsStatForNerdsExpanded", "statsExpanded", true )
    }
    val PLAYER_THUMBNAILS_CAROUSEL by lazy {
        Preference.BooleanPreference( preferences, "PlayerThumbnailCarousel", "carousel", true )
    }
    val PLAYER_THUMBNAIL_ANIMATION by lazy {
        Preference.BooleanPreference( preferences, "PlayerThumbnailAnimation", "showCoverThumbnailAnimation", false )
    }
    val PLAYER_THUMBNAIL_ROTATION by lazy {
        Preference.BooleanPreference( preferences, "PlayerThumbnailRotation", "albumCoverRotation", false )
    }
    val PLAYER_IS_TITLE_EXPANDED by lazy {
        Preference.BooleanPreference( preferences, "PlayerIsTitleExpanded", "titleExpanded", true )
    }
    val PLAYER_IS_TIMELINE_EXPANDED by lazy {
        Preference.BooleanPreference( preferences, "PlayerIsTimelineExpanded", "timelineExpanded", true )
    }
    val PLAYER_SONG_INFO_ICON by lazy {
        Preference.BooleanPreference( preferences, "PlayerSongInfoIcon", "playerInfoShowIcons", true )
    }
    val PLAYER_TOP_PADDING by lazy {
        Preference.BooleanPreference( preferences, "PlayerTopPadding", "topPadding", true )
    }
    val PLAYER_EXTRA_SPACE by lazy {
        Preference.BooleanPreference( preferences, "PlayerExtraSpace", "extraspace", false )
    }
    val PLAYER_ROTATING_ALBUM_COVER by lazy {
        Preference.BooleanPreference( preferences, "PlayerRotatingAlbumCover", "rotatingAlbumCover", false )
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Cache">
    val THUMBNAIL_CACHE_SIZE by lazy {
        Preference.EnumPreference( preferences, "ThumbnailCacheSize", "coilDiskCacheMaxSize", CoilDiskCacheMaxSize.`128MB` )
    }
    val THUMBNAIL_CACHE_CUSTOM_SIZE by lazy {
        Preference.IntPreference( preferences, "ThumbnailCacheCustomSize", "exoPlayerCustomCache", 32 )
    }
    val SONG_CACHE_SIZE by lazy {
        Preference.EnumPreference( preferences, "SongCacheSize", "exoPlayerDiskCacheMaxSize", ExoPlayerDiskCacheMaxSize.`2GB` )
    }
    val SONG_CACHE_CUSTOM_SIZE by lazy {
        Preference.IntPreference( preferences, "SongCacheCustomSize", "", 32 )
    }
    val SONG_DOWNLOAD_SIZE by lazy {
        Preference.EnumPreference( preferences, "SongDownloadSize", "exoPlayerDiskDownloadCacheMaxSize", ExoPlayerDiskDownloadCacheMaxSize.`2GB` )
    }
    val EXO_CACHE_LOCATION by lazy {
        Preference.EnumPreference( preferences, "ExoCacheLocation", "exoPlayerCacheLocationKey", ExoPlayerCacheLocation.System )
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Notification">
    val MEDIA_NOTIFICATION_FIRST_ICON by lazy {
        Preference.EnumPreference( preferences, "MediaNotificationFirstIcon", "notificationPlayerFirstIcon", NotificationButtons.Download )
    }
    val MEDIA_NOTIFICATION_SECOND_ICON by lazy {
        Preference.EnumPreference( preferences, "MediaNotificationSecondIcon", "notificationPlayerSecondIcon", NotificationButtons.Favorites )
    }
    val NOTIFICATION_TYPE by lazy {
        Preference.EnumPreference( preferences, "NotificationType", "notificationType", NotificationType.Default )
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Lyrics">
    val LYRICS_SIZE by lazy {
        Preference.FloatPreference( preferences, "LyricsSize", "lyricsSize", 5F )
    }
    val LYRICS_SIZE_LANDSCAPE by lazy {
        Preference.FloatPreference( preferences, "LyricsSizeLandscape", "lyricsSizeL", 5F )
    }
    val LYRICS_COLOR by lazy {
        Preference.EnumPreference( preferences, "LyricsColor", "lyricsColor", LyricsColor.Thememode )
    }
    val LYRICS_OUTLINE by lazy {
        Preference.EnumPreference( preferences, "HomeAlbumType", "albumType", LyricsOutline.None )
    }
    val LYRICS_FONT_SIZE by lazy {
        Preference.EnumPreference( preferences, "LyricsFontSize", "lyricsFontSize", LyricsFontSize.Medium )
    }
    val LYRICS_ROMANIZATION_TYPE by lazy {
        Preference.EnumPreference( preferences, "LyricsRomanizationType", "romanization", Romanization.Off )
    }
    val LYRICS_BACKGROUND by lazy {
        Preference.EnumPreference( preferences, "LyricsBackground", "lyricsBackground", LyricsBackground.Black )
    }
    val LYRICS_HIGHLIGHT by lazy {
        Preference.EnumPreference( preferences, "LyricsHighlight", "lyricsHighlight", LyricsHighlight.None )
    }
    val LYRICS_ALIGNMENT by lazy {
        Preference.EnumPreference( preferences, "LyricsAlignment", "lyricsAlignment", LyricsAlignment.Center )
    }
    val LYRICS_SHOW_THUMBNAIL by lazy {
        Preference.BooleanPreference( preferences, "LyricsShowThumbnail", "showlyricsthumbnail", false )
    }
    val LYRICS_JUMP_ON_TAP by lazy {
        Preference.BooleanPreference( preferences, "LyricsJumpOnTap", "clickOnLyricsText", true )
    }
    val LYRICS_SHOW_ACCENT_BACKGROUND by lazy {
        Preference.BooleanPreference( preferences, "LyricsShowAccentBackground", "showBackgroundLyrics", false )
    }
    val LYRICS_SYNCHRONIZED by lazy {
        Preference.BooleanPreference( preferences, "LyricsSynchronized", "isShowingSynchronizedLyrics", false )
    }
    val LYRICS_SHOW_SECOND_LINE by lazy {
        Preference.BooleanPreference( preferences, "LyricsShowSecondLine", "showSecondLine", false )
    }
    val LYRICS_ANIMATE_SIZE by lazy {
        Preference.BooleanPreference( preferences, "LyricsAnimateSize", "lyricsSizeAnimate", false )
    }
    val LYRICS_LANDSCAPE_CONTROLS by lazy {
        Preference.BooleanPreference( preferences, "LysricsLandscapeControls", "landscapeControls", true )
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Page type">
    val HOME_ARTIST_TYPE by lazy {
        Preference.EnumPreference( preferences, "HomeArtistType", "artistType", ArtistsType.Favorites )
    }
    val HOME_ALBUM_TYPE by lazy {
        Preference.EnumPreference( preferences, "HomeAlbumType", "albumType", AlbumsType.Favorites )
    }
    val HOME_SONGS_TYPE by lazy {
        Preference.EnumPreference( preferences, "HomeSongsType", "builtInPlaylist", BuiltInPlaylist.Favorites )
    }
    val HISTORY_PAGE_TYPE by lazy {
        Preference.EnumPreference( preferences, "HistoryPageType", "historyType", HistoryType.History )
    }
    val HOME_LIBRARY_TYPE by lazy {
        Preference.EnumPreference( preferences, "HomePlaylistType", "playlistType", PlaylistsType.Playlist )
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Audio">
    val AUDIO_FADE_DURATION by lazy {
        Preference.EnumPreference( preferences, "AudioFadeDuration", "playbackFadeAudioDuration", DurationInMilliseconds.Disabled )
    }
    val AUDIO_QUALITY by lazy {
        Preference.EnumPreference( preferences, "AudioQuality", "audioQualityFormat", AudioQualityFormat.Auto )
    }
    val AUDIO_REVERB_PRESET by lazy {
        Preference.EnumPreference( preferences, "AudioReverbPreset", "audioReverbPreset", PresetsReverb.NONE )
    }
    val AUDIO_SKIP_SILENCE by lazy {
        Preference.BooleanPreference( preferences, "AudioSkipSilence", "skipSilence", false )
    }
    val AUDIO_SKIP_SILENCE_LENGTH by lazy {
        Preference.LongPreference( preferences, "AudioSkipSilenceLength", "minimumSilenceDuration", 2_000_000L )
    }
    val AUDIO_VOLUME_NORMALIZATION by lazy {
        Preference.BooleanPreference( preferences, "AudioVolumeNormalization", "volumeNormalization", false )
    }
    val AUDIO_VOLUME_NORMALIZATION_TARGET by lazy {
        Preference.FloatPreference( preferences, "AudioVolumeNormalizationTarget", "loudnessBaseGain", 5F )
    }
    val AUDIO_SHAKE_TO_SKIP by lazy {
        Preference.BooleanPreference( preferences, "AudioShakeToSkip", "shakeEventEnabled", false )
    }
    val AUDIO_VOLUME_BUTTONS_CHANGE_SONG by lazy {
        Preference.BooleanPreference( preferences, "AudioVolumeButtonsChangeSong", "useVolumeKeysToChangeSong", false )
    }
    val AUDIO_BASS_BOOSTED by lazy {
        Preference.BooleanPreference( preferences, "AudioBassBoosted", "bassboostEnabled", false )
    }
    val AUDIO_BASS_BOOST_LEVEL by lazy {
        Preference.FloatPreference( preferences, "AudioBassBoostLevel", "bassboostLevel", .5F )
    }
    val AUDIO_SMART_PAUSE_DURING_CALLS by lazy {
        Preference.BooleanPreference( preferences, "AudioSmartPauseDuringCalls", "handleAudioFocusEnabled", true )
    }
    val AUDIO_SPEED by lazy {
        Preference.BooleanPreference( preferences, "AudioSpeed", "showPlaybackSpeedButton", false )
    }
    val AUDIO_SPEED_VALUE by lazy {
        Preference.FloatPreference( preferences, "AudioSpeedValue", "playbackSpeed", 1F )
    }
    val AUDIO_PITCH by lazy {
        Preference.FloatPreference( preferences, "AudioPitch", "playbackPitch", 1F )
    }
    val AUDIO_VOLUME by lazy {
        Preference.FloatPreference( preferences, "AudioVolume", "playbackVolume", .5F )
    }
    val AUDIO_DEVICE_VOLUME by lazy {
        Preference.FloatPreference( preferences, "AudioVolume", "playbackVolume", getDeviceVolume( appContext() ) )
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="YouTube">
    val YOUTUBE_LOGIN by lazy {
        Preference.BooleanPreference( preferences, "YouTubeLogin", "enableYoutubeLogin", false )
    }
    val YOUTUBE_PLAYLISTS_SYNC by lazy {
        Preference.BooleanPreference( preferences, "YouTubePlaylistsSync", "enableYoutubeSync", false )
    }
    val YOUTUBE_VISITOR_DATA by lazy {
        Preference.StringPreference( preferences, "YouTubeVisitorData", "ytVisitorData", Constants.VISITOR_DATA )
    }
    val YOUTUBE_SYNC_ID by lazy {
        Preference.StringPreference( preferences, "YouTubeSyncId", "ytDataSyncIdKey", "" )
    }
    val YOUTUBE_COOKIES by lazy {
        Preference.StringPreference( preferences, "YouTubeCookies", "ytCookie", "" )
    }
    val YOUTUBE_ACCOUNT_NAME by lazy {
        Preference.StringPreference( preferences, "YouTubeAccountName", "ytAccountNameKey", "" )
    }
    val YOUTUBE_ACCOUNT_EMAIL by lazy {
        Preference.StringPreference( preferences, "YouTubeAccountEmail", "ytAccountEmailKey", "" )
    }
    val YOUTUBE_SELF_CHANNEL_HANDLE by lazy {
        Preference.StringPreference( preferences, "YouTubeSelfChannelHandle", "ytAccountChannelHandleKey", "" )
    }
    val YOUTUBE_ACCOUNT_AVATAR by lazy {
        Preference.StringPreference( preferences, "YouTubeAccountAvatar", "ytAccountThumbnailKey", "" )
    }
    val YOUTUBE_LAST_VIDEO_ID by lazy {
        Preference.StringPreference( preferences, "YouTubeLastVideoId", "lastVideoId", "" )
    }
    val YOUTUBE_LAST_VIDEO_SECONDS by lazy {
        Preference.FloatPreference( preferences, "YouTubeLastVideoSeconds", "lastVideoSeconds", 0F )
    }
    //</editor-fold>
    //<editor-fold desc="Quick picks">
    val QUICK_PICKS_TYPE by lazy {
        Preference.EnumPreference( preferences, "QuickPicksType", "playEventsType", PlayEventsType.MostPlayed )
    }
    val QUICK_PICKS_MIN_DURATION by lazy {
        Preference.EnumPreference( preferences, "QuickPicksMinDuration", "exoPlayerMinTimeForEvent", ExoPlayerMinTimeForEvent.`20s` )
    }
    val QUICK_PICKS_SHOW_TIPS by lazy {
        Preference.BooleanPreference( preferences, "QuickPicksShowTips", "showTips", true )
    }
    val QUICK_PICKS_SHOW_RELATED_ALBUMS by lazy {
        Preference.BooleanPreference( preferences, "QuickPicksShowRelatedAlbums", "showRelatedAlbums", true )
    }
    val QUICK_PICKS_SHOW_RELATED_ARTISTS by lazy {
        Preference.BooleanPreference( preferences, "QuickPicksShowRelatedArtists", "showSimilarArtists", true )
    }
    val QUICK_PICKS_SHOW_NEW_ALBUMS_ARTISTS by lazy {
        Preference.BooleanPreference( preferences, "QuickPicksShowNewAlbumsArtists", "showNewAlbumsArtists", true )
    }
    val QUICK_PICKS_SHOW_NEW_ALBUMS by lazy {
        Preference.BooleanPreference( preferences, "QuickPicksShowNewAlbums", "showNewAlbums", true )
    }
    val QUICK_PICKS_SHOW_MIGHT_LIKE_PLAYLISTS by lazy {
        Preference.BooleanPreference( preferences, "QuickPicksShowPlaylists", "showPlaylistMightLike", true )
    }
    val QUICK_PICKS_SHOW_MOODS_AND_GENRES by lazy {
        Preference.BooleanPreference( preferences, "QuickPicksShowMoodsAndGenres", "showMoodsAndGenres", true )
    }
    val QUICK_PICKS_SHOW_MONTHLY_PLAYLISTS by lazy {
        Preference.BooleanPreference( preferences, "QuickPicksShowMonthlyPlaylists", "showMonthlyPlaylistInQuickPicks", true )
    }
    val QUICK_PICKS_SHOW_CHARTS by lazy {
        Preference.BooleanPreference( preferences, "QuickPicksShowCharts", "showCharts", true )
    }
    val QUICK_PICKS_PAGE by lazy {
        Preference.BooleanPreference( preferences, "QuickPicksPage", "enableQuickPicksPage", true )
    }
    //</editor-fold>
    //<editor-fold desc="Discord">
    val DISCORD_LOGIN by lazy {
        Preference.BooleanPreference( preferences, "DiscordLogin", "isDiscordPresenceEnabled", false )
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Proxy">
    val IS_PROXY_ENABLED by lazy {
        Preference.BooleanPreference( preferences, "IsProxyEnabled", "isProxyEnabled", false )
    }
    val PROXY_SCHEME by lazy {
        Preference.EnumPreference( preferences, "ProxyScheme", "ProxyMode", Proxy.Type.HTTP )
    }
    val PROXY_HOST by lazy {
        Preference.StringPreference( preferences, "ProxyHost", "proxyHostnameKey", "" )
    }
    val PROXY_PORT  by lazy {
        Preference.IntPreference( preferences, "ProxyPort", "proxyPort", 1080 )
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Custom light colors">
    val CUSTOM_LIGHT_THEME_BACKGROUND_0_HASH_CODE by lazy {
        Preference.IntPreference( preferences, "CustomLightThemeBackground0", "customThemeLight_Background0", DefaultLightColorPalette.background0.hashCode() )
    }
    val CUSTOM_LIGHT_THEME_BACKGROUND_1_HASH_CODE by lazy {
        Preference.IntPreference( preferences, "CustomLightThemeBackground1", "customThemeLight_Background1", DefaultLightColorPalette.background1.hashCode() )
    }
    val CUSTOM_LIGHT_THEME_BACKGROUND_2_HASH_CODE by lazy {
        Preference.IntPreference( preferences, "CustomLightThemeBackground2", "customThemeLight_Background2", DefaultLightColorPalette.background2.hashCode() )
    }
    val CUSTOM_LIGHT_THEME_BACKGROUND_3_HASH_CODE by lazy {
        Preference.IntPreference( preferences, "CustomLightThemeBackground3", "customThemeLight_Background3", DefaultLightColorPalette.background3.hashCode() )
    }
    val CUSTOM_LIGHT_THEME_BACKGROUND_4_HASH_CODE by lazy {
        Preference.IntPreference( preferences, "CustomLightThemeBackground4", "customThemeLight_Background4", DefaultLightColorPalette.background4.hashCode() )
    }
    val CUSTOM_LIGHT_TEXT_HASH_CODE by lazy {
        Preference.IntPreference( preferences, "CustomLightThemeText", "customThemeLight_Text", DefaultLightColorPalette.text.hashCode() )
    }
    val CUSTOM_LIGHT_TEXT_SECONDARY_HASH_CODE by lazy {
        Preference.IntPreference( preferences, "CustomLightThemeTextSecondary", "customThemeLight_textSecondary", DefaultLightColorPalette.textSecondary.hashCode() )
    }
    val CUSTOM_LIGHT_TEXT_DISABLED_HASH_CODE by lazy {
        Preference.IntPreference( preferences, "CustomLightThemeTextDisabled", "customThemeLight_textDisabled", DefaultLightColorPalette.textDisabled.hashCode() )
    }
    val CUSTOM_LIGHT_PLAY_BUTTON_HASH_CODE by lazy {
        Preference.IntPreference( preferences, "CustomLightThemePlayButton", "customThemeLight_iconButtonPlayer", DefaultLightColorPalette.iconButtonPlayer.hashCode() )
    }
    val CUSTOM_LIGHT_ACCENT_HASH_CODE by lazy {
        Preference.IntPreference( preferences, "CustomLightThemeAccent", "customThemeLight_accent", DefaultLightColorPalette.accent.hashCode() )
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Custom dark theme">
    val CUSTOM_DARK_THEME_BACKGROUND_0_HASH_CODE by lazy {
        Preference.IntPreference( preferences, "CustomDarkThemeBackground0", "customThemeDark_Background0", DefaultDarkColorPalette.background0.hashCode() )
    }
    val CUSTOM_DARK_THEME_BACKGROUND_1_HASH_CODE by lazy {
        Preference.IntPreference( preferences, "CustomDarkThemeBackground1", "customThemeDark_Background1", DefaultDarkColorPalette.background1.hashCode() )
    }
    val CUSTOM_DARK_THEME_BACKGROUND_2_HASH_CODE by lazy {
        Preference.IntPreference( preferences, "CustomDarkThemeBackground2", "customThemeDark_Background2", DefaultDarkColorPalette.background2.hashCode() )
    }
    val CUSTOM_DARK_THEME_BACKGROUND_3_HASH_CODE by lazy {
        Preference.IntPreference( preferences, "CustomDarkThemeBackground3", "customThemeDark_Background3", DefaultDarkColorPalette.background3.hashCode() )
    }
    val CUSTOM_DARK_THEME_BACKGROUND_4_HASH_CODE by lazy {
        Preference.IntPreference( preferences, "CustomDarkThemeBackground4", "customThemeDark_Background4", DefaultDarkColorPalette.background4.hashCode() )
    }
    val CUSTOM_DARK_TEXT_HASH_CODE by lazy {
        Preference.IntPreference( preferences, "CustomDarkThemeText", "customThemeDark_Text", DefaultDarkColorPalette.text.hashCode() )
    }
    val CUSTOM_DARK_TEXT_SECONDARY_HASH_CODE by lazy {
        Preference.IntPreference( preferences, "CustomDarkThemeTextSecondary", "customThemeDark_textSecondary", DefaultDarkColorPalette.textSecondary.hashCode() )
    }
    val CUSTOM_DARK_TEXT_DISABLED_HASH_CODE by lazy {
        Preference.IntPreference( preferences, "CustomDarkThemeTextDisabled", "customThemeDark_textDisabled", DefaultDarkColorPalette.textDisabled.hashCode() )
    }
    val CUSTOM_DARK_PLAY_BUTTON_HASH_CODE by lazy {
        Preference.IntPreference( preferences, "CustomDarkThemePlayButton", "customThemeDark_iconButtonPlayer", DefaultDarkColorPalette.iconButtonPlayer.hashCode() )
    }
    val CUSTOM_DARK_ACCENT_HASH_CODE by lazy {
        Preference.IntPreference( preferences, "CustomDarkThemeAccent", "customThemeDark_accent", DefaultDarkColorPalette.accent.hashCode() )
    }
    //</editor-fold>

    val HOME_SONGS_TOP_PLAYLIST_PERIOD by lazy {
        Preference.EnumPreference( preferences, "HomeSongsTopPlaylistPeriod", "", StatisticsType.All )
    }
    val MENU_STYLE by lazy {
        Preference.EnumPreference( preferences, "MenuStyle", "menuStyle", MenuStyle.List )
    }
    val MAIN_THEME by lazy {
        Preference.EnumPreference( preferences, "MainTheme", "UiType", UiType.RiMusic )
    }
    val COLOR_PALETTE by lazy {
        Preference.EnumPreference( preferences, "ColorPalette", "colorPaletteName", ColorPaletteName.Dynamic )
    }
    val THEME_MODE by lazy {
        Preference.EnumPreference( preferences, "ThemeMode", "colorPaletteMode", ColorPaletteMode.Dark )
    }
    val STARTUP_SCREEN by lazy {
        Preference.EnumPreference( preferences, "StartupScreen", "indexNavigationTab", HomeScreenTabs.Songs )
    }
    val FONT by lazy {
        Preference.EnumPreference( preferences, "Font", "fontType", FontType.Rubik )
    }
    val NAVIGATION_BAR_POSITION by lazy {
        Preference.EnumPreference( preferences, "NavigationBarPosition", "navigationBarPosition", NavigationBarPosition.Bottom )
    }
    val NAVIGATION_BAR_TYPE by lazy {
        Preference.EnumPreference( preferences, "NavigationBarType", "navigationBarType", NavigationBarType.IconAndText )
    }
    val PAUSE_BETWEEN_SONGS by lazy {
        Preference.EnumPreference( preferences, "PauseBetweenSongs", "pauseBetweenSongs", PauseBetweenSongs.`0` )
    }
    val THUMBNAIL_BORDER_RADIUS by lazy {
        Preference.EnumPreference( preferences, "ThumbnailBorderRadius", "thumbnailRoundness", ThumbnailRoundness.Heavy )
    }
    val TRANSITION_EFFECT by lazy {
        Preference.EnumPreference( preferences, "TransitionEffect", "transitionEffect", TransitionEffect.Scale )
    }
    val LIMIT_SONGS_WITH_DURATION by lazy {
        Preference.EnumPreference( preferences, "LimitSongsWithDuration", "excludeSongsWithDurationLimit", DurationInMinutes.Disabled )
    }
    val QUEUE_TYPE by lazy {
        Preference.EnumPreference( preferences, "QueueType", "queueType", QueueType.Essential )
    }
    val QUEUE_LOOP_TYPE by lazy {
        Preference.EnumPreference( preferences, "QueueLoopType", "queueLoopType", QueueLoopType.Default )
    }
    val QUEUE_AUTO_APPEND by lazy {
        Preference.BooleanPreference( preferences, "QueueAutoAppend", "autoLoadSongsInQueue", true )
    }
    val CAROUSEL_SIZE by lazy {
        Preference.EnumPreference( preferences, "CarouselSize", "carouselSize", CarouselSize.Biggest )
    }
    val THUMBNAIL_TYPE by lazy {
        Preference.EnumPreference( preferences, "ThumbnailType", "thumbnailType", ThumbnailType.Modern )
    }
    val LIKE_ICON by lazy {
        Preference.EnumPreference( preferences, "LikeIcon", "iconLikeType", IconLikeType.Essential )
    }
    val WALLPAPER_TYPE by lazy {
        Preference.EnumPreference( preferences, "WallpaperType", "wallpaperType", WallpaperType.Lockscreen )
    }
    val ANIMATED_GRADIENT by lazy {
        Preference.EnumPreference( preferences, "AnimatedGradient", "animatedGradient", AnimatedGradient.Linear )
    }
    val NOW_PLAYING_INDICATOR by lazy {
        Preference.EnumPreference( preferences, "NowPlayingIndicator", "nowPlayingIndicator", MusicAnimationType.Bubbles )
    }
    val PIP_MODULE by lazy {
        Preference.EnumPreference( preferences, "PipModule", "pipModule", PipModule.Cover )
    }
    val CHECK_UPDATE by lazy {
        Preference.EnumPreference( preferences, "CheckUpdateState", "checkUpdateState", CheckUpdateState.Disabled )
    }
    val APP_LANGUAGE by lazy {
        Preference.EnumPreference( preferences, "AppLanguage", "languageApp", Languages.System )
    }
    val OTHER_APP_LANGUAGE by lazy {
        Preference.EnumPreference( preferences, "OtherAppLanguage", "otherLanguageApp", Languages.System )
    }
    val HOME_ARTIST_AND_ALBUM_FILTER by lazy {
        Preference.EnumPreference( preferences, "filterBy", "", FilterBy.All )
    }
    val STATISTIC_PAGE_CATEGORY by lazy {
        Preference.EnumPreference( preferences, "StatisticPageCategory", "statisticsCategory", StatisticsCategory.Songs )
    }
    val SCROLLING_TEXT_DISABLED by lazy {
        Preference.BooleanPreference( preferences, "ScrollingText", "disableScrollingText", false )
    }
    val PARENTAL_CONTROL by lazy {
        Preference.BooleanPreference( preferences, "ParentalControl", "parentalControlEnabled", false )
    }
    val ROTATION_EFFECT by lazy {
        Preference.BooleanPreference( preferences, "RotationEffect", "effectRotation", true )
    }
    val TRANSPARENT_TIMELINE by lazy {
        Preference.BooleanPreference( preferences, "TransparentTimeline", "transparentbar", true )
    }
    val BLACK_GRADIENT by lazy {
        Preference.BooleanPreference( preferences, "BlackGradient", "blackgradient", false )
    }
    val TEXT_OUTLINE by lazy {
        Preference.BooleanPreference( preferences, "TextOutline", "textoutline", false )
    }
    val SHOW_FLOATING_ICON by lazy {
        Preference.BooleanPreference( preferences, "ShowFloatingIcon", "showFloatingIcon", false )
    }
    val FLOATING_ICON_X_OFFSET by lazy {
        Preference.FloatPreference( preferences, "FloatingIconXOffset", "floatActionIconOffsetX", 0F )
    }
    val FLOATING_ICON_Y_OFFSET by lazy {
        Preference.FloatPreference( preferences, "FloatingIconYOffset", "floatActionIconOffsetY", 0F )
    }
    val MULTI_FLOATING_ICON_X_OFFSET by lazy {
        Preference.FloatPreference( preferences, "MultiFloatingIconXOffset", "multiFloatActionIconOffsetX", 0F )
    }
    val MULTI_FLOATING_ICON_Y_OFFSET by lazy {
        Preference.FloatPreference( preferences, "MultiFloatingIconYOffset", "multiFloatActionIconOffsetY", 0F )
    }
    val ZOOM_OUT_ANIMATION by lazy {
        Preference.BooleanPreference( preferences, "ZoomOutAnimation", "buttonzoomout", false )
    }
    val ENABLE_WALLPAPER by lazy {
        Preference.BooleanPreference( preferences, "EnableWallpaper", "enableWallpaper", false )
    }
    val ENABLE_DISCOVER by lazy {
        Preference.BooleanPreference( preferences, "EnableDiscover", "discover", false )
    }
    val ENABLE_PERSISTENT_QUEUE by lazy {
        Preference.BooleanPreference( preferences, "EnablePersistentQueue", "persistentQueue", false )
    }
    val RESUME_PLAYBACK_ON_STARTUP by lazy {
        Preference.BooleanPreference( preferences, "ResumePlaybackOnStartup", "resumePlaybackOnStart", false )
    }
    val RESUME_PLAYBACK_WHEN_CONNECT_TO_AUDIO_DEVICE by lazy {
        Preference.BooleanPreference( preferences, "ResumePlaybackWhenConnectToAudioDevice", "resumePlaybackWhenDeviceConnected", false )
    }
    val CLOSE_BACKGROUND_JOB_IN_TASK_MANAGER by lazy {
        Preference.BooleanPreference( preferences, "CloseBackgroundJobInTaskManager", "closebackgroundPlayer", false )
    }
    val CLOSE_APP_ON_BACK by lazy {
        Preference.BooleanPreference( preferences, "CloseAppOnBack", "closeWithBackButton", true )
    }
    val PLAYBACK_SKIP_ON_ERROR by lazy {
        Preference.BooleanPreference( preferences, "PlaybackSkipOnError", "skipMediaOnError", false )
    }
    val USE_SYSTEM_FONT by lazy {
        Preference.BooleanPreference( preferences, "UseSystemFont", "useSystemFont", false )
    }
    val APPLY_FONT_PADDING by lazy {
        Preference.BooleanPreference( preferences, "ApplyFontPadding", "applyFontPadding", false )
    }
    val SHOW_SEARCH_IN_NAVIGATION_BAR by lazy {
        Preference.BooleanPreference( preferences, "ShowSearchInNavigationBar", "showSearchTab", false )
    }
    val SHOW_STATS_IN_NAVIGATION_BAR by lazy {
        Preference.BooleanPreference( preferences, "ShowStatsInNavigationBar", "showStatsInNavbar", false )
    }
    val SHOW_LISTENING_STATS by lazy {
        Preference.BooleanPreference( preferences, "ShowListeningStats", "showStatsListeningTime", true )
    }
    val HOME_SONGS_SHOW_FAVORITES_CHIP by lazy {
        Preference.BooleanPreference( preferences, "HomeSongsShowFavoritesChip", "showFavoritesPlaylist", true )
    }
    val HOME_SONGS_SHOW_CACHED_CHIP by lazy {
        Preference.BooleanPreference( preferences, "HomeSongsShowCachedChip", "showCachedPlaylist", true )
    }
    val HOME_SONGS_SHOW_DOWNLOADED_CHIP by lazy {
        Preference.BooleanPreference( preferences, "HomeSongsShowDownloadedChip", "showDownloadedPlaylist", true )
    }
    val HOME_SONGS_SHOW_MOST_PLAYED_CHIP by lazy {
        Preference.BooleanPreference( preferences, "HomeSongsShowMostPlayedChip", "showMyTopPlaylist", true )
    }
    val HOME_SONGS_SHOW_ON_DEVICE_CHIP by lazy {
        Preference.BooleanPreference( preferences, "HomeSongsShowOnDeviceChip", "showOnDevicePlaylist", true )
    }
    val HOME_SONGS_ON_DEVICE_SHOW_FOLDERS by lazy {
        Preference.BooleanPreference( preferences, "HomeSongsOnDeviceShowFolders", "showFoldersOnDevice", true )
    }
    val HOME_SONGS_INCLUDE_ON_DEVICE_IN_ALL by lazy {
        Preference.BooleanPreference( preferences, "HomeSongsIncludeOnDeviceInAll", "includeLocalSongs", false )
    }
    val MONTHLY_PLAYLIST_COMPILATION by lazy {
        Preference.BooleanPreference( preferences, "MonthlyPlaylistCompilation", "enableCreateMonthlyPlaylists", true )
    }
    val SHOW_PIPED_PLAYLISTS by lazy {
        Preference.BooleanPreference( preferences, "ShowPipedPlaylists", "showPipedPlaylists", true )
    }
    val SHOW_MONTHLY_PLAYLISTS by lazy {
        Preference.BooleanPreference( preferences, "ShowMonthlyPlaylists", "showMonthlyPlaylists", true )
    }
    val SHOW_PINNED_PLAYLISTS by lazy {
        Preference.BooleanPreference( preferences, "ShowPinnedPlaylists", "showPinnedPlaylists", true )
    }
    val SHOW_PLAYLIST_INDICATOR by lazy {
        Preference.BooleanPreference( preferences, "ShowPlaylistIndicator", "playlistindicator", false )
    }
    val PAUSE_WHEN_VOLUME_SET_TO_ZERO by lazy {
        Preference.BooleanPreference( preferences, "PauseWhenVolumeSetToZero", "isPauseOnVolumeZeroEnabled", false )
    }
    val PAUSE_HISTORY by lazy {
        Preference.BooleanPreference( preferences, "PauseHistory", "pauseListenHistory", false )
    }
    val RESTART_ACTIVITY by lazy {
        Preference.BooleanPreference( preferences, "RestartActivity", "restartActivity", false )
    }
    val IS_PIP_ENABLED by lazy {
        Preference.BooleanPreference( preferences, "IsPiPEnabled", "enablePicturInPicture", false )
    }
    val IS_AUTO_PIP_ENABLED by lazy {
        Preference.BooleanPreference( preferences, "IsAutoPiPEnabled", "enablePicturInPictureAuto", false )
    }
    val AUTO_DOWNLOAD by lazy {
        Preference.BooleanPreference( preferences, "AutoDownload", "autoDownloadSong", false )
    }
    val AUTO_DOWNLOAD_ON_LIKE by lazy {
        Preference.BooleanPreference( preferences, "AutoDownloadOnLike", "autoDownloadSongWhenLiked", false )
    }
    val AUTO_DOWNLOAD_ON_ALBUM_BOOKMARKED by lazy {
        Preference.BooleanPreference( preferences, "AutoDownloadOnAlbumBookmarked", "autoDownloadSongWhenAlbumBookmarked", false )
    }
    val KEEP_SCREEN_ON by lazy {
        Preference.BooleanPreference( preferences, "KeepScreenOn", "isKeepScreenOnEnabled", false )
    }
    val DEBUG_LOG by lazy {
        Preference.BooleanPreference( preferences, "DebugLog", "logDebugEnabled", false )
    }
    val ENABLE_PIPED by lazy {
        Preference.BooleanPreference( preferences, "EnablePiped", "isPipedEnabled", false )
    }
    val IS_CUSTOM_PIPED by lazy {
        Preference.BooleanPreference( preferences, "IsPipedCustom", "isPipedCustomEnabled", false )
    }
    val AUTO_SYNC by lazy {
        Preference.BooleanPreference( preferences, "AutoSync", "autosync", false )
    }
    val PAUSE_SEARCH_HISTORY by lazy {
        Preference.BooleanPreference( preferences, "PauseSearchHistory", "pauseSearchHistory", false )
    }
    val IS_DATA_KEY_LOADED by lazy {
        Preference.BooleanPreference( preferences, "IsDataKeyLoaded", "loadedData", false )
    }
    val LOCAL_PLAYLIST_SMART_RECOMMENDATION by lazy {
        Preference.BooleanPreference( preferences, "LocalPlaylistSmartRecommendation", "isRecommendationEnabled", false )
    }
    val IS_CONNECTION_METERED by lazy {
        Preference.BooleanPreference( preferences, "IsConnectionMetered", "isConnectionMeteredEnabled", true )
    }
    val JUMP_PREVIOUS by lazy {
        Preference.StringPreference( preferences, "JumpPrevious", "jumpPrevious", "3" )
    }
    val LOCAL_SONGS_FOLDER by lazy {
        Preference.StringPreference( preferences, "LocalSongsFolder", "defaultFolder", "/" )
    }
    val SEEN_CHANGELOGS_VERSION by lazy {
        Preference.StringPreference( preferences, "SeenChangelogsVersion", "seenChangelogsVersionKey", "" )
    }
    val PLAYBACK_DURATION by lazy {
        Preference.FloatPreference( preferences, "AudioVolume", "playbackVolume", 0F )
    }
    val CUSTOM_COLOR_HASH_CODE by lazy {
        Preference.IntPreference( preferences, "CustomColorHashCode", "customColor", Color.Green.hashCode() )
    }
    val SEARCH_RESULTS_TAB_INDEX by lazy {
        Preference.IntPreference( preferences, "SearchResultsTabIndex", "searchResultScreenTabIndex", 0 )
    }
    val HOME_TAB_INDEX by lazy {
        Preference.IntPreference( preferences, "HomeTabIndex", "homeScreenTabIndex", 0 )
    }
    val ARTIST_SCREEN_TAB_INDEX  by lazy {
        Preference.IntPreference( preferences, "ArtistScreenTabIndex", "artistScreenTabIndex", 0 )
    }

    /**
     * Initialize needed properties for settings to use.
     *
     * **ATTENTION**: Must be call as early as possible to prevent
     * because all preference require [preferences] to be initialized
     * to work.
     */
    fun load( context: Context ) {
        this.preferences = context.getSharedPreferences( "preferences", Context.MODE_PRIVATE )
    }

    /**
     * Finalize all changes and write it to disk.
     *
     * This is a blocking call.
     *
     * **NOTE**: Should only be called when the app
     * is about to close to make sure all settings are saved
     */
    @SuppressLint("UseKtx", "ApplySharedPref")      // Use conventional syntax because it's easier to read
    @Blocking
    fun unload() = this.preferences.edit().commit()

    /**
     * Represents an individual setting entry from **_preferences_** file.
     *
     * @param sharedPreferences a class that holds all entries of preferences file
     * @param key of the entry, used to extract/write data to preferences file
     * @param previousKey for backward compatibility, when key changed,
     * this will be used to extract old value to be used with new key
     * @param defaultValue if key doesn't exist in preferences, this value will be written
     * to it, and used as current value
     */
    sealed class Preference<T>(
        protected val sharedPreferences: SharedPreferences,
        val key: String,
        val previousKey: String,
        val defaultValue: T
    ): MutableState<T> {

        /**
         * How old and new value are processed
         */
        protected abstract val policy: SnapshotMutationPolicy<T>

        /**
         * Extract value from [SharedPreferences]. Return value
         * must be `null` if [key] doesn't exist inside preferences file.
         *
         * @return value of this preference, `null` if [key] doesn't exist
         */
        protected abstract fun getFromSharedPreferences(): T?

        /**
         * Write [value] into [SharedPreferences] instance.
         *
         * This is a non-blocking calls. Meaning, all writes
         * are temporary written to memory first, then sync
         * value to disk asynchronously.
         */
        @NonBlocking
        protected abstract fun write( value: T )

        override fun component1(): T = value

        override fun component2(): (T) -> Unit = { value = it }

        protected inner class StructuralEqualityPolicy: SnapshotMutationPolicy<T> {
            override fun equivalent( a: T, b: T ): Boolean {
                if( a != b ) write( b )
                return a == b
            }
        }

        protected inner class ReferentialEqualityPolicy: SnapshotMutationPolicy<T> {
            override fun equivalent( a: T, b: T ): Boolean {
                if( a !== b ) write( b )
                return a === b
            }
        }

        protected inner class DecimalEqualityPolicy: SnapshotMutationPolicy<T> {
            override fun equivalent( a: T, b: T ): Boolean {
                require( a is Comparable<*> && b is Comparable<*> && a!!::class == b!!::class )

                @Suppress("UNCHECKED_CAST")
                val areEqual = (a as Comparable<Any>).compareTo( b ) == 0
                if( !areEqual ) write( b )

                return areEqual
            }
        }

        class EnumPreference<E: Enum<E>>(
            sharedPreferences: SharedPreferences,
            key: String,
            previousKey: String,
            defaultValue: E
        ): Preference<E>(sharedPreferences, key, previousKey, defaultValue) {

            override val policy = ReferentialEqualityPolicy()

            override var value: E by mutableStateOf(
                value = getFromSharedPreferences() ?: defaultValue.also( ::write ),
                policy = this.policy
            )

            override fun getFromSharedPreferences(): E? {
                var fromFile: String? = null

                /*
                     Set [fromFile] to the value of [previousKey] if it's
                     existed in the preferences file, then delete that key
                     (for migration to new key)
                 */
                if( sharedPreferences.contains( previousKey ) ) {
                    fromFile = sharedPreferences.getString( previousKey, null )
                    sharedPreferences.edit( commit = true ) {
                        remove( previousKey )

                        // Add this value to new [key], otherwise, only old key
                        // will be removed and new key is not added until next start
                        // with default value
                        fromFile?.also { putString( key, it ) }
                    }
                }

                /*
                     Set [fromFile] to the value of [key] if it's
                     existed in the preferences file.

                     Reason for 2 separate steps is:
                     - When both [key] and [previousKey] are existed
                     in side the file, [previousKey] will be deleted
                     while value of [key] is being used.
                     - Or either 1 of the key will be used if only
                     1 of them existed inside the file.
                */
                if( sharedPreferences.contains( key ) )
                    fromFile = sharedPreferences.getString( key, null )

                return fromFile?.let { enumStr ->
                    defaultValue.javaClass.enumConstants?.firstOrNull { it.name == enumStr }
                }
            }

            override fun write( value: E ) =
                sharedPreferences.edit {
                    putString( key, value.name )
                }
        }

        class StringPreference(
            sharedPreferences: SharedPreferences,
            key: String,
            previousKey: String,
            defaultValue: String
        ): Preference<String>(sharedPreferences, key, previousKey, defaultValue) {

            override val policy = ReferentialEqualityPolicy()

            override var value: String by mutableStateOf(
                value = getFromSharedPreferences() ?: defaultValue.also( ::write ),
                policy = this.policy
            )

            override fun getFromSharedPreferences(): String? {
                var fromFile: String? = null

                /*
                     Set [fromFile] to the value of [previousKey] if it's
                     existed in the preferences file, then delete that key
                     (for migration to new key)
                 */
                if( sharedPreferences.contains( previousKey ) ) {
                    fromFile = sharedPreferences.getString( previousKey, null )
                    sharedPreferences.edit( commit = true ) {
                        remove( previousKey )

                        // Add this value to new [key], otherwise, only old key
                        // will be removed and new key is not added until next start
                        // with default value
                        fromFile?.also { putString( key, it ) }
                    }
                }

                /*
                     Set [fromFile] to the value of [key] if it's
                     existed in the preferences file.

                     Reason for 2 separate steps is:
                     - When both [key] and [previousKey] are existed
                     in side the file, [previousKey] will be deleted
                     while value of [key] is being used.
                     - Or either 1 of the key will be used if only
                     1 of them existed inside the file.
                */
                if( sharedPreferences.contains( key ) )
                    fromFile = sharedPreferences.getString( key, null )

                return fromFile
            }

            override fun write( value: String ) =
                sharedPreferences.edit {
                    putString( key, value )
                }
        }

        class StringSetPreference(
            sharedPreferences: SharedPreferences,
            key: String,
            previousKey: String,
            defaultValue: Set<String>
        ): Preference<Set<String>>(sharedPreferences, key, previousKey, defaultValue) {

            override val policy = StructuralEqualityPolicy()

            override var value: Set<String> by mutableStateOf(
                value = getFromSharedPreferences() ?: defaultValue.also( ::write ),
                policy = this.policy
            )

            override fun getFromSharedPreferences(): Set<String>? {
                var fromFile: Set<String>? = null

                /*
                     Set [fromFile] to the value of [previousKey] if it's
                     existed in the preferences file, then delete that key
                     (for migration to new key)
                 */
                if( sharedPreferences.contains( previousKey ) ) {
                    fromFile = sharedPreferences.getStringSet( previousKey, null )
                    sharedPreferences.edit( commit = true ) {
                        remove( previousKey )

                        // Add this value to new [key], otherwise, only old key
                        // will be removed and new key is not added until next start
                        // with default value
                        fromFile?.also { putStringSet( key, it ) }
                    }
                }

                /*
                     Set [fromFile] to the value of [key] if it's
                     existed in the preferences file.

                     Reason for 2 separate steps is:
                     - When both [key] and [previousKey] are existed
                     in side the file, [previousKey] will be deleted
                     while value of [key] is being used.
                     - Or either 1 of the key will be used if only
                     1 of them existed inside the file.
                */
                if( sharedPreferences.contains( key ) )
                    fromFile = sharedPreferences.getStringSet( key, null )

                return fromFile
            }

            override fun write( value: Set<String> ) =
                sharedPreferences.edit {
                    putStringSet( key, value )
                }
        }

        class IntPreference(
            sharedPreferences: SharedPreferences,
            key: String,
            previousKey: String,
            defaultValue: Int
        ): Preference<Int>(sharedPreferences, key, previousKey, defaultValue) {

            override val policy = DecimalEqualityPolicy()

            override var value: Int by mutableStateOf(
                value = getFromSharedPreferences() ?: defaultValue.also( ::write ),
                policy = this.policy
            )

            @Suppress("USELESS_NULLABLE")
            override fun getFromSharedPreferences(): Int? {
                var fromFile: Int? = null

                /*
                     Set [fromFile] to the value of [previousKey] if it's
                     existed in the preferences file, then delete that key
                     (for migration to new key)
                 */
                if( sharedPreferences.contains( previousKey ) ) {
                    fromFile = sharedPreferences.getInt( previousKey, defaultValue )
                    sharedPreferences.edit( commit = true ) {
                        remove( previousKey )

                        // Add this value to new [key], otherwise, only old key
                        // will be removed and new key is not added until next start
                        // with default value
                        fromFile?.also { putInt( key, it ) }
                    }
                }

                /*
                     Set [fromFile] to the value of [key] if it's
                     existed in the preferences file.

                     Reason for 2 separate steps is:
                     - When both [key] and [previousKey] are existed
                     in side the file, [previousKey] will be deleted
                     while value of [key] is being used.
                     - Or either 1 of the key will be used if only
                     1 of them existed inside the file.
                */
                if( sharedPreferences.contains( key ) )
                    fromFile = sharedPreferences.getInt( key, defaultValue )

                return fromFile
            }

            override fun write( value: Int ) =
                sharedPreferences.edit {
                    putInt( key, value )
                }
        }

        class LongPreference(
            sharedPreferences: SharedPreferences,
            key: String,
            previousKey: String,
            defaultValue: Long
        ): Preference<Long>(sharedPreferences, key, previousKey, defaultValue) {

            override val policy = DecimalEqualityPolicy()

            override var value: Long by mutableStateOf(
                value = getFromSharedPreferences() ?: defaultValue.also( ::write ),
                policy = this.policy
            )

            override fun getFromSharedPreferences(): Long? {
                var fromFile: Long? = null

                /*
                     Set [fromFile] to the value of [previousKey] if it's
                     existed in the preferences file, then delete that key
                     (for migration to new key)
                 */
                if( sharedPreferences.contains( previousKey ) ) {
                    fromFile = sharedPreferences.getLong( previousKey, defaultValue )
                    sharedPreferences.edit( commit = true ) {
                        remove( previousKey )

                        // Add this value to new [key], otherwise, only old key
                        // will be removed and new key is not added until next start
                        // with default value
                        fromFile?.also { putLong( key, it ) }
                    }
                }

                /*
                     Set [fromFile] to the value of [key] if it's
                     existed in the preferences file.

                     Reason for 2 separate steps is:
                     - When both [key] and [previousKey] are existed
                     in side the file, [previousKey] will be deleted
                     while value of [key] is being used.
                     - Or either 1 of the key will be used if only
                     1 of them existed inside the file.
                */
                if( sharedPreferences.contains( key ) )
                    fromFile = sharedPreferences.getLong( key, defaultValue )

                return fromFile
            }

            override fun write( value: Long ) =
                sharedPreferences.edit {
                    putLong( key, value )
                }
        }

        class FloatPreference(
            sharedPreferences: SharedPreferences,
            key: String,
            previousKey: String,
            defaultValue: Float
        ): Preference<Float>(sharedPreferences, key, previousKey, defaultValue) {

            override val policy = DecimalEqualityPolicy()

            override var value: Float by mutableStateOf(
                value = getFromSharedPreferences() ?: defaultValue.also( ::write ),
                policy = this.policy
            )

            override fun getFromSharedPreferences(): Float? {
                var fromFile: Float? = null

                /*
                     Set [fromFile] to the value of [previousKey] if it's
                     existed in the preferences file, then delete that key
                     (for migration to new key)
                 */
                if( sharedPreferences.contains( previousKey ) ) {
                    fromFile = sharedPreferences.getFloat( previousKey, defaultValue )
                    sharedPreferences.edit( commit = true ) {
                        remove( previousKey )

                        // Add this value to new [key], otherwise, only old key
                        // will be removed and new key is not added until next start
                        // with default value
                        fromFile?.also { putFloat( key, it ) }
                    }
                }

                /*
                     Set [fromFile] to the value of [key] if it's
                     existed in the preferences file.

                     Reason for 2 separate steps is:
                     - When both [key] and [previousKey] are existed
                     in side the file, [previousKey] will be deleted
                     while value of [key] is being used.
                     - Or either 1 of the key will be used if only
                     1 of them existed inside the file.
                */
                if( sharedPreferences.contains( key ) )
                    fromFile = sharedPreferences.getFloat( key, defaultValue )

                return fromFile
            }

            override fun write( value: Float ) =
                sharedPreferences.edit {
                    putFloat( key, value )
                }
        }

        class BooleanPreference(
            sharedPreferences: SharedPreferences,
            key: String,
            previousKey: String,
            defaultValue: Boolean
        ): Preference<Boolean>(sharedPreferences, key, previousKey, defaultValue) {

            override val policy = StructuralEqualityPolicy()

            override var value: Boolean by mutableStateOf(
                value = getFromSharedPreferences() ?: defaultValue.also( ::write ),
                policy = this.policy
            )

            override fun getFromSharedPreferences(): Boolean? {
                var fromFile: Boolean? = null

                /*
                     Set [fromFile] to the value of [previousKey] if it's
                     existed in the preferences file, then delete that key
                     (for migration to new key)
                 */
                if( sharedPreferences.contains( previousKey ) ) {
                    fromFile = sharedPreferences.getBoolean( previousKey, defaultValue )
                    sharedPreferences.edit( commit = true ) {
                        remove( previousKey )

                        // Add this value to new [key], otherwise, only old key
                        // will be removed and new key is not added until next start
                        // with default value
                        fromFile?.also { putBoolean( key, it ) }
                    }
                }

                /*
                     Set [fromFile] to the value of [key] if it's
                     existed in the preferences file.

                     Reason for 2 separate steps is:
                     - When both [key] and [previousKey] are existed
                     in side the file, [previousKey] will be deleted
                     while value of [key] is being used.
                     - Or either 1 of the key will be used if only
                     1 of them existed inside the file.
                */
                if( sharedPreferences.contains( key ) )
                    fromFile = sharedPreferences.getBoolean( key, defaultValue )

                return fromFile
            }

            override fun write( value: Boolean ) =
                sharedPreferences.edit {
                    putBoolean( key, value )
                }
        }
    }
}