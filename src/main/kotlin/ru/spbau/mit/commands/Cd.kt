package ru.spbau.mit.commands

import ru.spbau.mit.Environment
import ru.spbau.mit.splitBy
import java.io.File
import java.nio.file.Paths

/**
 * Created by Сева on 27.09.2016.
 */

class Cd: Command {
    override fun execute(input: String, env: Environment): String {
        val args = splitBy(input, ' ')
        if (args.isEmpty()) {
            env.currentDirectory = Paths.get("~/").toAbsolutePath().toString()
            return ""
        }
        val file = Paths.get(env.currentDirectory).resolve(args[0]).toFile()
        if (!file.isDirectory) {
            System.err.println("cd: ${args[0]} is not an directory")
            return ""
        }
        env.currentDirectory = file.absolutePath
        return ""
    }

}
