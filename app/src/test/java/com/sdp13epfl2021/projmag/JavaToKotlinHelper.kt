package com.sdp13epfl2021.projmag

import org.mockito.Mockito

/**
 * Workaround found on [StackOverflow][https://stackoverflow.com/a/30308199] to avoid
 * a `NullPointerException` caused by Java to Kotlin type cast
 */
object JavaToKotlinHelper {
    fun <T> anyObject(): T {
        Mockito.anyObject<T>()
        return uninitialized()
    }

    private fun <T> uninitialized(): T = null as T
}