package com.sdp13epfl2021.projmag.database

import android.os.Parcelable
import com.sdp13epfl2021.projmag.curriculumvitae.CurriculumVitae
import com.sdp13epfl2021.projmag.model.ImmutableProject
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.parcelize.Parcelize
import org.junit.After
import org.junit.Test
import java.io.File
import java.io.Serializable
import java.nio.file.Files

class LocalFileUtilsTest {

    private data class SerializableClassForTesting(val list: List<String>, private val num: Int, val s: String) : Serializable

    private val validFile: File = Files.createTempFile("testing", ".tmp").toFile()


    @After
    fun clean() {
        validFile.deleteRecursively()
    }

    @Test
    fun saveLoadWork() {
        val data = SerializableClassForTesting(listOf("a", "", "c"), 123, "test")
        LocalFileUtils.saveToFile(validFile, data)
        assertEquals(data, LocalFileUtils.loadFromFile(validFile, SerializableClassForTesting::class))

        val result: CurriculumVitae? = LocalFileUtils.loadFromFile(validFile, CurriculumVitae::class)
        assertNull(result)
    }

}