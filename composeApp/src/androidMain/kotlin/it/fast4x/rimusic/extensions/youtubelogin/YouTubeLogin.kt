package it.fast4x.rimusic.extensions.youtubelogin

import android.annotation.SuppressLint
import android.webkit.CookieManager
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import app.kreate.android.R
import app.kreate.android.Settings
import it.fast4x.innertube.Innertube
import it.fast4x.rimusic.LocalPlayerAwareWindowInsets
import it.fast4x.rimusic.ui.components.themed.Title
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(DelicateCoroutinesApi::class)
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun YouTubeLogin(
    onLogin: (String) -> Unit
) {

    val scope = rememberCoroutineScope()

    var visitorData by Settings.YOUTUBE_VISITOR_DATA
    var dataSyncId by Settings.YOUTUBE_SYNC_ID
    var cookie by Settings.YOUTUBE_COOKIES
    var accountName by Settings.YOUTUBE_ACCOUNT_NAME
    var accountEmail by Settings.YOUTUBE_ACCOUNT_EMAIL
    var accountChannelHandle by Settings.YOUTUBE_SELF_CHANNEL_HANDLE
    var accountThumbnail by Settings.YOUTUBE_ACCOUNT_AVATAR

    var webView: WebView? = null

    Column (
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().windowInsetsPadding(LocalPlayerAwareWindowInsets.current)
    ) {
        //Row(modifier = Modifier.fillMaxWidth()) {
            Title("Login to YouTube Music",
                icon = R.drawable.chevron_down,
                onClick = { onLogin(cookie) }
            )
        //}

        AndroidView(
            modifier = Modifier
                .windowInsetsPadding(LocalPlayerAwareWindowInsets.current)
                .fillMaxSize(),
            factory = { context ->
                WebView(context).apply {
                    webViewClient = object : WebViewClient() {
                        override fun doUpdateVisitedHistory(view: WebView, url: String, isReload: Boolean) {
                            if (url.startsWith("https://music.youtube.com")) {
                                cookie = CookieManager.getInstance().getCookie(url)
                                //onLogin(cookie)

                                GlobalScope.launch {
                                    Innertube.accountInfo().onSuccess {
                                        println("YoutubeLogin doUpdateVisitedHistory accountInfo() $it")
                                        accountName = it?.name.orEmpty()
                                        accountEmail = it?.email.orEmpty()
                                        accountChannelHandle = it?.channelHandle.orEmpty()
                                        accountThumbnail = it?.thumbnailUrl.orEmpty()
                                        onLogin(cookie)
                                    }.onFailure {
                                        Timber.e("Error YoutubeLogin: $it.stackTraceToString()")
                                        println("Error YoutubeLogin: ${it.stackTraceToString()}")
                                    }
                                }
                            }
                        }

                        override fun onPageFinished(view: WebView, url: String?) {
                            loadUrl("javascript:Android.onRetrieveVisitorData(window.yt.config_.VISITOR_DATA)")
                            loadUrl("javascript:Android.onRetrieveDataSyncId(window.yt.config_.DATASYNC_ID)")
                        }



                    }
                    settings.apply {
                        javaScriptEnabled = true
                        setSupportZoom(true)
                        builtInZoomControls = true
                    }
                    addJavascriptInterface(object {
                        @JavascriptInterface
                        fun onRetrieveVisitorData(newVisitorData: String?) {
                            if (newVisitorData != null) {
                                visitorData = newVisitorData
                            }
                        }
                        @JavascriptInterface
                        fun onRetrieveDataSyncId(newDataSyncId: String?) {
                            if (newDataSyncId != null) {
                                dataSyncId = newDataSyncId
                            }
                        }
                    }, "Android")
                    webView = this
                    loadUrl("https://accounts.google.com/ServiceLogin?ltmpl=music&service=youtube&passive=true&continue=https%3A%2F%2Fwww.youtube.com%2Fsignin%3Faction_handle_signin%3Dtrue%26next%3Dhttps%253A%252F%252Fmusic.youtube.com%252F")
                }
            }
        )

        BackHandler(enabled = webView?.canGoBack() == true) {
            webView?.goBack()
        }


    }



}

