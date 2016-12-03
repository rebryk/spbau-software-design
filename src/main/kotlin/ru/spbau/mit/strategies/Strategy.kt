package ru.spbau.mit.strategies

import ru.spbau.mit.creatures.Creature
import ru.spbau.mit.world.World

interface Strategy {
    fun move(creature: Creature, from: Pair<Int, Int>, world: World): Action
}