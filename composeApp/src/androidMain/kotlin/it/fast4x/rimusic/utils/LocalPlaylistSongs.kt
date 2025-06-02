package it.fast4x.rimusic.utils

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.stringResource
import app.kreate.android.R
import it.fast4x.rimusic.enums.SortOrder
import it.fast4x.rimusic.ui.components.LocalMenuState
import it.fast4x.rimusic.ui.components.MenuState
import it.fast4x.rimusic.ui.components.tab.toolbar.Descriptive
import it.fast4x.rimusic.ui.components.tab.toolbar.DualIcon
import it.fast4x.rimusic.ui.components.tab.toolbar.DynamicColor
import it.fast4x.rimusic.ui.components.tab.toolbar.MenuIcon
import it.fast4x.rimusic.ui.components.themed.DeleteDialog
import me.knighthat.utils.Toaster

class PositionLock private constructor(
    private val iconState: MutableState<Boolean>,
    private val colorState: MutableState<Boolean>
): MenuIcon, DualIcon, DynamicColor, Descriptive {

    companion object {
        @JvmStatic
        @Composable
        fun init( sortOrder: SortOrder ) = PositionLock(
            remember { mutableStateOf( false ) },
            rememberSaveable( sortOrder ) { mutableStateOf( sortOrder == SortOrder.Ascending ) }
        )
    }

    override val secondIconId: Int = R.drawable.unlocked
    override val iconId: Int = R.drawable.locked
    override val messageId: Int = R.string.info_lock_unlock_reorder_songs
    override val menuIconTitle: String
        @Composable
        get() = stringResource( messageId )

    override var isFirstIcon: Boolean = iconState.value
        set(value) {
            iconState.value = value
            field = value
        }
    override var isFirstColor: Boolean = colorState.value
        set(value) {
            colorState.value = value
            field = value
        }

    fun isLocked(): Boolean = isFirstIcon

    override fun onShortClick() {
        if( !isFirstColor )
            Toaster.i( R.string.info_reorder_is_possible_only_in_ascending_sort )
        else
            isFirstIcon = !isFirstIcon
    }
}

@SuppressLint("ComposableNaming")
@Composable
fun DeletePlaylist(
    activeState: MutableState<Boolean> = rememberSaveable { mutableStateOf( false ) },
    menuState: MenuState = LocalMenuState.current,
    onEvent: DeleteDialog.() -> Unit
): DeleteDialog = object : DeleteDialog( activeState, menuState ) {

    override val dialogTitle: String
        @Composable
        get() = stringResource( R.string.delete_playlist )

    override fun onConfirm() = onEvent()
}