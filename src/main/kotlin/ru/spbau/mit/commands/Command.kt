package ru.spbau.mit.commands

/**
 * Command interface
 */
interface Command {
    fun execute(input: String) : String
}
