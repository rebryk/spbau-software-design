package ru.spbau.mit.creatures

import ru.spbau.mit.painter.DrawingParameters
import ru.spbau.mit.painter.Painter
import ru.spbau.mit.strategies.Strategy
import ru.spbau.mit.strategies.PlayerStrategy

class PlayerCreature : Creature() {
    override val strategy: Strategy = PlayerStrategy()

    init {
        hp = 100.0
        maxHp = 100.0
        damage = 10.0
        hpGenerationSpeed = 2.0
    }

    override fun draw(painter: Painter, params: DrawingParameters?) = painter.draw(this, params)
}