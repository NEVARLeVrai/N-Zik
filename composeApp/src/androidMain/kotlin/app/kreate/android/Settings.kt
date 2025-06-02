package app.kreate.android

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SnapshotMutationPolicy
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.edit
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
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Player">
    val PLAYER_CONTROLS_TYPE by lazy {
        Preference.EnumPreference( preferences, "PlayerControlsType", "playerControlsType", PlayerControlsType.Essential )
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
    val PLAYER_NO_THUMBNAIL_SWIPE_ANIMATION by lazy {
        Preference.EnumPreference( preferences, "PlayerNoThumbnailSwipeAnimation", "swipeAnimationsNoThumbnail", SwipeAnimationNoThumbnail.Sliding )
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Cache">
    val THUMBNAIL_CACHE_SIZE by lazy {
        Preference.EnumPreference( preferences, "ThumbnailCacheSize", "coilDiskCacheMaxSize", CoilDiskCacheMaxSize.`128MB` )
    }
    val SONG_CACHE_SIZE by lazy {
        Preference.EnumPreference( preferences, "SongCacheSize", "exoPlayerDiskCacheMaxSize", ExoPlayerDiskCacheMaxSize.`2GB` )
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
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Page type">
    val QUICK_PICKS_TYPE by lazy {
        Preference.EnumPreference( preferences, "QuickPicksType", "playEventsType", PlayEventsType.MostPlayed )
    }
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

    val HOME_SONGS_TOP_PLAYLIST_PERIOD by lazy {
        Preference.EnumPreference( preferences, "HomeSongsTopPlaylistPeriod", "", StatisticsType.All )
    }
    val MENU_STYLE by lazy {
        Preference.EnumPreference( preferences, "MenuStyle", "menuStyle", MenuStyle.List )
    }
    val QUICK_PICKS_MIN_DURATION by lazy {
        Preference.EnumPreference( preferences, "QuickPicksMinDuration", "exoPlayerMinTimeForEvent", ExoPlayerMinTimeForEvent.`20s` )
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
    val AUDIO_FADE_DURATION by lazy {
        Preference.EnumPreference( preferences, "AudioFadeDuration", "playbackFadeAudioDuration", DurationInMilliseconds.Disabled )
    }
    val AUDIO_QUALITY by lazy {
        Preference.EnumPreference( preferences, "AudioQuality", "audioQualityFormat", AudioQualityFormat.Auto )
    }
    val AUDIO_REVERB_PRESET by lazy {
        Preference.EnumPreference( preferences, "AudioReverbPreset", "audioReverbPreset", PresetsReverb.NONE )
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
    val PROXY_SCHEME by lazy {
        Preference.EnumPreference( preferences, "ProxyScheme", "ProxyMode", Proxy.Type.HTTP )
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