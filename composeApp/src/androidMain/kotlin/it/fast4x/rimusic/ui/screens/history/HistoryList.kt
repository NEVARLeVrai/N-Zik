package it.fast4x.rimusic.ui.screens.history

import androidx.annotation.OptIn
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastDistinctBy
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavController
import app.kreate.android.R
import app.kreate.android.Settings
import it.fast4x.compose.persist.persist
import it.fast4x.innertube.YtMusic
import it.fast4x.innertube.requests.HistoryPage
import it.fast4x.rimusic.Database
import it.fast4x.rimusic.EXPLICIT_PREFIX
import it.fast4x.rimusic.LocalPlayerAwareWindowInsets
import it.fast4x.rimusic.LocalPlayerServiceBinder
import it.fast4x.rimusic.colorPalette
import it.fast4x.rimusic.enums.HistoryType
import it.fast4x.rimusic.enums.NavigationBarPosition
import it.fast4x.rimusic.models.Event
import it.fast4x.rimusic.thumbnailShape
import it.fast4x.rimusic.ui.components.ButtonsRow
import it.fast4x.rimusic.ui.components.LocalMenuState
import it.fast4x.rimusic.ui.components.SwipeablePlaylistItem
import it.fast4x.rimusic.ui.components.themed.NonQueuedMediaItemMenuLibrary
import it.fast4x.rimusic.ui.components.themed.Title
import it.fast4x.rimusic.ui.screens.settings.isYouTubeLoggedIn
import it.fast4x.rimusic.ui.styling.Dimensions
import it.fast4x.rimusic.utils.asMediaItem
import it.fast4x.rimusic.utils.asSong
import it.fast4x.rimusic.utils.forcePlay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import it.fast4x.rimusic.utils.addNext
import it.fast4x.rimusic.utils.enqueue
import me.knighthat.component.tab.Search
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.animation.AnimatedVisibility
import it.fast4x.rimusic.ui.components.themed.HeaderWithIcon
import it.fast4x.rimusic.ui.styling.favoritesIcon

@kotlin.OptIn(ExperimentalTextApi::class)
@OptIn(UnstableApi::class)
@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun HistoryList(
    navController: NavController
) {
    val context = LocalContext.current
    val binder = LocalPlayerServiceBinder.current
    val menuState = LocalMenuState.current
    val lazyListState = rememberLazyListState()

    val parentalControlEnabled by Settings.PARENTAL_CONTROL
    val disableScrollingText by Settings.SCROLLING_TEXT_DISABLED

    val search = Search(lazyListState)

    /**
     * Topology:
     *
     * Get all events with its song attached to it as.
     * On each event, calculate days since [Event.timestamp] is assigned.
     *
     * There are 5 categories of this:
     * - "Today",
     * - "Yesterday",
     * - "This week",
     * - "Last week",
     * - The rest are represented in "MMM yyyy" format
     *
     * This function will group each event into their correct category
     * and **remember** it.
     */
    val events by remember {
        Database.eventTable
                .allWithSong()
                .distinctUntilChanged()
                .map { list ->
                    val today = java.time.LocalDate.now()
                    val yesterday = today.minusDays(1)
                    list.filter { !parentalControlEnabled || it.song.title.startsWith( EXPLICIT_PREFIX, true ) }
                        .reversed()
                        .groupBy {
                            val eventDate = java.time.Instant.ofEpochMilli(it.event.timestamp)
                                .atZone(java.time.ZoneId.systemDefault())
                                .toLocalDate()
                            when {
                                eventDate.isEqual(today) -> context.getString(R.string.today)
                                eventDate.isEqual(yesterday) -> context.getString(R.string.yesterday)
                                eventDate.isAfter(today.minusWeeks(1)) -> context.getString(R.string.last_week)
                                eventDate.isAfter(today.minusWeeks(2)) -> context.getString(R.string.last_week)
                                else -> SimpleDateFormat("MMM yyyy", Locale.getDefault()).format(Date(it.event.timestamp))
                            }
                        }
                }
    }.collectAsState( emptyMap(), Dispatchers.IO )

    val buttonsList = mutableListOf(HistoryType.History to stringResource(R.string.history))
    buttonsList += HistoryType.YTMHistory to stringResource(R.string.yt_history)

    var historyType by Settings.HISTORY_PAGE_TYPE

    var historyPage by persist<Result<HistoryPage>>("home/historyPage")
    LaunchedEffect(Unit, historyType) {
        if (isYouTubeLoggedIn())
            historyPage = YtMusic.getHistory()
    }

    Column (
        modifier = Modifier
            .background(colorPalette().background0)
            .fillMaxHeight()
            .fillMaxWidth(
                if( NavigationBarPosition.Right.isCurrent() )
                    Dimensions.contentWidthRightBar
                else
                    1f
            )
    ) {
        LazyColumn(
            state = lazyListState,
            contentPadding = LocalPlayerAwareWindowInsets.current
                .only(WindowInsetsSides.Vertical + WindowInsetsSides.End).asPaddingValues(),
            modifier = Modifier
                .background(colorPalette().background0)
                .fillMaxSize()
        ) {
            item(key = "header", contentType = 0) {
                HeaderWithIcon(
                    title = stringResource(R.string.history),
                    iconId = R.drawable.history,
                    enabled = false,
                    showIcon = false,
                    modifier = Modifier,
                    onClick = {}
                )
            }

            item(
                key = "tabList", contentType = 0,
            ) {
                Row(
                    modifier = Modifier
                        .padding(start = 12.dp, end = 12.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ButtonsRow(
                        chips = buttonsList,
                        currentValue = historyType,
                        onValueUpdate = { historyType = it },
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = { search.isVisible = !search.isVisible },
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.search_circle),
                            contentDescription = stringResource(R.string.search),
                            tint = colorPalette().favoritesIcon
                        )
                    }
                }
            }

            item(key = "searchBar", contentType = 0) {
                AnimatedVisibility(
                    visible = search.isVisible,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    search.SearchBar(this@Column)
                }
            }

            if( historyType == HistoryType.History )
                events.forEach { (headerStr, details) ->
                    stickyHeader {
                        Title(
                            title = headerStr,
                            modifier = Modifier.background(
                                color = colorPalette().background3,
                                shape = thumbnailShape()
                            )
                        )
                    }

                    items(
                        items = details.fastDistinctBy { it.song.id }
                            .filter { event ->
                                event.song.title.contains(search.inputValue, ignoreCase = true) ||
                                (event.song.artistsText ?: "").contains(search.inputValue, ignoreCase = true)
                            },
                        key = { it.event.id }
                    ) { event ->
                        SwipeablePlaylistItem(
                            mediaItem = event.song.asMediaItem,
                            onPlayNext = {
                                binder?.player?.addNext(event.song.asMediaItem)
                            },
                            onEnqueue = {
                                binder?.player?.enqueue(event.song.asMediaItem)
                            }
                        ) {
                            me.knighthat.component.SongItem(
                                song = event.song,
                                navController = navController,
                                onClick = {
                                    binder?.player?.forcePlay(event.song.asMediaItem)
                                }
                            )
                        }
                    }
                }

            if ( historyType == HistoryType.YTMHistory )
                historyPage?.getOrNull()?.sections?.forEach { section ->
                    stickyHeader {
                        Title(
                            title = section.title,
                            modifier = Modifier.background(
                                color = colorPalette().background3,
                                shape = thumbnailShape()
                            )
                        )
                    }

                    items(
                        items = section.songs
                                       .map { it.asMediaItem }
                                       .filter { it.mediaId.isNotEmpty() }
                                       .filter { mediaItem ->
                                           (mediaItem.mediaMetadata.title ?: "").contains(search.inputValue, ignoreCase = true) ||
                                           (mediaItem.mediaMetadata.artist ?: "").contains(search.inputValue, ignoreCase = true)
                                       },
                        key = { it.mediaId }
                    ) { mediaItem ->
                        SwipeablePlaylistItem(
                            mediaItem = mediaItem,
                            onPlayNext = {
                                binder?.player?.addNext(mediaItem)
                            },
                            onEnqueue = {
                                binder?.player?.enqueue(mediaItem)
                            }
                        ) {
                            me.knighthat.component.SongItem(
                                song = mediaItem.asSong,
                                navController = navController,
                                onClick = {
                                    binder?.player?.forcePlay(mediaItem)
                                },
                                onLongClick = {
                                    menuState.display {
                                        NonQueuedMediaItemMenuLibrary(
                                            navController = navController,
                                            mediaItem = mediaItem,
                                            onDismiss = menuState::hide,
                                            disableScrollingText = disableScrollingText
                                        )
                                    }
                                }
                            )
                        }
                    }
                }
        }
    }
}

