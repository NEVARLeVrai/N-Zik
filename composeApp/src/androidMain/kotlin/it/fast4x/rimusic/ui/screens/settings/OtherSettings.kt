package it.fast4x.rimusic.ui.screens.settings

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.media3.common.util.UnstableApi
import app.kreate.android.BuildConfig
import app.kreate.android.R
import app.kreate.android.Settings.DEBUG_LOG
import app.kreate.android.Settings.HOME_SONGS_ON_DEVICE_SHOW_FOLDERS
import app.kreate.android.Settings.IS_PROXY_ENABLED
import app.kreate.android.Settings.KEEP_SCREEN_ON
import app.kreate.android.Settings.LOCAL_SONGS_FOLDER
import app.kreate.android.Settings.PARENTAL_CONTROL
import app.kreate.android.Settings.PLAYER_EXTRA_SPACE
import app.kreate.android.Settings.PROXY_HOST
import app.kreate.android.Settings.PROXY_PORT
import app.kreate.android.Settings.PROXY_SCHEME
import it.fast4x.rimusic.colorPalette
import it.fast4x.rimusic.enums.NavigationBarPosition
import it.fast4x.rimusic.enums.ValidationType
import it.fast4x.rimusic.ui.components.themed.HeaderWithIcon
import it.fast4x.rimusic.ui.styling.Dimensions
import it.fast4x.rimusic.utils.isAtLeastAndroid10
import it.fast4x.rimusic.utils.isAtLeastAndroid12
import it.fast4x.rimusic.utils.isAtLeastAndroid6
import it.fast4x.rimusic.utils.isIgnoringBatteryOptimizations
import it.fast4x.rimusic.utils.textCopyToClipboard
import me.knighthat.utils.Toaster
import java.io.File

@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("BatteryLife")
@ExperimentalAnimationApi
@Composable
fun OtherSettings() {
    val context = LocalContext.current

//    var isAndroidAutoEnabled by remember {
//        val component = ComponentName(context, PlayerServiceModern::class.java)
//        val disabledFlag = PackageManager.COMPONENT_ENABLED_STATE_DISABLED
//        val enabledFlag = PackageManager.COMPONENT_ENABLED_STATE_ENABLED
//
//        mutableStateOf(
//            value = context.packageManager.getComponentEnabledSetting(component) == enabledFlag,
//            policy = object : SnapshotMutationPolicy<Boolean> {
//                override fun equivalent(a: Boolean, b: Boolean): Boolean {
//                    context.packageManager.setComponentEnabledSetting(
//                        component,
//                        if (b) enabledFlag else disabledFlag,
//                        PackageManager.DONT_KILL_APP
//                    )
//                    return a == b
//                }
//            }
//        )
//    }

    //var isInvincibilityEnabled by rememberPreference(isInvincibilityEnabledKey, false)

    var isIgnoringBatteryOptimizations by remember {
        mutableStateOf(context.isIgnoringBatteryOptimizations)
    }

    val activityResultLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            isIgnoringBatteryOptimizations = context.isIgnoringBatteryOptimizations
        }

    var isProxyEnabled by IS_PROXY_ENABLED
    var proxyHost by PROXY_HOST
    var proxyPort by PROXY_PORT
    var proxyMode by PROXY_SCHEME

    var defaultFolder by LOCAL_SONGS_FOLDER

    var isKeepScreenOnEnabled by KEEP_SCREEN_ON

    //var checkUpdateState by rememberPreference(checkUpdateStateKey, CheckUpdateState.Disabled)

    var showFolders by HOME_SONGS_ON_DEVICE_SHOW_FOLDERS

    var blackListedPaths by remember {
        val file = File(context.filesDir, "Blacklisted_paths.txt")
        if (file.exists()) {
            mutableStateOf(file.readLines())
        } else {
            mutableStateOf(emptyList())
        }
    }

    var parentalControlEnabled by PARENTAL_CONTROL
    var logDebugEnabled by DEBUG_LOG

    var extraspace by PLAYER_EXTRA_SPACE

    Column(
        modifier = Modifier
            .background(colorPalette().background0)
            //.fillMaxSize()
            .fillMaxHeight()
            .fillMaxWidth(
                if (NavigationBarPosition.Right.isCurrent())
                    Dimensions.contentWidthRightBar
                else
                    1f
            )
            .verticalScroll(rememberScrollState())
        /*
        .padding(
            LocalPlayerAwareWindowInsets.current
                .only(WindowInsetsSides.Vertical + WindowInsetsSides.End)
                .asPaddingValues()
        )

         */
    ) {
        HeaderWithIcon(
            title = stringResource(R.string.tab_miscellaneous),
            iconId = R.drawable.equalizer,
            enabled = false,
            showIcon = true,
            modifier = Modifier,
            onClick = {}
        )

    SettingsGroupSpacer()
    SettingsEntryGroupText(stringResource(R.string.on_device))
    StringListValueSelectorSettingsEntry(
        title = stringResource(R.string.blacklisted_folders),
        text = stringResource(R.string.edit_blacklist_for_on_device_songs),
        addTitle = stringResource(R.string.add_folder),
        addPlaceholder = if (isAtLeastAndroid10) {
            "Android/media/com.whatsapp/WhatsApp/Media"
        } else {
            "/storage/emulated/0/Android/media/com.whatsapp/"
        },
        conflictTitle = stringResource(R.string.this_folder_already_exists),
        removeTitle = stringResource(R.string.are_you_sure_you_want_to_remove_this_folder_from_the_blacklist),
        context = LocalContext.current,
        list = blackListedPaths,
        add = { newPath ->
            blackListedPaths = blackListedPaths + newPath
            val file = File(context.filesDir, "Blacklisted_paths.txt")
            file.writeText(blackListedPaths.joinToString("\n"))
        },
        remove = { path ->
            blackListedPaths = blackListedPaths.filter { it != path }
            val file = File(context.filesDir, "Blacklisted_paths.txt")
            file.writeText(blackListedPaths.joinToString("\n"))
        }
    )

    SwitchSettingEntry(
        title = stringResource(R.string.folders),
        text = stringResource(R.string.show_folders_in_on_device_page),
        isChecked = showFolders,
        onCheckedChange = { showFolders = it }
    )
    AnimatedVisibility(visible = showFolders) {
        TextDialogSettingEntry(
            title = stringResource(R.string.folder_that_will_show_when_you_open_on_device_page),
            text = defaultFolder,
            currentText = defaultFolder,
            onTextSave = { defaultFolder = it }
        )
    }

    SettingsGroupSpacer()

//    SettingsEntryGroupText(title = stringResource(R.string.android_auto))
//
//    SettingsDescription(text = stringResource(R.string.enable_unknown_sources))
//
//    SwitchSettingEntry(
//        title = stringResource(R.string.android_auto_1),
//        text = stringResource(R.string.enable_android_auto_support),
//        isChecked = isAndroidAutoEnabled,
//        onCheckedChange = { isAndroidAutoEnabled = it }
//    )
//
//    SettingsGroupSpacer()

    SettingsEntryGroupText(title = stringResource(R.string.androidheadunit))
    SwitchSettingEntry(
        title = stringResource(R.string.extra_space),
        text = "",
        isChecked = extraspace,
        onCheckedChange = { extraspace = it }
    )

    SettingsGroupSpacer()

    SettingsEntryGroupText(title = stringResource(R.string.service_lifetime))

    SwitchSettingEntry(
        title = stringResource(R.string.keep_screen_on),
        text = stringResource(R.string.prevents_screen_timeout),
        isChecked = isKeepScreenOnEnabled,
        onCheckedChange = { isKeepScreenOnEnabled = it }
    )

    ImportantSettingsDescription(text = stringResource(R.string.battery_optimizations_applied))

    if (isAtLeastAndroid12) {
        SettingsDescription(text = stringResource(R.string.is_android12))
    }

    val msgNoBatteryOptim = stringResource(R.string.not_find_battery_optimization_settings)

    SettingsEntry(
        title = stringResource(R.string.ignore_battery_optimizations),
        isEnabled = !isIgnoringBatteryOptimizations,
        text = if (isIgnoringBatteryOptimizations) {
            stringResource(R.string.already_unrestricted)
        } else {
            stringResource(R.string.disable_background_restrictions)
        },
        onClick = {
            if (!isAtLeastAndroid6) return@SettingsEntry

            try {
                activityResultLauncher.launch(
                    Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                        data = Uri.parse("package:${context.packageName}")
                    }
                )
            } catch (e: ActivityNotFoundException) {
                try {
                    activityResultLauncher.launch(
                        Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
                    )
                } catch (e: ActivityNotFoundException) {
                    Toaster.i( "$msgNoBatteryOptim ${BuildConfig.APP_NAME}" )
                }
            }
        }
    )

        /*
    SwitchSettingEntry(
        title = stringResource(R.string.invincible_service),
        text = stringResource(R.string.turning_off_battery_optimizations_is_not_enough),
        isChecked = isInvincibilityEnabled,
        onCheckedChange = { isInvincibilityEnabled = it }
    )

         */

    SettingsGroupSpacer()

    SettingsGroupSpacer()
    SettingsEntryGroupText(title = stringResource(R.string.proxy))
    SettingsDescription(text = stringResource(R.string.restarting_rimusic_is_required))
    SwitchSettingEntry(
        title = stringResource(R.string.enable_proxy),
        text = "",
        isChecked = isProxyEnabled,
        onCheckedChange = { isProxyEnabled = it }
    )

    AnimatedVisibility(visible = isProxyEnabled) {
        Column {
            EnumValueSelectorSettingsEntry(title = stringResource(R.string.proxy_mode),
                selectedValue = proxyMode,
                onValueSelected = { proxyMode = it },
                valueText = { it.name }
            )
            TextDialogSettingEntry(
                title = stringResource(R.string.proxy_host),
                text = proxyHost, //stringResource(R.string.set_proxy_hostname),
                currentText = proxyHost,
                onTextSave = { proxyHost = it },
                validationType = ValidationType.Ip
            )
            TextDialogSettingEntry(
                title = stringResource(R.string.proxy_port),
                text = proxyPort.toString(), //stringResource(R.string.set_proxy_port),
                currentText = proxyPort.toString(),
                onTextSave = { proxyPort = it.toIntOrNull() ?: 1080 })
        }
    }

    SettingsGroupSpacer()

    SettingsEntryGroupText(title = stringResource(R.string.parental_control))

    SwitchSettingEntry(
        title = stringResource(R.string.parental_control),
        text = stringResource(R.string.info_prevent_play_songs_with_age_limitation),
        isChecked = parentalControlEnabled,
        onCheckedChange = { parentalControlEnabled = it }
    )


    SettingsGroupSpacer()

    var text by remember { mutableStateOf(null as String?) }
    val noLogAvailable = stringResource(R.string.no_log_available)

    SettingsEntryGroupText(title = stringResource(R.string.debug))
    SwitchSettingEntry(
        title = stringResource(R.string.enable_log_debug),
        text = stringResource(R.string.if_enabled_create_a_log_file_to_highlight_errors),
        isChecked = logDebugEnabled,
        onCheckedChange = {
            logDebugEnabled = it
            if (!it) {
                val file = File(context.filesDir.resolve("logs"), "RiMusic_log.txt")
                if (file.exists())
                    file.delete()

                val filec = File(context.filesDir.resolve("logs"), "RiMusic_crash_log.txt")
                if (filec.exists())
                    filec.delete()


            } else
                Toaster.i( R.string.restarting_rimusic_is_required )
        }
    )
    ImportantSettingsDescription(text = stringResource(R.string.restarting_rimusic_is_required))
    ButtonBarSettingEntry(
        isEnabled = logDebugEnabled,
        title = stringResource(R.string.copy_log_to_clipboard),
        text = "",
        icon = R.drawable.copy,
        onClick = {
            val file = File(context.filesDir.resolve("logs"), "RiMusic_log.txt")
            if (file.exists()) {
                text = file.readText()
                text?.let {
                    textCopyToClipboard(it, context)
                }
            } else
                Toaster.w( noLogAvailable )
        }
    )
    ButtonBarSettingEntry(
        isEnabled = logDebugEnabled,
        title = stringResource(R.string.copy_crash_log_to_clipboard),
        text = "",
        icon = R.drawable.copy,
        onClick = {
            val file = File(context.filesDir.resolve("logs"), "RiMusic_crash_log.txt")
            if (file.exists()) {
                text = file.readText()
                text?.let {
                    textCopyToClipboard(it, context)
                }
            } else
                Toaster.w( noLogAvailable )
        }
    )

    Spacer(modifier = Modifier.height(Dimensions.bottomSpacer))

    }
}


