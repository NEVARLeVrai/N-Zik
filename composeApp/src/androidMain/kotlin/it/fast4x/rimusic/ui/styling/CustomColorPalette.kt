package it.fast4x.rimusic.ui.styling

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import app.kreate.android.Settings
import it.fast4x.rimusic.enums.ColorPaletteMode
import it.fast4x.rimusic.utils.customThemeDark_Background0Key
import it.fast4x.rimusic.utils.customThemeDark_Background1Key
import it.fast4x.rimusic.utils.customThemeDark_Background2Key
import it.fast4x.rimusic.utils.customThemeDark_Background3Key
import it.fast4x.rimusic.utils.customThemeDark_Background4Key
import it.fast4x.rimusic.utils.customThemeDark_TextKey
import it.fast4x.rimusic.utils.customThemeDark_accentKey
import it.fast4x.rimusic.utils.customThemeDark_iconButtonPlayerKey
import it.fast4x.rimusic.utils.customThemeDark_textDisabledKey
import it.fast4x.rimusic.utils.customThemeDark_textSecondaryKey
import it.fast4x.rimusic.utils.customThemeLight_Background0Key
import it.fast4x.rimusic.utils.customThemeLight_Background1Key
import it.fast4x.rimusic.utils.customThemeLight_Background2Key
import it.fast4x.rimusic.utils.customThemeLight_Background3Key
import it.fast4x.rimusic.utils.customThemeLight_Background4Key
import it.fast4x.rimusic.utils.customThemeLight_TextKey
import it.fast4x.rimusic.utils.customThemeLight_accentKey
import it.fast4x.rimusic.utils.customThemeLight_iconButtonPlayerKey
import it.fast4x.rimusic.utils.customThemeLight_textDisabledKey
import it.fast4x.rimusic.utils.customThemeLight_textSecondaryKey
import it.fast4x.rimusic.utils.preferences


fun customColorPalette(colorPalette: ColorPalette, context: Context, isSystemInDarkTheme: Boolean): ColorPalette {
    val colorPaletteMode by Settings.THEME_MODE

    val customThemeLight = colorPalette.copy(
        background0 = Color(context.preferences.getInt(customThemeLight_Background0Key, DefaultLightColorPalette.background0.hashCode())),
        background1 = Color(context.preferences.getInt(customThemeLight_Background1Key, DefaultLightColorPalette.background1.hashCode())),
        background2 = Color(context.preferences.getInt(customThemeLight_Background2Key, DefaultLightColorPalette.background2.hashCode())),
        background3 = Color(context.preferences.getInt(customThemeLight_Background3Key, DefaultLightColorPalette.background3.hashCode())),
        background4 = Color(context.preferences.getInt(customThemeLight_Background4Key, DefaultLightColorPalette.background4.hashCode())),
        text = Color(context.preferences.getInt(customThemeLight_TextKey, DefaultLightColorPalette.text.hashCode())),
        textSecondary = Color(context.preferences.getInt(customThemeLight_textSecondaryKey, DefaultLightColorPalette.textSecondary.hashCode())),
        textDisabled = Color(context.preferences.getInt(customThemeLight_textDisabledKey, DefaultLightColorPalette.textDisabled.hashCode())),
        iconButtonPlayer = Color(context.preferences.getInt(customThemeLight_iconButtonPlayerKey, DefaultLightColorPalette.iconButtonPlayer.hashCode())),
        accent = Color(context.preferences.getInt(customThemeLight_accentKey, DefaultLightColorPalette.accent.hashCode()))
    )

    val customThemeDark = colorPalette.copy(
        background0 = Color(context.preferences.getInt(customThemeDark_Background0Key, DefaultDarkColorPalette.background0.hashCode())),
        background1 = Color(context.preferences.getInt(customThemeDark_Background1Key, DefaultDarkColorPalette.background1.hashCode())),
        background2 = Color(context.preferences.getInt(customThemeDark_Background2Key, DefaultDarkColorPalette.background2.hashCode())),
        background3 = Color(context.preferences.getInt(customThemeDark_Background3Key, DefaultDarkColorPalette.background3.hashCode())),
        background4 = Color(context.preferences.getInt(customThemeDark_Background4Key, DefaultDarkColorPalette.background4.hashCode())),
        text = Color(context.preferences.getInt(customThemeDark_TextKey, DefaultDarkColorPalette.text.hashCode())),
        textSecondary = Color(context.preferences.getInt(customThemeDark_textSecondaryKey, DefaultDarkColorPalette.textSecondary.hashCode())),
        textDisabled = Color(context.preferences.getInt(customThemeDark_textDisabledKey, DefaultDarkColorPalette.textDisabled.hashCode())),
        iconButtonPlayer = Color(context.preferences.getInt(customThemeDark_iconButtonPlayerKey, DefaultDarkColorPalette.iconButtonPlayer.hashCode())),
        accent = Color(context.preferences.getInt(customThemeDark_accentKey, DefaultDarkColorPalette.accent.hashCode()))
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
