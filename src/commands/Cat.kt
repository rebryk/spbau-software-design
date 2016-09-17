import java.nio.file.Files
import java.nio.file.Paths

/**
 * Created by rebryk on 9/10/16.
 */

class Cat : Command {
    override fun execute(input: String): String {
        if (!Files.exists(Paths.get(input))) {
            println(String.format("Error: no such file \'%s\'!", input))
            return ""
        }

        return String(Files.readAllBytes(Paths.get(input)))
    }
}