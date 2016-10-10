package ru.spbau.mit.commands

/**
 * Created by rebryk on 9/7/16.
 */

/**
 * Command that prints current location
 */
class Pwd : Command {
    override fun execute(input: String, shell: Shell) : String {
        return System.getProperty("user.dir")
    }
}
