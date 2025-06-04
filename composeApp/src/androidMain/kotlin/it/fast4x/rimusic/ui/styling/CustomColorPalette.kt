package it.fast4x.rimusic.ui.styling

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import app.kreate.android.Settings
import it.fast4x.rimusic.enums.ColorPaletteMode


fun customColorPalette(colorPalette: ColorPalette, context: Context, isSystemInDarkTheme: Boolean): ColorPalette {
    val colorPaletteMode by Settings.THEME_MODE

    val customThemeLight = colorPalette.copy(
        background0 = Color(Settings.CUSTOM_LIGHT_THEME_BACKGROUND_0_HASH_CODE.value),
        background1 = Color(Settings.CUSTOM_LIGHT_THEME_BACKGROUND_1_HASH_CODE.value),
        background2 = Color(Settings.CUSTOM_LIGHT_THEME_BACKGROUND_2_HASH_CODE.value),
        background3 = Color(Settings.CUSTOM_LIGHT_THEME_BACKGROUND_3_HASH_CODE.value),
        background4 = Color(Settings.CUSTOM_LIGHT_THEME_BACKGROUND_4_HASH_CODE.value),
        text = Color(Settings.CUSTOM_LIGHT_TEXT_HASH_CODE.value),
        textSecondary = Color(Settings.CUSTOM_LIGHT_TEXT_SECONDARY_HASH_CODE.value),
        textDisabled = Color(Settings.CUSTOM_LIGHT_TEXT_DISABLED_HASH_CODE.value),
        iconButtonPlayer = Color(Settings.CUSTOM_LIGHT_PLAY_BUTTON_HASH_CODE.value),
        accent = Color(Settings.CUSTOM_LIGHT_ACCENT_HASH_CODE.value)
    )

    val customThemeDark = colorPalette.copy(
        background0 = Color(Settings.CUSTOM_DARK_THEME_BACKGROUND_0_HASH_CODE.value),
        background1 = Color(Settings.CUSTOM_DARK_THEME_BACKGROUND_1_HASH_CODE.value),
        background2 = Color(Settings.CUSTOM_DARK_THEME_BACKGROUND_2_HASH_CODE.value),
        background3 = Color(Settings.CUSTOM_DARK_THEME_BACKGROUND_3_HASH_CODE.value),
        background4 = Color(Settings.CUSTOM_DARK_THEME_BACKGROUND_4_HASH_CODE.value),
        text = Color(Settings.CUSTOM_DARK_TEXT_HASH_CODE.value),
        textSecondary = Color(Settings.CUSTOM_DARK_TEXT_SECONDARY_HASH_CODE.value),
        textDisabled = Color(Settings.CUSTOM_DARK_TEXT_DISABLED_HASH_CODE.value),
        iconButtonPlayer = Color(Settings.CUSTOM_DARK_PLAY_BUTTON_HASH_CODE.value),
        accent = Color(Settings.CUSTOM_DARK_ACCENT_HASH_CODE.value)
    )

    return when (colorPaletteMode) {
        ColorPaletteMode.Dark, ColorPaletteMode.PitchBlack -> customThemeDark
        ColorPaletteMode.Light -> customThemeLight
        ColorPaletteMode.System -> when (isSystemInDarkTheme) {
            true -> customThemeDark
            false -> customThemeLight
        }
    }
}
