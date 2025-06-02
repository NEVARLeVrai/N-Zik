package it.fast4x.rimusic.utils

import android.widget.Toast
import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.media3.common.util.UnstableApi
import app.kreate.android.R
import app.kreate.android.Settings
import it.fast4x.rimusic.Database
import it.fast4x.rimusic.LocalPlayerServiceBinder
import kotlinx.coroutines.flow.first
import me.knighthat.utils.Toaster

@OptIn(UnstableApi::class)
@Composable
fun ApplyDiscoverToQueue() {
    /*   DISCOVER  */
    val discoverIsEnabled by Settings.ENABLE_DISCOVER
    if (!discoverIsEnabled) return

    val binder = LocalPlayerServiceBinder.current

    binder?.player ?: return

    val player = binder.player

    var listMediaItemsIndex = remember {
        mutableListOf<Int>()
    }
    val windows by remember {
        mutableStateOf(player.currentTimeline.windows)
    }

    LaunchedEffect(Unit) {
        listMediaItemsIndex.clear()
        windows.forEach { window ->
            if( window.firstPeriodIndex == player.currentMediaItemIndex )
                return@forEach

            val mediaId = window.mediaItem.mediaId
            val isMappedToAPlaylist = Database.songPlaylistMapTable.isMapped( mediaId ).first()
            val isLiked = Database.songTable.isLiked( mediaId ).first()

            if( isMappedToAPlaylist && isLiked )
                listMediaItemsIndex.add(window.firstPeriodIndex)

        }.also {
            if (listMediaItemsIndex.isNotEmpty()) {
                val mediacount = listMediaItemsIndex.size - 1
                listMediaItemsIndex.sort()
                for (i in mediacount.downTo(0)) {
                    binder.player.removeMediaItem(listMediaItemsIndex[i])
                }
                Toaster.s(
                    R.string.discover_has_been_applied_to_queue,
                    listMediaItemsIndex.size,
                    duration = Toast.LENGTH_SHORT
                )
                listMediaItemsIndex.clear()
            }
        }

    }

    /*   DISCOVER  */

}