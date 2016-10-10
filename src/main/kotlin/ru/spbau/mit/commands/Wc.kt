package ru.spbau.mit.commands

/**
 * Created by rebryk on 9/7/16.
 */

/**
 * Command counts number of lines, words, characters in the given string
 */
class Wc : Command {
    override fun execute(input: String, shell: Shell): String {
        var wordsCount = 0
        var linesCount = 0

        input.split('\n').forEach { line ->
           ++linesCount
            wordsCount += line.split(' ').size
        };

        return String.format("%d %d %d", linesCount, wordsCount, input.length)
    }
}