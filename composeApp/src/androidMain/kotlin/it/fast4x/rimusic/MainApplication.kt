package it.fast4x.rimusic

import android.app.Application
import androidx.compose.runtime.getValue
import app.kreate.android.Settings
import coil.ImageLoader
import coil.ImageLoaderFactory
import it.fast4x.rimusic.utils.CaptureCrash
import it.fast4x.rimusic.utils.FileLoggingTree
import me.knighthat.coil.ImageCacheFactory
import timber.log.Timber
import java.io.File

class MainApplication : Application(), ImageLoaderFactory {

    override fun onCreate() {
        Settings.load( this )

        super.onCreate()
        //DatabaseInitializer()
        Dependencies.init(this)

        /**** LOG *********/
        val logEnabled by Settings.DEBUG_LOG
        if (logEnabled) {
            val dir = filesDir.resolve("logs").also {
                if (it.exists()) return@also
                it.mkdir()
            }

            Thread.setDefaultUncaughtExceptionHandler(CaptureCrash(dir.absolutePath))

            Timber.plant(FileLoggingTree(File(dir, "RiMusic_log.txt")))
            Timber.d("Log enabled at ${dir.absolutePath}")
        } else {
            Timber.uprootAll()
            Timber.plant(Timber.DebugTree())
        }
        /**** LOG *********/
    }

    override fun onTerminate() {
        Settings.unload()

        super.onTerminate()
    }

    override fun newImageLoader(): ImageLoader = ImageCacheFactory.LOADER

}

object Dependencies {
    lateinit var application: MainApplication
        private set

    internal fun init(application: MainApplication) {
        this.application = application
    }
}