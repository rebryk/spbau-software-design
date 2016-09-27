package ru.spbau.mit.commands

import org.apache.commons.cli.DefaultParser
import org.apache.commons.cli.Options
import org.apache.commons.cli.ParseException
import ru.spbau.mit.Environment
import ru.spbau.mit.splitBy
import ru.spbau.mit.unwrap
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Created by rebryk on 9/17/16.
 */

/**
 * Command finds pattern in the files
 */
class Grep : Command {
    private val parser = DefaultParser()
    private val options = Options()

    init {
        options.addOption("i", "Ignore case")
        options.addOption("w", "Word regexp")
        options.addOption("A", "After context")
    }

    private fun match(text: String, regex: Regex, w: Boolean): Boolean {
        if (w) {
            text.split(' ').forEach { if (regex.matches(it)) { return true } }
            return false
        }

        return regex.containsMatchIn(text)
    }

    private fun grep(i: Boolean, w: Boolean, A: Int, pattern: String, lines: List<String>): String {
        val builder = StringBuilder()
        val options = mutableSetOf<RegexOption>()

        if (i) {
            options.add(RegexOption.IGNORE_CASE)
        }

        val regex = unwrap(pattern).toRegex(options)

        var l = 0;
        while (l < lines.size) {
            if (match(lines[l], regex, w)) {
                lines.subList(l, Math.min(l + A, lines.size)).forEach { builder.append(it).append('\n') }
                l += A
            } else {
                l += 1
            }
        }

        if (builder.length > 0) {
            builder.deleteCharAt(builder.length - 1)
        }

        return builder.toString()
    }

    override fun execute(input: String, env: Environment): String {
        val args = splitBy(input, ' ')

        val commandLine = try {
            parser.parse(options, args.toTypedArray())
        } catch(e: ParseException) {
            println(e.message)
            return ""
        }

        val ignoreCase = commandLine.hasOption("i")
        val wordRegexp = commandLine.hasOption("w")
        val afterContext = commandLine.getOptionValue("A", "1").toInt()

        val pattern = commandLine.args.getOrNull(0)
        val file = commandLine.args.getOrNull(1)

        if (pattern == null) {
            println("Error: no pattern!")
            return ""
        }

        if (file == null) {
            println("Error: no file!")
            return ""
        }

        val path = Paths.get(env.currentDirectory).resolve(file)
        if (!Files.exists(path)) {
            println(String.format("Error: no such file \'%s\'!", file))
            return "";
        }

        return grep(ignoreCase, wordRegexp, afterContext, pattern,
                Files.readAllLines(path))
    }
}