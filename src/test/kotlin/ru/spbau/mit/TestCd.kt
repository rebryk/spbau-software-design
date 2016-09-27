package ru.spbau.mit

import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import ru.spbau.mit.commands.Cd
import kotlin.test.assertEquals

class TestCd {
    val cd = Cd()
    val env = Environment()

    @get:Rule
    private val tmpFolder = TemporaryFolder()

    @Test
    fun testCd() {
        tmpFolder.create()
        val folder = tmpFolder.newFolder("testFolder")
        cd.execute(folder.absolutePath, env)
        assertEquals(env.currentDirectory, folder.absolutePath)
    }

}