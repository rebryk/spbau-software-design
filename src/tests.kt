/**
 * Created by rebryk on 9/12/16.
 */

interface Test {
    fun test(shell: Command): Boolean

    fun run(shell: Command): Boolean {
        try {
            return test(shell)
        } catch(e: Exception) {
            println(String.format("Error: unexpected exception \'%s!\'", e.toString()))
            return false
        }
    }
}

class TestCat : Test {
    override fun test(shell: Command): Boolean {
        shell.execute("file = \'src/main.kt\'")
        return shell.execute("cat src/main.kt | wc").compareTo(shell.execute("cat \$file | wc")) == 0
    }
}

class TestEcho : Test {
    override fun test(shell: Command): Boolean {
        return shell.execute("echo 'test message'").compareTo("test message") == 0
    }
}

class TestGrep : Test {
    override fun test(shell: Command): Boolean {
        return shell.execute("grep main src/main.kt | wc").compareTo("1 4 31") == 0
    }
}

fun main(args: Array<String>) {
    val shell = Shell()
    shell.registerCommand("echo", Echo())
    shell.registerCommand("pwd", Pwd())
    shell.registerCommand("wc", Wc())
    shell.registerCommand("cat", Cat())
    shell.registerCommand("grep", Grep())

    arrayListOf(TestCat(), TestEcho(), TestGrep()).map {
        if (it.run(shell)) {
            println(it.javaClass.name + " passed!")
        } else {
            println(it.javaClass.name + " failed!")
        }
    }
}
