package ru.spbau.mit.commands

import ru.spbau.mit.Environment
import ru.spbau.mit.unwrap

/**
 * Created by rebryk on 9/7/16.
 */

/**
 * Command prints input string to stdout
 */
class Echo : Command {
    override fun execute(input: String, env: Environment): String {
        return unwrap(input)
    }
}