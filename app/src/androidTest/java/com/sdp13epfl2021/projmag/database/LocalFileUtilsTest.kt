package com.sdp13epfl2021.projmag.database

import com.sdp13epfl2021.projmag.curriculumvitae.CurriculumVitae
import junit.framework.TestCase.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.File
import java.io.Serializable
import java.nio.file.Files

class LocalFileUtilsTest {

    private data class SerializableClassForTesting(
        val list: List<String>,
        private val num: Int,
        val s: String
    ) : Serializable

    private val validFile: File = Files.createTempFile("testing", ".tmp").toFile()
    private val invalidFile: File = Files.createTempDirectory("testing2").toFile()

    @Before
    fun setup() {
        invalidFile.mkdirs()
        invalidFile.setWritable(false)
        invalidFile.setReadOnly()
    }

    @After
    fun clean() {
        validFile.deleteRecursively()
        invalidFile.deleteRecursively()
    }

    @Test
    fun saveLoadWork() {
        val data = SerializableClassForTesting(listOf("a", "", "c"), 123, "test")
        assertTrue(saveToFile(validFile, data))
        assertEquals(data, loadFromFile(validFile, SerializableClassForTesting::class))

        val result: CurriculumVitae? = loadFromFile(validFile, CurriculumVitae::class)
        assertNull(result)

        assertFalse(saveToFile(invalidFile, data))
    }

}