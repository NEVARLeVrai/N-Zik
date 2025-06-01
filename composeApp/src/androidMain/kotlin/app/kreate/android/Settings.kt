package app.kreate.android

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SnapshotMutationPolicy
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.edit
import org.jetbrains.annotations.Blocking
import org.jetbrains.annotations.NonBlocking

/**
 * A set of lazily initialized singleton preferences.
 *
 * Unlike old implementation [it.fast4x.rimusic.utils.rememberPreference], each
 * call returns new [MutableState] that gets updated individually
 * and requires default value to be defined at call time. Such implementation
 * creates unwanted behavior when different "default value" is appointed.
 *
 * New implementation introduced to unified calls, main focus is to
 * make all calls return the same **mutable** object. Lazy init is another step
 * to make sure that unused preferences remain uninitialized so they won't
 * consume resources.
 */
object Settings {

    private lateinit var preferences: SharedPreferences

    /**
     * Initialize needed properties for settings to use.
     *
     * **ATTENTION**: Must be call as early as possible to prevent
     * because all preference require [preferences] to be initialized
     * to work.
     */
    fun load( context: Context ) {
        this.preferences = context.getSharedPreferences( "preferences", Context.MODE_PRIVATE )
    }

    /**
     * Finalize all changes and write it to disk.
     *
     * This is a blocking call.
     *
     * **NOTE**: Should only be called when the app
     * is about to close to make sure all settings are saved
     */
    @SuppressLint("UseKtx", "ApplySharedPref")      // Use conventional syntax because it's easier to read
    @Blocking
    fun unload() = this.preferences.edit().commit()

    /**
     * Represents an individual setting entry from **_preferences_** file.
     *
     * @param sharedPreferences a class that holds all entries of preferences file
     * @param key of the entry, used to extract/write data to preferences file
     * @param previousKey for backward compatibility, when key changed,
     * this will be used to extract old value to be used with new key
     * @param defaultValue if key doesn't exist in preferences, this value will be written
     * to it, and used as current value
     */
    sealed class Preference<T>(
        protected val sharedPreferences: SharedPreferences,
        val key: String,
        val previousKey: String,
        val defaultValue: T
    ): MutableState<T> {

        /**
         * How old and new value are processed
         */
        protected abstract val policy: SnapshotMutationPolicy<T>

        /**
         * Extract value from [SharedPreferences]. Return value
         * must be `null` if [key] doesn't exist inside preferences file.
         *
         * @return value of this preference, `null` if [key] doesn't exist
         */
        protected abstract fun getFromSharedPreferences(): T?

        /**
         * Write [value] into [SharedPreferences] instance.
         *
         * This is a non-blocking calls. Meaning, all writes
         * are temporary written to memory first, then sync
         * value to disk asynchronously.
         */
        @NonBlocking
        protected abstract fun write( value: T )

        override fun component1(): T = value

        override fun component2(): (T) -> Unit = { value = it }

        protected inner class StructuralEqualityPolicy: SnapshotMutationPolicy<T> {
            override fun equivalent( a: T, b: T ): Boolean {
                if( a != b ) write( b )
                return a == b
            }
        }

        protected inner class ReferentialEqualityPolicy: SnapshotMutationPolicy<T> {
            override fun equivalent( a: T, b: T ): Boolean {
                if( a !== b ) write( b )
                return a === b
            }
        }

        protected inner class DecimalEqualityPolicy: SnapshotMutationPolicy<T> {
            override fun equivalent( a: T, b: T ): Boolean {
                require( a is Comparable<*> && b is Comparable<*> && a!!::class == b!!::class )

                @Suppress("UNCHECKED_CAST")
                val areEqual = (a as Comparable<Any>).compareTo( b ) == 0
                if( !areEqual ) write( b )

                return areEqual
            }
        }

        class EnumPreference<E: Enum<E>>(
            sharedPreferences: SharedPreferences,
            key: String,
            previousKey: String,
            defaultValue: E
        ): Preference<E>(sharedPreferences, key, previousKey, defaultValue) {

            override val policy = ReferentialEqualityPolicy()

            override var value: E by mutableStateOf(
                value = getFromSharedPreferences() ?: defaultValue.also( ::write ),
                policy = this.policy
            )

            override fun getFromSharedPreferences(): E? {
                var fromFile: String? = null

                /*
                     Set [fromFile] to the value of [previousKey] if it's
                     existed in the preferences file, then delete that key
                     (for migration to new key)
                 */
                if( sharedPreferences.contains( previousKey ) ) {
                    fromFile = sharedPreferences.getString( previousKey, null )
                    sharedPreferences.edit( commit = true ) {
                        remove( previousKey )

                        // Add this value to new [key], otherwise, only old key
                        // will be removed and new key is not added until next start
                        // with default value
                        fromFile?.also { putString( key, it ) }
                    }
                }

                /*
                     Set [fromFile] to the value of [key] if it's
                     existed in the preferences file.

                     Reason for 2 separate steps is:
                     - When both [key] and [previousKey] are existed
                     in side the file, [previousKey] will be deleted
                     while value of [key] is being used.
                     - Or either 1 of the key will be used if only
                     1 of them existed inside the file.
                */
                if( sharedPreferences.contains( key ) )
                    fromFile = sharedPreferences.getString( key, null )

                return fromFile?.let { enumStr ->
                    defaultValue.javaClass.enumConstants?.firstOrNull { it.name == enumStr }
                }
            }

            override fun write( value: E ) =
                sharedPreferences.edit {
                    putString( key, value.name )
                }
        }

        class StringPreference(
            sharedPreferences: SharedPreferences,
            key: String,
            previousKey: String,
            defaultValue: String
        ): Preference<String>(sharedPreferences, key, previousKey, defaultValue) {

            override val policy = ReferentialEqualityPolicy()

            override var value: String by mutableStateOf(
                value = getFromSharedPreferences() ?: defaultValue.also( ::write ),
                policy = this.policy
            )

            override fun getFromSharedPreferences(): String? {
                var fromFile: String? = null

                /*
                     Set [fromFile] to the value of [previousKey] if it's
                     existed in the preferences file, then delete that key
                     (for migration to new key)
                 */
                if( sharedPreferences.contains( previousKey ) ) {
                    fromFile = sharedPreferences.getString( previousKey, null )
                    sharedPreferences.edit( commit = true ) {
                        remove( previousKey )

                        // Add this value to new [key], otherwise, only old key
                        // will be removed and new key is not added until next start
                        // with default value
                        fromFile?.also { putString( key, it ) }
                    }
                }

                /*
                     Set [fromFile] to the value of [key] if it's
                     existed in the preferences file.

                     Reason for 2 separate steps is:
                     - When both [key] and [previousKey] are existed
                     in side the file, [previousKey] will be deleted
                     while value of [key] is being used.
                     - Or either 1 of the key will be used if only
                     1 of them existed inside the file.
                */
                if( sharedPreferences.contains( key ) )
                    fromFile = sharedPreferences.getString( key, null )

                return fromFile
            }

            override fun write( value: String ) =
                sharedPreferences.edit {
                    putString( key, value )
                }
        }

        class StringSetPreference(
            sharedPreferences: SharedPreferences,
            key: String,
            previousKey: String,
            defaultValue: Set<String>
        ): Preference<Set<String>>(sharedPreferences, key, previousKey, defaultValue) {

            override val policy = StructuralEqualityPolicy()

            override var value: Set<String> by mutableStateOf(
                value = getFromSharedPreferences() ?: defaultValue.also( ::write ),
                policy = this.policy
            )

            override fun getFromSharedPreferences(): Set<String>? {
                var fromFile: Set<String>? = null

                /*
                     Set [fromFile] to the value of [previousKey] if it's
                     existed in the preferences file, then delete that key
                     (for migration to new key)
                 */
                if( sharedPreferences.contains( previousKey ) ) {
                    fromFile = sharedPreferences.getStringSet( previousKey, null )
                    sharedPreferences.edit( commit = true ) {
                        remove( previousKey )

                        // Add this value to new [key], otherwise, only old key
                        // will be removed and new key is not added until next start
                        // with default value
                        fromFile?.also { putStringSet( key, it ) }
                    }
                }

                /*
                     Set [fromFile] to the value of [key] if it's
                     existed in the preferences file.

                     Reason for 2 separate steps is:
                     - When both [key] and [previousKey] are existed
                     in side the file, [previousKey] will be deleted
                     while value of [key] is being used.
                     - Or either 1 of the key will be used if only
                     1 of them existed inside the file.
                */
                if( sharedPreferences.contains( key ) )
                    fromFile = sharedPreferences.getStringSet( key, null )

                return fromFile
            }

            override fun write( value: Set<String> ) =
                sharedPreferences.edit {
                    putStringSet( key, value )
                }
        }

        class IntPreference(
            sharedPreferences: SharedPreferences,
            key: String,
            previousKey: String,
            defaultValue: Int
        ): Preference<Int>(sharedPreferences, key, previousKey, defaultValue) {

            override val policy = DecimalEqualityPolicy()

            override var value: Int by mutableStateOf(
                value = getFromSharedPreferences() ?: defaultValue.also( ::write ),
                policy = this.policy
            )

            @Suppress("USELESS_NULLABLE")
            override fun getFromSharedPreferences(): Int? {
                var fromFile: Int? = null

                /*
                     Set [fromFile] to the value of [previousKey] if it's
                     existed in the preferences file, then delete that key
                     (for migration to new key)
                 */
                if( sharedPreferences.contains( previousKey ) ) {
                    fromFile = sharedPreferences.getInt( previousKey, defaultValue )
                    sharedPreferences.edit( commit = true ) {
                        remove( previousKey )

                        // Add this value to new [key], otherwise, only old key
                        // will be removed and new key is not added until next start
                        // with default value
                        fromFile?.also { putInt( key, it ) }
                    }
                }

                /*
                     Set [fromFile] to the value of [key] if it's
                     existed in the preferences file.

                     Reason for 2 separate steps is:
                     - When both [key] and [previousKey] are existed
                     in side the file, [previousKey] will be deleted
                     while value of [key] is being used.
                     - Or either 1 of the key will be used if only
                     1 of them existed inside the file.
                */
                if( sharedPreferences.contains( key ) )
                    fromFile = sharedPreferences.getInt( key, defaultValue )

                return fromFile
            }

            override fun write( value: Int ) =
                sharedPreferences.edit {
                    putInt( key, value )
                }
        }

        class LongPreference(
            sharedPreferences: SharedPreferences,
            key: String,
            previousKey: String,
            defaultValue: Long
        ): Preference<Long>(sharedPreferences, key, previousKey, defaultValue) {

            override val policy = DecimalEqualityPolicy()

            override var value: Long by mutableStateOf(
                value = getFromSharedPreferences() ?: defaultValue.also( ::write ),
                policy = this.policy
            )

            override fun getFromSharedPreferences(): Long? {
                var fromFile: Long? = null

                /*
                     Set [fromFile] to the value of [previousKey] if it's
                     existed in the preferences file, then delete that key
                     (for migration to new key)
                 */
                if( sharedPreferences.contains( previousKey ) ) {
                    fromFile = sharedPreferences.getLong( previousKey, defaultValue )
                    sharedPreferences.edit( commit = true ) {
                        remove( previousKey )

                        // Add this value to new [key], otherwise, only old key
                        // will be removed and new key is not added until next start
                        // with default value
                        fromFile?.also { putLong( key, it ) }
                    }
                }

                /*
                     Set [fromFile] to the value of [key] if it's
                     existed in the preferences file.

                     Reason for 2 separate steps is:
                     - When both [key] and [previousKey] are existed
                     in side the file, [previousKey] will be deleted
                     while value of [key] is being used.
                     - Or either 1 of the key will be used if only
                     1 of them existed inside the file.
                */
                if( sharedPreferences.contains( key ) )
                    fromFile = sharedPreferences.getLong( key, defaultValue )

                return fromFile
            }

            override fun write( value: Long ) =
                sharedPreferences.edit {
                    putLong( key, value )
                }
        }

        class FloatPreference(
            sharedPreferences: SharedPreferences,
            key: String,
            previousKey: String,
            defaultValue: Float
        ): Preference<Float>(sharedPreferences, key, previousKey, defaultValue) {

            override val policy = DecimalEqualityPolicy()

            override var value: Float by mutableStateOf(
                value = getFromSharedPreferences() ?: defaultValue.also( ::write ),
                policy = this.policy
            )

            override fun getFromSharedPreferences(): Float? {
                var fromFile: Float? = null

                /*
                     Set [fromFile] to the value of [previousKey] if it's
                     existed in the preferences file, then delete that key
                     (for migration to new key)
                 */
                if( sharedPreferences.contains( previousKey ) ) {
                    fromFile = sharedPreferences.getFloat( previousKey, defaultValue )
                    sharedPreferences.edit( commit = true ) {
                        remove( previousKey )

                        // Add this value to new [key], otherwise, only old key
                        // will be removed and new key is not added until next start
                        // with default value
                        fromFile?.also { putFloat( key, it ) }
                    }
                }

                /*
                     Set [fromFile] to the value of [key] if it's
                     existed in the preferences file.

                     Reason for 2 separate steps is:
                     - When both [key] and [previousKey] are existed
                     in side the file, [previousKey] will be deleted
                     while value of [key] is being used.
                     - Or either 1 of the key will be used if only
                     1 of them existed inside the file.
                */
                if( sharedPreferences.contains( key ) )
                    fromFile = sharedPreferences.getFloat( key, defaultValue )

                return fromFile
            }

            override fun write( value: Float ) =
                sharedPreferences.edit {
                    putFloat( key, value )
                }
        }

        class BooleanPreference(
            sharedPreferences: SharedPreferences,
            key: String,
            previousKey: String,
            defaultValue: Boolean
        ): Preference<Boolean>(sharedPreferences, key, previousKey, defaultValue) {

            override val policy = StructuralEqualityPolicy()

            override var value: Boolean by mutableStateOf(
                value = getFromSharedPreferences() ?: defaultValue.also( ::write ),
                policy = this.policy
            )

            override fun getFromSharedPreferences(): Boolean? {
                var fromFile: Boolean? = null

                /*
                     Set [fromFile] to the value of [previousKey] if it's
                     existed in the preferences file, then delete that key
                     (for migration to new key)
                 */
                if( sharedPreferences.contains( previousKey ) ) {
                    fromFile = sharedPreferences.getBoolean( previousKey, defaultValue )
                    sharedPreferences.edit( commit = true ) {
                        remove( previousKey )

                        // Add this value to new [key], otherwise, only old key
                        // will be removed and new key is not added until next start
                        // with default value
                        fromFile?.also { putBoolean( key, it ) }
                    }
                }

                /*
                     Set [fromFile] to the value of [key] if it's
                     existed in the preferences file.

                     Reason for 2 separate steps is:
                     - When both [key] and [previousKey] are existed
                     in side the file, [previousKey] will be deleted
                     while value of [key] is being used.
                     - Or either 1 of the key will be used if only
                     1 of them existed inside the file.
                */
                if( sharedPreferences.contains( key ) )
                    fromFile = sharedPreferences.getBoolean( key, defaultValue )

                return fromFile
            }

            override fun write( value: Boolean ) =
                sharedPreferences.edit {
                    putBoolean( key, value )
                }
        }
    }
}