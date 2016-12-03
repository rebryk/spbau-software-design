package ru.spbau.mit.artefacts

import ru.spbau.mit.painter.DrawingParameters
import ru.spbau.mit.painter.Painter

class Shield : Artifact() {
    init {
        armor = 20.0
    }

    override fun draw(painter: Painter, params: DrawingParameters?) = painter.draw(this, params)
}
