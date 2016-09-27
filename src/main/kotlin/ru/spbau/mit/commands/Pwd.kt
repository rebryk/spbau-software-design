package ru.spbau.mit.commands

import ru.spbau.mit.Environment
import java.nio.file.Paths

/**
 * Created by rebryk on 9/7/16.
 */

/**
 * Command that prints current location
 */
class Pwd : Command {
    override fun execute(input: String, env: Environment): String {
        return env.currentDirectory
    }
}
