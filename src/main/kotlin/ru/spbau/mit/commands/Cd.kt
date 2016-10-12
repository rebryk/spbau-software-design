package ru.spbau.mit.commands

import java.nio.file.Files

/**
 * Cd command: changes current directory of shell.
 */
class Cd : Command {
    override fun execute(input: String, shell: Shell): String {
        val newCurrentDirectory = shell.getCurrentDir().resolve(input)
        if (Files.isDirectory(newCurrentDirectory)) {
            shell.setCurrentDir(newCurrentDirectory)
        } else {
            println(String.format("Error: no such directory \'%s\'!", input))
        }
        return ""
    }
}
