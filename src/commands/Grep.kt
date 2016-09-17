import com.evalab.core.cli.exception.OptionException
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Created by rebryk on 9/17/16.
 */

class Grep : Command {
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

    override fun execute(input: String): String {
        val args = splitBy(input, ' ')

        val command =  com.evalab.core.cli.Command("grep", "")
        command.addBooleanOption("ignore-case", false, 'i', null)
        command.addBooleanOption("word-regexp", false, 'w', null)
        command.addIntegerOption("after-context", false, 'A', null)

        try {
            command.parse(args.toTypedArray())
        } catch(e: OptionException) {
            println(e.message)
            return ""
        }

        val ignore_case = command.getBooleanValue("ignore-case", false)
        val word_regexp = command.getBooleanValue("word-regexp", false)
        val after_context = command.getIntegerValue("after-context", 1)

        var args_cnt = 0;

        if (ignore_case == true) {
            ++args_cnt
        }

        if (word_regexp == true) {
            ++args_cnt
        }

        if (after_context!! > 1) {
            args_cnt += 2
        }

        val pattern = args.getOrNull(args_cnt)
        val file = args.getOrNull(args_cnt + 1)

        if (pattern == null) {
            println("Error: no pattern!")
            return ""
        }

        if (file == null) {
            println("Error: no file!")
            return ""
        }

        if (!Files.exists(Paths.get(file))) {
            println(String.format("Error: no such file \'%s\'!", file))
            return "";
        }

        return grep(ignore_case!!, word_regexp!!, after_context, pattern, Files.readAllLines(Paths.get(file)))
    }
}