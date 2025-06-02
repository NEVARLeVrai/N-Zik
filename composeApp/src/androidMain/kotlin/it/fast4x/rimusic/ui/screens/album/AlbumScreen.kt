package it.fast4x.rimusic.ui.screens.album

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastJoinToString
import androidx.compose.ui.util.fastMapNotNull
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavController
import app.kreate.android.Settings
import it.fast4x.compose.persist.PersistMapCleanup
import it.fast4x.compose.persist.persist
import it.fast4x.compose.persist.persistList
import it.fast4x.innertube.Innertube
import it.fast4x.innertube.YtMusic
import it.fast4x.rimusic.Database
import it.fast4x.rimusic.colorPalette
import it.fast4x.rimusic.enums.NavRoutes
import it.fast4x.rimusic.enums.PlayerPosition
import it.fast4x.rimusic.enums.TransitionEffect
import it.fast4x.rimusic.enums.UiType
import it.fast4x.rimusic.models.Album
import it.fast4x.rimusic.models.SongAlbumMap
import it.fast4x.rimusic.ui.components.navigation.header.AppHeader
import it.fast4x.rimusic.utils.asMediaItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import me.knighthat.coil.ImageCacheFactory
import me.knighthat.utils.PropUtils


@ExperimentalTextApi
@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@UnstableApi
@Composable
fun AlbumScreen(
    navController: NavController,
    browseId: String,
    modifier: Modifier = Modifier,
    miniPlayer: @Composable () -> Unit = {}
) {
    PersistMapCleanup( tagPrefix = "album/$browseId/" )
    PersistMapCleanup( tagPrefix = "album/$browseId/alternatives" )

    // Essentials
    val saveableStateHolder = rememberSaveableStateHolder()

    // Settings
    val transitionEffect by Settings.TRANSITION_EFFECT
    val playerPosition by Settings.MINI_PLAYER_POSITION

    var album by persist<Album?>("album/$browseId")
    LaunchedEffect(Unit) {
        Database.albumTable
                .findById( browseId )
                .flowOn( Dispatchers.IO )
                .distinctUntilChanged()
                .collect { album = it }
    }

    var alternatives by persistList<Innertube.AlbumItem>( "album/$browseId/alternatives" )
    var description by rememberSaveable { mutableStateOf("") }
    LaunchedEffect( Unit ) {
        YtMusic.getAlbum( browseId, true )
               .onSuccess { online ->
                   val onlineAlbum = online.album
                   val authorsText: String? = onlineAlbum.authors
                                                         ?.fastMapNotNull { it.name }
                                                         ?.fastJoinToString( "" )

                   Database.asyncTransaction {
                       albumTable.upsert(Album(
                           id = browseId,
                           title = PropUtils.retainIfModified( album?.title, onlineAlbum.title ),
                           thumbnailUrl = PropUtils.retainIfModified( album?.thumbnailUrl, onlineAlbum.thumbnail?.url ),
                           year = onlineAlbum.year,
                           authorsText = PropUtils.retainIfModified( album?.authorsText, authorsText ),
                           shareUrl = online.url,
                           timestamp = album?.timestamp ?: System.currentTimeMillis(),
                           bookmarkedAt = album?.bookmarkedAt
                       ))

                       online.songs
                             .map( Innertube.SongItem::asMediaItem )
                             .onEach( ::insertIgnore )
                             .mapIndexed { position, mediaItem ->
                                 SongAlbumMap(
                                     songId = mediaItem.mediaId,
                                     albumId = browseId,
                                     position = position
                                 )
                             }
                             .also( songAlbumMapTable::upsert )
                   }

                   alternatives = online.otherVersions
                   description = online.description ?: ""
               }
    }

    val thumbnailPainter = ImageCacheFactory.Painter( album?.thumbnailUrl )

    androidx.compose.material3.Scaffold(
        modifier = Modifier,
        containerColor = colorPalette().background0,
        topBar = {
            if( UiType.RiMusic.isCurrent() )
                AppHeader( navController ).Draw()
        }
    ) {
        Box(
            modifier = Modifier.padding(it)
                .fillMaxSize()
        ) {

            Row(
                modifier = Modifier.fillMaxSize()
                    .background( colorPalette().background0 )
            ) {
                val topPadding = if ( UiType.ViMusic.isCurrent() ) 30.dp else 0.dp

                AnimatedContent(
                    targetState = 0,
                    transitionSpec = {
                        when (transitionEffect) {
                            TransitionEffect.None -> EnterTransition.None togetherWith ExitTransition.None
                            TransitionEffect.Expand -> expandIn(
                                animationSpec = tween(
                                    350,
                                    easing = LinearOutSlowInEasing
                                ), expandFrom = Alignment.BottomStart
                            ).togetherWith(
                                shrinkOut(
                                    animationSpec = tween(
                                        350,
                                        easing = FastOutSlowInEasing
                                    ), shrinkTowards = Alignment.CenterStart
                                )
                            )

                            TransitionEffect.Fade -> fadeIn(animationSpec = tween(350)).togetherWith(
                                fadeOut(animationSpec = tween(350))
                            )

                            TransitionEffect.Scale -> scaleIn(animationSpec = tween(350)).togetherWith(
                                scaleOut(animationSpec = tween(350))
                            )

                            TransitionEffect.SlideHorizontal, TransitionEffect.SlideVertical -> {
                                val slideDirection = when (targetState > initialState) {
                                    true -> {
                                        if (transitionEffect == TransitionEffect.SlideHorizontal)
                                            AnimatedContentTransitionScope.SlideDirection.Left
                                        else AnimatedContentTransitionScope.SlideDirection.Up
                                    }

                                    false -> {
                                        if (transitionEffect == TransitionEffect.SlideHorizontal)
                                            AnimatedContentTransitionScope.SlideDirection.Right
                                        else AnimatedContentTransitionScope.SlideDirection.Down
                                    }
                                }

                                val animationSpec = spring(
                                    dampingRatio = 0.9f,
                                    stiffness = Spring.StiffnessLow,
                                    visibilityThreshold = IntOffset.VisibilityThreshold
                                )

                                slideIntoContainer(
                                    slideDirection,
                                    animationSpec
                                ) togetherWith
                                        slideOutOfContainer(slideDirection, animationSpec)
                            }
                        }
                    },
                    label = "",
                    modifier = Modifier.fillMaxHeight()
                        .padding( top = topPadding )
                ) { currentTabIndex ->
                    saveableStateHolder.SaveableStateProvider(key = currentTabIndex) {
                        when (currentTabIndex) {
                            0 -> AlbumDetails(
                                navController = navController,
                                browseId = browseId,
                                album = album,
                                thumbnailPainter = thumbnailPainter,
                                alternatives = alternatives,
                                description = description,
                                onSearchClick = {
                                    navController.navigate(NavRoutes.search.name)
                                },
                                onSettingsClick = {
                                    navController.navigate(NavRoutes.settings.name)
                                }
                            )
                        }
                    }
                }
            }

            //**
            Box(
                modifier = modifier.padding( vertical = 5.dp ).align(
                    if( playerPosition == PlayerPosition.Top ) Alignment.TopCenter else Alignment.BottomCenter
                )
            ) {
                miniPlayer.invoke()
            }
        }
    }
}
