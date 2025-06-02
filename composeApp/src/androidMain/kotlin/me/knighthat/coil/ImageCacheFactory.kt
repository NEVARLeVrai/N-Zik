package me.knighthat.coil

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import app.kreate.android.R
import app.kreate.android.Settings
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.AsyncImagePainter.State
import coil.compose.rememberAsyncImagePainter
import coil.disk.DiskCache
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.transform.Transformation
import it.fast4x.rimusic.appContext
import it.fast4x.rimusic.enums.CoilDiskCacheMaxSize
import it.fast4x.rimusic.thumbnail
import it.fast4x.rimusic.thumbnailShape
import it.fast4x.rimusic.utils.coilCustomDiskCacheKey
import it.fast4x.rimusic.utils.preferences

object ImageCacheFactory {

    private val DISK_CACHE: DiskCache by lazy {
        val preferences = appContext().preferences
        val diskSize by Settings.THUMBNAIL_CACHE_SIZE

        DiskCache.Builder()
                 .directory( appContext().filesDir.resolve( "coil" ) )
                 .maxSizeBytes(
                     when( diskSize ) {
                         CoilDiskCacheMaxSize.Custom -> preferences.getInt( coilCustomDiskCacheKey, 128 )
                                                                   .times( 1000L )
                                                                   .times( 1000 )

                         else                        -> diskSize.bytes
                     }
                 ).build()
    }

    // This is isn't for all devices, some devices need
    // bigger sizes so the image won't distort, but 900 (px)
    // is fit for majority of devices and the their storage sizes
    // isn't too big either.
    const val THUMBNAIL_SIZE = 900;

     val LOADER: ImageLoader by lazy {
         // This version doesn't have Network Cache,
         // enabling this potentially cause high storage
         // because Coil attempts to store downloaded image
         // and HTTP version separately.
         ImageLoader.Builder( appContext() )
                    .crossfade( true )
                    .placeholder( R.drawable.loader )
                    .error( R.drawable.noimage )
                    .fallback( R.drawable.image )
                    .diskCachePolicy( CachePolicy.ENABLED )
                    .diskCache( DISK_CACHE )
                    .build()
     }

    @Composable
    fun Thumbnail(
        thumbnailUrl: String?,
        contentDescription: String? = null,
        contentScale: ContentScale = ContentScale.FillBounds,
        modifier: Modifier = Modifier.clip( thumbnailShape() )
                                     .fillMaxSize()
    ) {
        /*
         * TODO: Make a simple system to detect network speed and/or
         * TODO: data saver that automatically lower the quality to
         * TODO: reduce loading time and to preserve data usage.
         */
        val request = ImageRequest.Builder( appContext() )
                                                  .data( thumbnailUrl.thumbnail( THUMBNAIL_SIZE ) )
                                                  .diskCacheKey( thumbnailUrl )
                                                  .build()

        AsyncImage(
            model = request,
            imageLoader = LOADER,
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = modifier
        )
    }

    @Composable
    fun Thumbnail(
        thumbnailUrl: String?,
        contentDescription: String? = null,
        contentScale: ContentScale = ContentScale.FillBounds,
        transformations: List<Transformation> = emptyList(),
        modifier: Modifier = Modifier.clip( thumbnailShape() )
            .fillMaxSize()
    ) {
        /*
         * TODO: Make a simple system to detect network speed and/or
         * TODO: data saver that automatically lower the quality to
         * TODO: reduce loading time and to preserve data usage.
         */
        val request = ImageRequest.Builder( appContext() )
                                                  .data( thumbnailUrl.thumbnail( THUMBNAIL_SIZE ) )
                                                  .diskCacheKey( thumbnailUrl )
                                                  .transformations( transformations )
                                                  .build()

        AsyncImage(
            model = request,
            imageLoader = LOADER,
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = modifier
        )
    }

    @Composable
    fun Painter(
        thumbnailUrl: String?,
        contentScale: ContentScale = ContentScale.FillBounds,
        transformations: List<Transformation> = emptyList(),
        @DrawableRes placeholder: Int = R.drawable.loader,
        @DrawableRes error: Int = R.drawable.noimage,
        @DrawableRes fallback: Int = R.drawable.image,
        onLoading: ((State.Loading) -> Unit)? = null,
        onSuccess: ((State.Success) -> Unit)? = null,
        onError: ((State.Error) -> Unit)? = null
    ): AsyncImagePainter {
        /*
         * TODO: Make a simple system to detect network speed and/or
         * TODO: data saver that automatically lower the quality to
         * TODO: reduce loading time and to preserve data usage.
         */
        val request = ImageRequest.Builder( appContext() )
                                                  .data( thumbnailUrl.thumbnail( THUMBNAIL_SIZE ) )
                                                  .diskCacheKey( thumbnailUrl )
                                                  .transformations( transformations )
                                                  .build()

        return rememberAsyncImagePainter(
            model = request,
            imageLoader = LOADER,
            contentScale = contentScale,
            placeholder = painterResource( placeholder ),
            error = painterResource( error ),
            fallback = painterResource( fallback ),
            onLoading = onLoading,
            onSuccess = onSuccess,
            onError = onError
        )
    }
}