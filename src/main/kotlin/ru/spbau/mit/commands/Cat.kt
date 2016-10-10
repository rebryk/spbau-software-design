package ru.spbau.mit.commands

import java.nio.file.Files
import java.nio.file.Paths

/**
 * Created by rebryk on 9/10/16.
 */

/**
 * Command prints content of input file
 */
class Cat : Command {
    override fun execute(input: String, shell: Shell): String {
        val pathToFile = shell.getCurrentDir().resolve(input);
        if (!Files.exists(pathToFile)) {
            println(String.format("Error: no such file \'%s\'!", input))
            return ""
        }

        return String(Files.readAllBytes(pathToFile))
    }
}