package com.sdp13epfl2021.projmag.database

import java.io.*
import kotlin.reflect.KClass


/**
 * This serializable class can be used to wrap a list of String.
 */
data class SerializedStringListWrapper(val list: List<String>) : Serializable

/**
 * Save a serializable object to a file.
 *
 * @param file The file where data will be stored.
 * @param data The parcelable data to store.
 * @return if the operation was successful.
 */
fun saveToFile(file: File, data: Serializable): Boolean {
    try {
        ObjectOutputStream(FileOutputStream(file, false)).use {
            it.writeObject(data)
            return true
        }
    } catch (e: Exception) {
        e.printStackTrace()
        return false
    }
}

/**
 * Load a serialized object from a file.
 *
 * @param file The file from which the data is loaded
 * @param kclass The class type of the data.
 * @return the data casted to kclass type or null if failed.
 *
 */
fun <T : Serializable> loadFromFile(file: File, kclass: KClass<T>): T? {
    try {
        ObjectInputStream(FileInputStream(file)).use {
            return kclass.javaObjectType.cast(it.readObject())
        }
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}
