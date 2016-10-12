package ru.spbau.mit.commands

import java.io.IOException
import java.nio.file.Files

/**
 * Ls command
 */
class Ls : Command {
    override fun execute(input: String, shell: Shell): String {
        val path = shell.getCurrentDir().resolve(input)
        if (!Files.exists(path)) {
            println(String.format("Error: no such file or directory \'%s\'!", input))
            return ""
        }

        try {
            Files.newDirectoryStream(path, "*").use {
                val buffer = StringBuffer()
                for (entry in it) {
                    buffer.append(entry.fileName.toString())
                    buffer.append('\n')
                }
                return buffer.toString()
            }
        } catch (e: IOException) {
            println(String.format("Error: failed to read \'%s\'!", input))
            return ""
        }
    }
}