package ru.spbau.mit.commands

import ru.spbau.mit.Environment
import ru.spbau.mit.splitBy
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors

/**
 * Created by Сева on 27.09.2016.
 */

class Ls: Command {
    override fun execute(input: String, env: Environment): String {
        val args = splitBy(input, ' ')
        val paths = if (args.isNotEmpty()) args else listOf(".")
        return paths.map {
            try {
                Files.walk(Paths.get(env.currentDirectory).resolve(it), 1).skip(1).map { it.fileName.toString() }
                        .sorted().collect(Collectors.joining(" "))
            } catch (e: NoSuchFileException) {
                System.err.println("ls: $it: no such file or directory")
                ""
            } catch (e: Exception) {
                System.err.println("ls: $it: could not open file or directory")
                ""
            }
        }.filter { it.isNotBlank() }.joinToString(" ")
    }
}