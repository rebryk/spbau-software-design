/**
 * Created by rebryk on 9/7/16.
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