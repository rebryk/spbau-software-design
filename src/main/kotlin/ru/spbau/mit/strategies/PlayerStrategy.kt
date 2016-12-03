package ru.spbau.mit.strategies

import ru.spbau.mit.creatures.Creature
import ru.spbau.mit.world.World

class PlayerStrategy : Strategy {
    override fun move(creature: Creature, from: Pair<Int, Int>, world: World): Action {
        val command = readLine()
        return when (command) {
            "w" -> Action.GO_UP
            "s" -> Action.GO_DOWN
            "a" -> Action.GO_LEFT
            "d" -> Action.GO_RIGHT
            else -> {
                val args = command!!.split(' ')
                if (args.size == 2) {
                    try {
                        val index = args[1].toInt() - 1
                        when (args[0]) {
                            "activate" -> creature.artifacts.getOrNull(index)?.let {
                                creature.activateArtifact(it)
                            }
                            "deactivate" -> creature.activatedArtifacts.getOrNull(index)?.let {
                                creature.deactivateArtifact(it)
                            }
                        }
                    } catch (e: NumberFormatException) {
                        println("Error: Incorrect index!")
                    }
                }

                Action.SKIP
            }
        }
    }
}