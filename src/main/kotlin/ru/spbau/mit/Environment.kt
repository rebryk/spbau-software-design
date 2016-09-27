package ru.spbau.mit

import java.nio.file.Paths

class Environment {
    var currentDirectory: String = Paths.get(".").toAbsolutePath().toString()
}