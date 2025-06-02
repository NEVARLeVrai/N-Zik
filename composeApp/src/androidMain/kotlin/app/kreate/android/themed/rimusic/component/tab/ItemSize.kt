package app.kreate.android.themed.rimusic.component.tab

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import app.kreate.android.R
import app.kreate.android.Settings
import it.fast4x.rimusic.enums.HomeItemSize
import it.fast4x.rimusic.ui.components.MenuState
import it.fast4x.rimusic.ui.components.tab.toolbar.Descriptive
import it.fast4x.rimusic.ui.components.tab.toolbar.MenuIcon
import it.fast4x.rimusic.ui.components.themed.Menu
import it.fast4x.rimusic.ui.components.themed.MenuEntry

class ItemSize(
    preference: Settings.Preference<HomeItemSize>,
    private val menuState: MenuState
): MenuIcon, Descriptive {

    var size: HomeItemSize by preference

    override val iconId: Int = R.drawable.resize
    override val messageId: Int = R.string.size
    override val menuIconTitle: String
        @Composable
        get() = stringResource( R.string.size )

    @Composable
    private fun Entry( size: HomeItemSize) {
        MenuEntry(
            size.icon,
            size.text,
            onClick = {
                this.size = size
                menuState.hide()
            }
        )
    }

    override fun onShortClick() = menuState.display {
        Menu {
            HomeItemSize.entries.forEach { Entry(it) }
        }
    }
}