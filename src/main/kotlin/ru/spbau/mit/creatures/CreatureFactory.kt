package ru.spbau.mit.creatures

import ru.spbau.mit.strategies.Action
import java.util.*

/**
 * Factory for generating creatures of different types
 */
object CreatureFactory {
    enum class CreatureType {
        GOBLIN,
        SCAVENGER,
        PLAYER;

        companion object {
            fun random(): Action = Action.values()[Random().nextInt(Action.values().size - 1)]
        }
    }

    /**
     * Creates creature of the given type
     * @param type creature type
     * @return creature of the given type
     */
    fun create(type: CreatureType): Creature = when (type) {
        CreatureType.GOBLIN -> Goblin()
        CreatureType.SCAVENGER -> Scavenger()
        CreatureType.PLAYER -> PlayerCreature()
        else -> throw RuntimeException("Not found creature $type")
    }
}