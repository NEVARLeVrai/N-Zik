package me.knighthat.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.offline.Download
import androidx.navigation.NavController
import app.kreate.android.R
import app.kreate.android.Settings
import it.fast4x.rimusic.Database
import it.fast4x.rimusic.EXPLICIT_PREFIX
import it.fast4x.rimusic.LocalPlayerServiceBinder
import it.fast4x.rimusic.cleanPrefix
import it.fast4x.rimusic.colorPalette
import it.fast4x.rimusic.enums.DownloadedStateMedia
import it.fast4x.rimusic.enums.NavRoutes
import it.fast4x.rimusic.models.Song
import it.fast4x.rimusic.service.MyDownloadHelper
import it.fast4x.rimusic.service.modern.isLocal
import it.fast4x.rimusic.typography
import it.fast4x.rimusic.ui.components.tab.toolbar.Clickable
import it.fast4x.rimusic.ui.components.tab.toolbar.Descriptive
import it.fast4x.rimusic.ui.components.tab.toolbar.Icon
import it.fast4x.rimusic.ui.components.themed.HeaderIconButton
import it.fast4x.rimusic.ui.components.themed.IconButton
import it.fast4x.rimusic.ui.components.themed.NowPlayingSongIndicator
import it.fast4x.rimusic.ui.styling.Dimensions
import it.fast4x.rimusic.ui.styling.favoritesIcon
import it.fast4x.rimusic.ui.styling.favoritesOverlay
import it.fast4x.rimusic.utils.conditional
import it.fast4x.rimusic.utils.downloadedStateMedia
import it.fast4x.rimusic.utils.getDownloadState
import it.fast4x.rimusic.utils.getLikedIcon
import it.fast4x.rimusic.utils.isNowPlaying
import it.fast4x.rimusic.utils.medium
import it.fast4x.rimusic.utils.secondary
import it.fast4x.rimusic.utils.semiBold
import kotlinx.coroutines.Dispatchers
import me.knighthat.coil.ImageCacheFactory
import me.knighthat.component.menu.song.SongItemMenu
import me.knighthat.component.tab.ItemSelector

private interface SongIndicator: Icon {
    override val sizeDp: Dp
        get() = 18.dp
    override val color: Color
        @Composable
        get() = colorPalette().accent

    override fun onShortClick() { /* Does nothing */ }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    override fun ToolBarButton() {
        val modifier = this.modifier
                                     .size(sizeDp)
                                     .combinedClickable(
                                         onClick = ::onShortClick,
                                         onLongClick = {
                                             if(this is Clickable)
                                                 this.onLongClick()
                                         }
                                     )

        IconButton(
            icon = iconId,
            color = color,
            enabled = isEnabled,
            onClick = {},
            modifier = modifier
        )

        Spacer( Modifier.padding(horizontal = 3.dp) )
    }
}

@Composable
fun SongText(
    text: String,
    style: TextStyle,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    modifier: Modifier = Modifier
) = BasicText(
    text = text,
    style = style,
    maxLines = 1,
    overflow = overflow,
    modifier = modifier
)

/**
 *  Displays information of a song.
 *
 *  [onLongClick] overrides song's menu
 *
 *  @param song record from database
 *  @param itemSelector optional field to enable select box
 *  @param navController optional field to detect whether the
 *  current location is playlist to hide playlist indicator.
 *  @param isRecommended whether this song is selected by algorithm
 *  @param modifier applied to the outermost layer but not its content
 *  @param showThumbnail whether to fetch/show thumbnail of this song.
 *  [thumbnailOverlay] will still being shown regardless the state of this value.
 *  @param onLongClick what happens when user holds this item for a short while
 *  @param trailingContent content being placed to the rightmost of the card
 *  @param onClick what happens when user tap on this item
 */
@UnstableApi
@ExperimentalFoundationApi
@Composable
fun SongItem(
    song: Song,
    itemSelector: ItemSelector<Song>? = null,
    navController: NavController? = null,
    isRecommended: Boolean = false,
    modifier: Modifier = Modifier,
    showThumbnail: Boolean = true,
    onLongClick: (() -> Unit)? = null,
    trailingContent: @Composable (RowScope.() -> Unit)? = null,
    thumbnailOverlay: @Composable BoxScope.() -> Unit = {},
    onClick: () -> Unit = {}
) {
    // Essentials
    val context = LocalContext.current
    val binder = LocalPlayerServiceBinder.current
    val disableScrollingText by Settings.SCROLLING_TEXT_DISABLED
    val hapticFeedback = LocalHapticFeedback.current

    val colorPalette = colorPalette()
    val isPlaying = binder?.player?.isNowPlaying( song.id ) ?: false

    val menu = if( navController != null && onLongClick == null ) SongItemMenu( navController, song ) else null
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy( 12.dp ),
        modifier = modifier.clip( RoundedCornerShape(10.dp) )
                           .fillMaxWidth()
                           .conditional( isPlaying ) {
                               background( colorPalette.favoritesOverlay )
                           }
                           // This component must be placed before padding to prevent
                           // ripple effect to only highlight padded area
                           .combinedClickable(
                               onClick = onClick,
                               onLongClick = {
                                   hapticFeedback.performHapticFeedback( HapticFeedbackType.LongPress )

                                   onLongClick?.invoke()
                                   menu?.openMenu()
                               }
                           )
                           .padding(
                               vertical = Dimensions.itemsVerticalPadding,
                               horizontal = 16.dp
                           )
    ) {
        // Song's thumbnail
        Box(
            Modifier.size( Dimensions.thumbnails.song )
        ) {
            // Actual thumbnail (from cache or fetch from url)
            if( showThumbnail )
                ImageCacheFactory.Thumbnail(
                    thumbnailUrl = song.thumbnailUrl,
                    contentScale = ContentScale.FillHeight
                )

            if( isPlaying )
                NowPlayingSongIndicator(
                    mediaId = song.id,
                    player = binder?.player
                )

            thumbnailOverlay()

            if( song.likedAt != null )
                HeaderIconButton(
                    onClick = {},
                    icon = getLikedIcon(),
                    color = colorPalette().favoritesIcon,
                    iconSize = 12.dp,
                    modifier = Modifier.align( Alignment.BottomStart )
                                       .absoluteOffset( x = (-8).dp )
                )
        }

        // Song's information
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy( 4.dp ),
            modifier = Modifier.weight( 1f )
        ) {
            Row( verticalAlignment = Alignment.CenterVertically ) {
                // Show icon if song is recommended by the algorithm
                if( isRecommended )
                    object: SongIndicator {
                        override val iconId: Int = R.drawable.smart_shuffle
                    }.ToolBarButton()

                val showInPlaylistIndicator by Settings.SHOW_PLAYLIST_INDICATOR
                val isInPlaylistScreen = navController != null && NavRoutes.localPlaylist.isHere( navController )
                // Show icon if song belongs to a playlist,
                // except when in playlist.
                if( showInPlaylistIndicator && !isInPlaylistScreen ) {

                    val isExistedInAPlaylist by remember( showInPlaylistIndicator ) {
                        Database.songPlaylistMapTable.isMapped( song.id )
                    }.collectAsState( initial = false, context = Dispatchers.IO )

                    if( isExistedInAPlaylist )
                        object: SongIndicator, Descriptive {
                            override val iconId: Int = R.drawable.add_in_playlist
                            override val messageId: Int = R.string.playlistindicatorinfo2
                            override val sizeDp: Dp = 10.dp
                            override val modifier: Modifier =
                                Modifier.background(colorPalette().accent, CircleShape)
                                        .padding(all = 3.dp)
                            override val color: Color
                                @Composable
                                get() = colorPalette.text

                            override fun onShortClick() = super.onShortClick()
                        }.ToolBarButton()
                }

                if( song.title.startsWith(EXPLICIT_PREFIX) )
                    object: SongIndicator {
                        override val iconId: Int = R.drawable.explicit
                    }.ToolBarButton()

                // Song's name
                SongText(
                    text = cleanPrefix( song.title ),
                    style = typography().xs.semiBold,
                    modifier = Modifier.weight( 1f )
                                       .conditional( !disableScrollingText ) {
                                           basicMarquee( iterations = Int.MAX_VALUE )
                                       }
                )
            }

            Row( verticalAlignment = Alignment.CenterVertically ) {
                // Song's author
                SongText(
                    text = song.cleanArtistsText(),
                    style = typography().xs.semiBold.secondary,
                    overflow = TextOverflow.Clip,
                    modifier = Modifier.weight( 1f )
                                       .conditional( !disableScrollingText ) {
                                           basicMarquee( iterations = Int.MAX_VALUE )
                                       }
                )

                /*
                    Song's duration
                    If it's "null", show --:-- instead of leaving it empty
                 */
                SongText(
                    text = song.durationText ?: "--:--",
                    style = typography().xxs.secondary.medium,
                    modifier = Modifier.padding( top = 4.dp, start = 5.dp  )
                )

                Spacer( Modifier.padding(horizontal = 4.dp) )

                // Show download icon when song is NOT local
                if( !song.isLocal ) {
                    val cacheState = downloadedStateMedia( song.id )
                    val downloadState = getDownloadState( song.id )

                    val icon = when( downloadState ) {
                        Download.STATE_DOWNLOADING  -> R.drawable.download_progress
                        Download.STATE_REMOVING     -> R.drawable.download
                        else                        -> cacheState.iconId
                    }
                    val color = when( cacheState ) {
                        DownloadedStateMedia.NOT_CACHED_OR_DOWNLOADED -> colorPalette().textDisabled
                        else                                          -> colorPalette().text
                    }

                    IconButton(
                        icon = icon,
                        color = color,
                        modifier = Modifier.size( 20.dp ),
                        onClick = {
                            // TODO: Confirmation dialog upon delete
                            binder?.cache?.removeResource( song.id )
                            Database.asyncTransaction {
                                formatTable.deleteBySongId( song.id )
                            }

                            MyDownloadHelper.handleDownload( context, song, true )
                        }
                    )
                }
            }
        }

        if( itemSelector != null ) {
            // It must watch for [selectedItems.size] for changes
            // Otherwise, state will stay the same
            val checkedState = remember( itemSelector.size ) {
                mutableStateOf( song in itemSelector )
            }

            if( itemSelector.isActive )
                Checkbox(
                    checked = checkedState.value,
                    onCheckedChange = {
                        checkedState.value = it
                        if ( it )
                            itemSelector.add( song )
                        else
                            itemSelector.remove( song )
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = colorPalette().accent,
                        uncheckedColor = colorPalette().text
                    ),
                    modifier = Modifier.scale( 0.7f )
                )
        }

        trailingContent?.invoke( this )
    }
}