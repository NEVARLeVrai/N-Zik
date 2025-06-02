package it.fast4x.rimusic.enums

import androidx.compose.runtime.Composable
import app.kreate.android.Settings

enum class UiType {
    RiMusic,
    ViMusic;

    companion object {

        fun current(): UiType = Settings.MAIN_THEME.value
    }

    @Composable
    fun isCurrent(): Boolean = current() == this

    @Composable
    fun isNotCurrent(): Boolean = !isCurrent()
}