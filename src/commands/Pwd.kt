/**
 * Created by rebryk on 9/7/16.
 */

class Pwd : Command {
    override fun execute(input: String) : String {
        return System.getProperty("user.dir")
    }
}
