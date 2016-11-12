package ru.spbau.mit.commands

/**
 * Command counts number of lines, words, characters in the given string
 */
class Wc : Command {
    override fun execute(input: String): String {
        var wordsCount = 0
        var linesCount = 0

        input.split('\n').forEach { line ->
           ++linesCount
            wordsCount += line.split(' ').size
        };

        return String.format("%d %d %d", linesCount, wordsCount, input.length)
    }
}