package ru.spbau.mit.world

import ru.spbau.mit.painter.Drawable

interface WorldMap : Drawable {
    fun isWall(x: Int, y: Int): Boolean
}