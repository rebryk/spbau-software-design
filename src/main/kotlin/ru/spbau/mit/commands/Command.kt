package ru.spbau.mit.commands

import ru.spbau.mit.Environment

/**
 * Created by rebryk on 9/7/16.
 */

/**
 * Command interface
 */
interface Command {
    fun execute(input: String, env: Environment) : String
}
