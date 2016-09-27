package ru.spbau.mit

import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import ru.spbau.mit.commands.Ls
import kotlin.test.assertEquals

class TestLs {
    val ls = Ls()
    val env = Environment()

    @get:Rule
    private val tmpFolder = TemporaryFolder()

    @Test
    fun testCd() {
        tmpFolder.create()

        val folder = tmpFolder.newFolder("testFolder")
        tmpFolder.newFile("testFolder/test1")
        tmpFolder.newFile("testFolder/test2")
        tmpFolder.newFolder("testFolder", "testFolder")

        assertEquals("test1 test2 testFolder", ls.execute(folder.absolutePath, env))
    }

}