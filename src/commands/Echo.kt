/**
 * Created by rebryk on 9/7/16.
 */

class Echo : Command {
    override fun execute(input: String): String {
        println(unwrap(input))
        return ""
    }
}