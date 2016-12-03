package ru.spbau.mit.creatures

import ru.spbau.mit.painter.DrawingParameters
import ru.spbau.mit.painter.Painter
import ru.spbau.mit.strategies.EmptyStrategy
import ru.spbau.mit.strategies.Strategy

class Scavenger : Creature() {
    override val strategy: Strategy = EmptyStrategy()

    init {
        hp = 30.0
        maxHp = 30.0
        damage = 5.0
        hpGenerationSpeed = 1.0
    }

    override fun draw(painter: Painter, params: DrawingParameters?) = painter.draw(this, params)
}