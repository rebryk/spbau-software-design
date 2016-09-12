/**
 * Created by rebryk on 9/12/16.
 */

fun test_cat(shell: Command): Boolean {
    try {
        shell.execute("file = \'src/main.kt\'")
        val output1 = shell.execute("cat src/main.kt | wc")
        val output2 = shell.execute("cat \$file | wc")
        return output1.compareTo(output2) == 0
    } catch(e: Exception) {
        println(String.format("Error: unexpected exception \'%s!\'", e.toString()))
        return false
    }
}

fun test_echo(shell: Command): Boolean {
    try {
        return shell.execute("echo 'test message'").compareTo("test message") == 0;
    } catch(e: Exception) {
        println(String.format("Error: unexpected exception \'%s!\'", e.toString()))
        return false
    }
}

fun main(args: Array<String>) {
    val shell = Shell()
    shell.registerCommand("echo", Echo())
    shell.registerCommand("pwd", Pwd())
    shell.registerCommand("wc", Wc())
    shell.registerCommand("cat", Cat())

    if (test_cat(shell)) {
        println("Test 1 passed.")
    } else {
        println("Test 1 failed.")
    }

    if (test_echo(shell)) {
        println("Test 2 passed.")
    } else {
        println("Test 2 failed.")
    }
}
