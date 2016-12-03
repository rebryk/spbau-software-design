package ru.spbau.mit.creatures

import ru.spbau.mit.painter.DrawingParameters
import ru.spbau.mit.painter.Painter
import ru.spbau.mit.strategies.GreedyStrategy
import ru.spbau.mit.strategies.Strategy

class Goblin : Creature() {
    override val strategy: Strategy = GreedyStrategy()

    init {
        hp = 30.0
        maxHp = 30.0
        damage = 5.0
        hpGenerationSpeed = 1.0
    }

    override fun draw(painter: Painter, params: DrawingParameters?) = painter.draw(this, params)
}