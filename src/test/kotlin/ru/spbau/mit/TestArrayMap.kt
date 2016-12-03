package ru.spbau.mit

import org.junit.Test
import ru.spbau.mit.world.ArrayMap
import kotlin.test.assertEquals

class TestArrayMap {
    private val map: ArrayMap = ArrayMap("maps/small.txt")

    @Test
    fun testWallsCount() {
        var wallsCount: Int = 0
        map.walls.forEach { it.forEach { wallsCount += if (it) 1 else 0 } }
        assertEquals(19, wallsCount)
    }
}