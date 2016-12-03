package ru.spbau.mit.artefacts

import ru.spbau.mit.painter.DrawingParameters
import ru.spbau.mit.painter.Painter

class Sword : Artifact() {
    init {
        damage = 10.0
    }

    override fun draw(painter: Painter, params: DrawingParameters?) = painter.draw(this, params)
}