package it.fast4x.rimusic.ui.components.themed

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import app.kreate.android.Settings
import coil.Coil
import coil.annotation.ExperimentalCoilApi
import it.fast4x.rimusic.LocalPlayerServiceBinder
import it.fast4x.rimusic.enums.CacheType
import it.fast4x.rimusic.enums.ExoPlayerDiskCacheMaxSize
import it.fast4x.rimusic.enums.ExoPlayerDiskDownloadCacheMaxSize


@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(ExperimentalCoilApi::class)
@Composable
fun CacheSpaceIndicator(
    cacheType: CacheType = CacheType.Images,
    circularIndicator: Boolean = false,
    horizontalPadding: Dp = 12.dp,
) {

    val coilDiskCacheMaxSize by Settings.THUMBNAIL_CACHE_SIZE
    val exoPlayerDiskCacheMaxSize by Settings.SONG_CACHE_SIZE
    val exoPlayerDiskDownloadCacheMaxSize by Settings.SONG_DOWNLOAD_SIZE

    when (cacheType) {
        CacheType.Images -> {}
        CacheType.CachedSongs -> {
            if (exoPlayerDiskCacheMaxSize == ExoPlayerDiskCacheMaxSize.Unlimited) return
        }
        CacheType.DownloadedSongs -> {
            if (exoPlayerDiskDownloadCacheMaxSize == ExoPlayerDiskDownloadCacheMaxSize.Unlimited) return
        }
    }

    val context = LocalContext.current
    val binder = LocalPlayerServiceBinder.current

    val imageDiskCacheSize = remember {
        Coil.imageLoader(context).diskCache?.size
    }

    val cachedSongsDiskCacheSize = remember {
        binder?.cache?.cacheSpace
    }

    val downloadedSongsDiskCacheSize = remember {
        binder?.downloadCache?.cacheSpace
    }

    val progressValue = remember { mutableStateOf(0f) }

    LaunchedEffect (Unit, cacheType) {
        progressValue.value =
        when (cacheType) {
            CacheType.Images -> imageDiskCacheSize?.toFloat()
                ?.div(coilDiskCacheMaxSize.bytes.coerceAtLeast(1)) ?: 0.0f
            CacheType.CachedSongs -> cachedSongsDiskCacheSize?.toFloat()
                ?.div(exoPlayerDiskCacheMaxSize.bytes.coerceAtLeast(1)) ?: 0.0f
            CacheType.DownloadedSongs -> downloadedSongsDiskCacheSize?.toFloat()
                ?.div(exoPlayerDiskDownloadCacheMaxSize.bytes.coerceAtLeast(1)) ?: 0.0f
        }
    }

    if (!circularIndicator)
        ProgressIndicator(
            progress = progressValue.value,
            strokeCap = StrokeCap.Round,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .padding(horizontal = horizontalPadding)
        )
    else
        ProgressIndicatorCircular(
            progress = progressValue.value,
            strokeCap = StrokeCap.Round,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .padding(horizontal = horizontalPadding)
        )
}