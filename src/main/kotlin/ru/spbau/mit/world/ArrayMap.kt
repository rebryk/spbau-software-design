package ru.spbau.mit.world

import ru.spbau.mit.painter.DrawingParameters
import ru.spbau.mit.painter.Painter
import java.nio.file.Paths

/**
 * Class to store walls
 */
class ArrayMap(fileName: String) : WorldMap {
    val walls: List<List<Boolean>>

    /**
     * Reads map from the given file
     */
    init {
        walls = Paths.get(fileName).toFile().let {
            if (!it.exists()) {
                throw NoSuchFileException(it)
            }

            it.readLines().map { it.map { it == '#' }.toMutableList() }
        }
    }

    override fun isWall(x: Int, y: Int): Boolean {
        return walls.getOrNull(x) == null || walls[x].getOrNull(y) == null || walls[x][y]
    }

    override fun draw(painter: Painter, params: DrawingParameters?) = painter.draw(this, params)
}