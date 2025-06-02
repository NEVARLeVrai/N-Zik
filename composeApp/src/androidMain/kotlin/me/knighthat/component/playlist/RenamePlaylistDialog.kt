package me.knighthat.component.playlist

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import app.kreate.android.R
import app.kreate.android.Settings
import it.fast4x.rimusic.Database
import it.fast4x.rimusic.PIPED_PREFIX
import it.fast4x.rimusic.appContext
import it.fast4x.rimusic.cleanPrefix
import it.fast4x.rimusic.models.Playlist
import it.fast4x.rimusic.utils.getPipedSession
import it.fast4x.rimusic.utils.renamePipedPlaylist
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import me.knighthat.component.RenameDialog
import java.util.UUID

class RenamePlaylistDialog private constructor(
    activeState: MutableState<Boolean>,
    valueState: MutableState<TextFieldValue>,
    private val pipedActiveState: MutableState<Boolean>,
    private val getPlaylist: () -> Playlist?,
): RenameDialog(activeState, valueState) {

    companion object {
        @Composable
        operator fun invoke( getPlaylist: () -> Playlist? ) =
            RenamePlaylistDialog(
                remember { mutableStateOf(false) },
                remember( getPlaylist()?.name ) {
                    mutableStateOf( TextFieldValue(getPlaylist()?.name ?: "") )
                },
                Settings.ENABLE_PIPED,
                getPlaylist
            )
    }

    override val iconId: Int = R.drawable.title_edit
    override val messageId: Int = R.string.rename_playlist
    override val keyboardOption: KeyboardOptions = KeyboardOptions.Default
    override val dialogTitle: String
        @Composable
        get() = stringResource( R.string.enter_the_playlist_name )
    override val menuIconTitle: String
        @Composable
        get() = stringResource( messageId )

    override fun hideDialog() {
        super.hideDialog()
        // Always reset string so when dialog turns
        // back on it will not show previous value.
        value = TextFieldValue(getPlaylist()?.name ?: "")
    }

    override fun onSet( newValue: String ) {
        super.onSet( newValue )
        if( errorMessage.isNotEmpty() ) return

        val playlist = getPlaylist() ?: return

        val pipedSession = getPipedSession()
        val isPipedPlaylist =
            playlist.name.startsWith( PIPED_PREFIX, true )
                    && pipedActiveState.value
                    && pipedSession.token.isNotEmpty()
        val prefix = if( isPipedPlaylist ) PIPED_PREFIX else ""

        Database.asyncTransaction {
            playlist.copy( name = "$prefix$newValue" )
                    .let( playlistTable::update )
        }

        if ( isPipedPlaylist )
            renamePipedPlaylist(
                context = appContext(),
                coroutineScope = CoroutineScope( Dispatchers.IO ),
                pipedSession = pipedSession.toApiSession(),
                id = UUID.fromString( cleanPrefix(playlist.browseId ?: "") ),
                name = "$PIPED_PREFIX$newValue"
            )

        hideDialog()
    }
}