package ru.spbau.mit.commands

import ru.spbau.mit.*
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

/**
 * Created by rebryk on 9/10/16.
 */

/**
 * Main command that executes terminal commands
 */
class Shell {
    private val commands: MutableMap<String, Command> = HashMap()
    private val variables: MutableMap<String, String> = HashMap()
    private val environment: Environment = Environment()
    private var currentDirectory: Path = Paths.get("")

    fun setCurrentDir(newCurrentDirectory: Path) {
        currentDirectory = newCurrentDirectory
    }

    fun getCurrentDir(): Path {
        return currentDirectory
    }

    private fun process(input: String) : String {
        if (input.isEmpty()) {
            return input
        }

        val args = splitBy(input, ' ').filter { it.isNotEmpty() }
        if (args.size > 2 && args[1].compareTo("=") == 0) {
            val value = args.subList(2, args.size).joinToString(" ")
            variables["$" + args[0]] = unwrap(processStrings(value, variables))
            return ""
        }

        if (commands[args[0]] == null) {
            return environment.execute(args.joinToString(" "), this)
        }

        return commands[args[0]]!!.execute(args.subList(1, args.size).joinToString(" "), this)
    }

    fun execute(input: String) : String {
        return splitBy(input, '|').map { processStrings(it, variables) }.fold("", { a, b -> process(b + " " + a) })
    }

    fun registerCommand(name: String, command: Command) {
        commands.put(name, command)
    }
}