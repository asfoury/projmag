package com.sdp13epfl2021.projmag

import org.mockito.Mockito

/**
 * Duplicate because we cant get from test/ directory, but we can't duplicate name either
 *
 * Workaround found on [StackOverflow][https://stackoverflow.com/a/30308199] to avoid
 * a `NullPointerException` caused by Java to Kotlin type cast
 */
object JavaToKotlinHelperAndroidTest {
    fun <T> anyObject(): T {
        Mockito.anyObject<T>()
        return uninitialized()
    }

    private fun <T> uninitialized(): T = null as T
}