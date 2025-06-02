package me.knighthat.updater

import android.os.Looper
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastFirstOrNull
import app.kreate.android.BuildConfig
import app.kreate.android.R
import app.kreate.android.Settings
import it.fast4x.rimusic.appContext
import it.fast4x.rimusic.enums.CheckUpdateState
import it.fast4x.rimusic.ui.components.themed.SecondaryTextButton
import it.fast4x.rimusic.ui.screens.settings.EnumValueSelectorSettingsEntry
import it.fast4x.rimusic.ui.screens.settings.SettingsDescription
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import me.knighthat.utils.Repository
import me.knighthat.utils.Toaster
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.UnknownHostException
import java.nio.file.NoSuchFileException

object Updater {
    private lateinit var tagName: String

    lateinit var build: GithubRelease.Build

    private fun extractBuild( assets: List<GithubRelease.Build> ): GithubRelease.Build {
        val appName = BuildConfig.APP_NAME
        val buildType = BuildConfig.BUILD_TYPE

        /*
           IDE will complain that is condition is always true
           but because it only sees debug, it assumes the results
           of this evaluation. DO NOT remove this!
         */
        if( buildType != "full" && buildType != "minified" )
            throw IllegalStateException( "Unknown build type ${BuildConfig.BUILD_TYPE}" )

        // Get the first build that has name matches 'Kreate-<buildType>.apk'
        // e.g. Full version will have name 'Kreate-full.apk'
        val fileName = "$appName-$buildType.apk"
        return assets.fastFirstOrNull {    // Experimental, revert to firstOrNull if needed
            it.name == fileName
        } ?: throw NoSuchFileException("")
    }

    /**
     * Turns `v1.0.0` to `1.0.0`, `1.0.0-m` to `1.0.0`
     */
    private fun trimVersion( versionStr: String ) = versionStr.removePrefix( "v" ).substringBefore( "-" )

    /**
     * Sends out requests to Github for latest version.
     *
     * Results are downloaded, filtered, and saved to [build]
     *
     * > **NOTE**: This is a blocking process, it should never run on UI thread
     */
    private suspend fun fetchUpdate() = withContext( Dispatchers.IO ) {
        assert( Looper.myLooper() != Looper.getMainLooper() ) {
            "Cannot run fetch update on main thread"
        }

        // https://api.github.com/repos/knighthat/Kreate/releases/latest
        val url = "${Repository.GITHUB_API}/repos/${Repository.LATEST_TAG_URL}"
        val request = Request.Builder().url( url ).build()
        val response = OkHttpClient().newCall( request ).execute()

        if( !response.isSuccessful ) {
            Toaster.e( response.message )
            return@withContext
        }

        val resBody = response.body?.string()
        if( resBody.isNullOrBlank() ) {
            Toaster.i( R.string.info_no_update_available )
            return@withContext
        }

        val json = Json {
            ignoreUnknownKeys = true
        }
        val githubRelease = json.decodeFromString<GithubRelease>( resBody )
        build = extractBuild( githubRelease.builds )
        tagName = githubRelease.tagName
    }

    fun checkForUpdate(
        isForced: Boolean = false
    ) = CoroutineScope( Dispatchers.IO ).launch {
        if( !BuildConfig.IS_AUTOUPDATE || NewUpdateAvailableDialog.isCancelled ) return@launch

        try {
            if( !::build.isInitialized || isForced )
                fetchUpdate()

            NewUpdateAvailableDialog.isActive = trimVersion( BuildConfig.VERSION_NAME ) != trimVersion( tagName )
            if( !NewUpdateAvailableDialog.isActive ) {
                Toaster.i( R.string.info_no_update_available )
                NewUpdateAvailableDialog.isCancelled = true
            }
        } catch( e: Exception ) {
            val message = when( e ) {
                is UnknownHostException -> appContext().getString( R.string.error_no_internet )
                is NoSuchFileException -> appContext().getString( R.string.info_no_update_available )
                else -> e.message ?: appContext().getString( R.string.error_unknown )
            }
            Toaster.e( message )

            NewUpdateAvailableDialog.isCancelled = true
        }
    }

    @Composable
    fun SettingEntry() {
        var checkUpdateState by Settings.CHECK_UPDATE
        if( !BuildConfig.IS_AUTOUPDATE )
            checkUpdateState = CheckUpdateState.Disabled

        Row( Modifier.fillMaxWidth() ) {
            EnumValueSelectorSettingsEntry(
                title = stringResource( R.string.enable_check_for_update ),
                selectedValue = checkUpdateState,
                onValueSelected = { checkUpdateState = it },
                valueText = { it.text },
                isEnabled = BuildConfig.IS_AUTOUPDATE,
                modifier = Modifier.weight( 1f )
            )

            AnimatedVisibility(
                visible = checkUpdateState != CheckUpdateState.Disabled && BuildConfig.IS_AUTOUPDATE,
                // Slide in from right + fade in effect.
                enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(initialAlpha = 0f),
                // Slide out from left + fade out effect.
                exit = slideOutHorizontally(targetOffsetX = { it }) + fadeOut(targetAlpha = 0f)
            ) {
                SecondaryTextButton(
                    text = stringResource( R.string.info_check_update_now ),
                    onClick = { checkForUpdate( true ) },
                    modifier = Modifier.padding( end = 24.dp )
                )
            }
        }

        SettingsDescription(
            stringResource(
                if( BuildConfig.IS_AUTOUPDATE )
                    R.string.when_enabled_a_new_version_is_checked_and_notified_during_startup
                else
                    R.string.description_app_not_installed_by_apk
            )
        )
    }
}