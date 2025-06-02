package me.knighthat.component.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import app.kreate.android.R
import app.kreate.android.Settings
import it.fast4x.rimusic.colorPalette
import it.fast4x.rimusic.ui.components.themed.IconButton
import it.fast4x.rimusic.ui.components.themed.SliderControl
import it.fast4x.rimusic.ui.screens.settings.SwitchSettingEntry
import it.fast4x.rimusic.ui.styling.favoritesIcon
import it.fast4x.rimusic.utils.blurStrengthKey
import it.fast4x.rimusic.utils.playerBackdropKey
import it.fast4x.rimusic.utils.rememberPreference
import me.knighthat.component.dialog.Dialog

class BlurAdjuster private constructor(
    activeState: MutableState<Boolean>,
    strengthState: MutableState<Float>,
    backdropState: MutableState<Float>,
    rotatingCoverState: MutableState<Boolean>
): Dialog {

    companion object {
        @Composable
        operator fun invoke() = BlurAdjuster(
            remember { mutableStateOf( false ) },
            rememberPreference( blurStrengthKey, 25f ),
            rememberPreference( playerBackdropKey, 0f ),
            Settings.PLAYER_ROTATING_ALBUM_COVER
        )
    }

    override val dialogTitle: String
        @Composable
        get() = stringResource( R.string.controls_title_blur_effect )

    var strength: Float by strengthState
    var backdrop: Float by backdropState
    var isCoverRotating: Boolean by rotatingCoverState
    override var isActive: Boolean by activeState

    fun onDismiss()  { isActive = false }

    @Composable
    override fun DialogBody() {
        Column(
            modifier = Modifier.wrapContentSize()
                               .padding( horizontal = 8.dp ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            //<editor-fold defaultstate="collapsed" desc="Blur slider">
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = { strength = 25f },
                    icon = R.drawable.drop_blur,
                    color = colorPalette().favoritesIcon,
                    modifier = Modifier.size(24.dp)
                )

                SliderControl(
                    state = strength,
                    onSlide = { strength = it },
                    onSlideComplete = {},
                    toDisplay = { "%.02f".format(it) },
                    range = 0f..100f
                )
            }
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="Backdrop slider">
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = { backdrop = 0f },
                    icon = R.drawable.drop_half_fill,
                    color = colorPalette().favoritesIcon,
                    modifier = Modifier.size( 24.dp )
                )

                SliderControl(
                    state = backdrop,
                    onSlide = { backdrop = it },
                    onSlideComplete = {},
                    toDisplay = { "%.0f".format(it) },
                    range = 0f..100f
                )
            }
            //</editor-fold>
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = { backdrop = 0f },
                    icon = R.drawable.image,
                    color = colorPalette().favoritesIcon,
                    modifier = Modifier.size( 24.dp )
                )

                SwitchSettingEntry(
                    title = stringResource( R.string.rotating_cover_title ),
                    text = "",
                    isChecked = isCoverRotating,
                    onCheckedChange = { isCoverRotating = it }
                )
            }
        }
    }
}