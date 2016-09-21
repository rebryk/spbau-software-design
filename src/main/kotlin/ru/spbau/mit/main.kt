package ru.spbau.mit

import ru.spbau.mit.commands.*

/**
 * Created by rebryk on 9/7/16.
 */

/**
 * Method creates shell and registers commands
 */
fun main(args: Array<String>) {
    val shell = Shell()
    shell.registerCommand("echo", Echo())
    shell.registerCommand("pwd", Pwd())
    shell.registerCommand("wc", Wc())
    shell.registerCommand("cat", Cat())

    do {
        val line = readLine()
        if (line.orEmpty().compareTo("exit") == 0) {
            break;
        }

        try {
            val result = shell.execute(line.orEmpty())
            if (result.isNotEmpty()) {
                println(result)
            }
        } catch (e: Exception) {
            println(String.format("Error: unexpected exception \'%s!\'", e.toString()))
        }
    } while (line != null)
}
