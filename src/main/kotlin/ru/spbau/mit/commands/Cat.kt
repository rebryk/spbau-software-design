package ru.spbau.mit.commands

import java.nio.file.Files
import java.nio.file.Paths

/**
 * Command prints content of input file
 */
class Cat : Command {
    override fun execute(input: String): String {
        if (!Files.exists(Paths.get(input))) {
            println(String.format("Error: no such file \'%s\'!", input))
            return ""
        }

        return String(Files.readAllBytes(Paths.get(input)))
    }
}