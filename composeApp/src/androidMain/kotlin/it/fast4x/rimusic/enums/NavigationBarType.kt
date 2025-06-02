package it.fast4x.rimusic.enums

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import app.kreate.android.R
import app.kreate.android.Settings
import me.knighthat.enums.TextView


enum class NavigationBarType(
    @field:StringRes override val textId: Int
): TextView {

    IconAndText( R.string.icon_and_text ),
    IconOnly( R.string.only_icon );

    companion object {

        fun current(): NavigationBarType = Settings.NAVIGATION_BAR_TYPE.value
    }

    @Composable
    fun isCurrent(): Boolean = current() == this
}