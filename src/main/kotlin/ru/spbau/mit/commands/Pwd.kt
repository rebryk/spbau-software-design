package ru.spbau.mit.commands

/**
 * Command that prints current location
 */
class Pwd : Command {
    override fun execute(input: String) : String {
        return System.getProperty("user.dir")
    }
}
