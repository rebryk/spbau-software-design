package ru.spbau.mit.artefacts

import ru.spbau.mit.painter.DrawingParameters
import ru.spbau.mit.painter.Painter

class MedicineChest : Artifact() {
    init {
        hp = 100.0
        disposable = true
    }

    override fun draw(painter: Painter, params: DrawingParameters?) = painter.draw(this, params)
}