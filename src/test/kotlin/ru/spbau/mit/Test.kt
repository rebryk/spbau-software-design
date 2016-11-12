package ru.spbau.mit

import org.junit.Before
import org.junit.Test
import ru.spbau.mit.commands.*
import kotlin.test.assertEquals

class Test {
    val shell = Shell()

    @Before
    fun init() {
        shell.registerCommand("echo", Echo())
        shell.registerCommand("wc", Wc())
        shell.registerCommand("cat", Cat())
        shell.registerCommand("pwd", Pwd())
    }

    @Test
    fun testEcho() {
        assertEquals("test message", shell.execute("echo 'test message'"))
    }

    @Test
    fun testCat() {
        shell.execute("file = \'src/main/kotlin/ru/spbau/mit/main.kt\'")
        assertEquals(shell.execute("cat \$file | wc"), shell.execute("cat src/main/kotlin/ru/spbau/mit/main.kt | wc"))
    }

    @Test
    fun testSubstitution() {
        shell.execute("x = \'qwerty\'")
        assertEquals("123qwerty", shell.execute("echo \"123\$x\""))
    }

    @Test
    fun testPwd() {
        assertEquals(System.getProperty("user.dir"), shell.execute("pwd"))
    }
}