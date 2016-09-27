package ru.spbau.mit.commands

import ru.spbau.mit.Environment
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Created by rebryk on 9/10/16.
 */

/**
 * Command prints content of input file
 */
class Cat : Command {
    override fun execute(input: String, env: Environment): String {
        val path = Paths.get(env.currentDirectory).resolve(input)
        if (!Files.exists(path)) {
            println(String.format("Error: no such file \'%s\'!", input))
            return ""
        }

        return String(Files.readAllBytes(path))
    }
}