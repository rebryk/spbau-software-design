package ru.spbau.mit.commands

import ru.spbau.mit.Environment

/**
 * Created by rebryk on 9/7/16.
 */

/**
 * Command executes input string in the system shell
 */
class ExternalCommand : Command {
    override fun execute(input: String, env: Environment): String {
        try {
            val process = Runtime.getRuntime().exec(input)
            if (process.waitFor() == 0) {
                return process.inputStream.reader().readText()
            }
        } catch(e: Exception) {
            println(String.format("Error: %s", e.toString()))
        }

        return "";
    }
}