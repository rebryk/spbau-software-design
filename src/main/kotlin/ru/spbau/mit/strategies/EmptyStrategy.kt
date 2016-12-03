package ru.spbau.mit.strategies

import ru.spbau.mit.creatures.Creature
import ru.spbau.mit.world.World

class EmptyStrategy : Strategy {
    /**
     * Just does nothing
     * @param creature
     * @param from current position of the creature
     * @param world description of the world
     * @return SKIP action
     */
    override fun move(creature: Creature, from: Pair<Int, Int>, world: World): Action = Action.SKIP
}