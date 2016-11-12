package ru.spbau.mit.commands

import ru.spbau.mit.unwrap

/**
 * Command prints input string to stdout
 */
class Echo : Command {
    override fun execute(input: String): String {
        return unwrap(input)
    }
}