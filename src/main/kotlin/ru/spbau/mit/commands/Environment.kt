package ru.spbau.mit.commands

/**
 * Created by rebryk on 9/7/16.
 */

/**
 * Command executes input string in the system shell
 */
class Environment : Command {
    override fun execute(input: String, shell: Shell): String {
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