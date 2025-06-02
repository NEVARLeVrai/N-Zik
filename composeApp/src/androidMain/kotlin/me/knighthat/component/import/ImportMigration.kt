package me.knighthat.component.import

import android.content.Context
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.media3.common.util.UnstableApi
import app.kreate.android.Settings
import it.fast4x.rimusic.Database
import it.fast4x.rimusic.enums.ExoPlayerCacheLocation
import it.fast4x.rimusic.enums.ExoPlayerDiskCacheMaxSize
import it.fast4x.rimusic.enums.ExoPlayerDiskDownloadCacheMaxSize
import it.fast4x.rimusic.service.MyDownloadHelper
import it.fast4x.rimusic.service.modern.PlayerServiceModern
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.knighthat.component.ImportFromFile
import me.knighthat.component.dialog.RestartAppDialog
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import kotlin.io.path.createTempDirectory

class ImportMigration private constructor(
    launcher: ManagedActivityResultLauncher<Array<String>, Uri?>
) : ImportFromFile(launcher) {

    companion object {
        @UnstableApi
        @Composable
        operator fun invoke( context: Context, binder: PlayerServiceModern.Binder? ): ImportMigration =
            ImportMigration(
                rememberLauncherForActivityResult(
                    ActivityResultContracts.OpenDocument()
                ) { uri ->
                    // [uri] must be non-null (meaning path exists) in order to work
                    uri ?: return@rememberLauncherForActivityResult
                    // Same thing with binder
                    binder ?: return@rememberLauncherForActivityResult

                    CoroutineScope( Dispatchers.IO ).launch {
                        context.contentResolver
                               .openInputStream( uri )
                               ?.use { inStream ->         // Use [use] because it closes stream on exit
                                   ZipInputStream( inStream ).use { zipIn ->
                                       var entry: ZipEntry? = zipIn.nextEntry

                                       val cacheDir = when( Settings.SONG_CACHE_SIZE.value ) {
                                           // Temporary directory deletes itself after close
                                           // It means songs remain on device as long as it's open
                                           ExoPlayerDiskCacheMaxSize.Disabled -> createTempDirectory( PlayerServiceModern.CACHE_DIRNAME ).toFile()

                                           else                               ->
                                               // Looks a bit ugly but what it does is
                                               // check location set by user and return
                                               // appropriate path with [CACHE_DIRNAME] appended.
                                               when( Settings.EXO_CACHE_LOCATION.value ) {
                                                   ExoPlayerCacheLocation.System -> context.cacheDir
                                                   ExoPlayerCacheLocation.Private -> context.filesDir
                                               }.resolve( PlayerServiceModern.CACHE_DIRNAME )
                                       }
                                       // Ensure folder is empty
                                       cacheDir.listFiles()?.forEach( File::deleteRecursively )

                                       val downloadDir = when( Settings.SONG_DOWNLOAD_SIZE.value ) {
                                           // Temporary directory deletes itself after close
                                           // It means songs remain on device as long as it's open
                                           ExoPlayerDiskDownloadCacheMaxSize.Disabled -> createTempDirectory( MyDownloadHelper.CACHE_DIRNAME ).toFile()

                                           else                               ->
                                               // Looks a bit ugly but what it does is
                                               // check location set by user and return
                                               // appropriate path with [CACHE_DIRNAME] appended.
                                               when( Settings.EXO_CACHE_LOCATION.value ) {
                                                   ExoPlayerCacheLocation.System -> context.cacheDir
                                                   ExoPlayerCacheLocation.Private -> context.filesDir
                                               }.resolve( MyDownloadHelper.CACHE_DIRNAME )
                                       }
                                       // Ensure folder is empty
                                       downloadDir.listFiles()?.forEach( File::deleteRecursively )

                                       while( entry != null ) {
                                           //<editor-fold desc="Import cached songs">
                                           if( !entry.isDirectory && entry.name.startsWith( "cached/", true ) ) {
                                               val relPath = entry.name.substringAfter( "cached/" )

                                               val dest = File(cacheDir, relPath)
                                               dest.parentFile?.mkdirs()

                                               FileOutputStream(dest).use { fileOut ->
                                                   zipIn.copyTo( fileOut )
                                               }
                                           }

                                           if( !entry.isDirectory && entry.name.startsWith( "downloaded/", true ) ) {
                                               val relPath = entry.name.substringAfter( "downloaded/" )

                                               val dest = File(downloadDir, relPath)
                                               dest.parentFile?.mkdirs()

                                               FileOutputStream(dest).use { fileOut ->
                                                   zipIn.copyTo( fileOut )
                                               }
                                           }
                                           //</editor-fold>

                                           //<editor-fold desc="Import databases">
                                           if( entry.name.equals( "database.db", true ) ) {
                                               Database.checkpoint()
                                               Database.close()

                                               val dbFile = context.getDatabasePath( Database.FILE_NAME )
                                               FileOutputStream(dbFile).use { dbOut ->
                                                   zipIn.copyTo( dbOut )
                                               }
                                           }

                                           if( entry.name.equals( "exoplayer_internal.db", true ) )
                                               FileOutputStream(context.getDatabasePath( "exoplayer_internal.db" )).use { dbOut ->
                                                   zipIn.copyTo( dbOut )
                                               }
                                           //</editor-fold>

                                           if( entry.name.equals( "settings.csv", true ) ) {
                                               // CsvWriter closes ZipInputStream after use.
                                               // Must create a copy to prevent stream close prematurely
                                               val settingsFile = kotlin.io.path.createTempFile( "settings", "csv" ).toFile()
                                               FileOutputStream(settingsFile).use { fileOut ->
                                                   zipIn.copyTo( fileOut )
                                               }

                                               FileInputStream(settingsFile).use { fileIn ->
                                                   ImportSettings.onImport( context, fileIn )
                                               }
                                           }

                                           entry = zipIn.nextEntry
                                       }
                                   }
                               }

                        RestartAppDialog.showDialog()
                    }
                }
            )
    }

    override val supportedMimes: Array<String> = arrayOf(
        "application/zip"
    )
}