package it.fast4x.rimusic.enums

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import app.kreate.android.R
import app.kreate.android.Settings
import me.knighthat.enums.TextView

enum class NavigationBarPosition(
    @field:StringRes override val textId: Int
): TextView {

    Left( R.string.direction_left ),
    Right( R.string.direction_right ),
    Top( R.string.direction_top ),
    Bottom( R.string.direction_bottom );

    companion object {

        fun current() = Settings.NAVIGATION_BAR_POSITION.value
    }

    @Composable
    fun isCurrent(): Boolean = current() == this
}