package ru.spbau.mit.painter

interface Drawable {
    fun draw(painter: Painter, params: DrawingParameters? = null): Unit
}