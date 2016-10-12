package ru.spbau.mit.commands

/**
 * Created by rebryk on 9/7/16.
 */

/**
 * Command interface
 */
interface Command {
    fun execute(input: String, shell: Shell) : String
}
