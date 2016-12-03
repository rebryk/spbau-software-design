package ru.spbau.mit.painter

import ru.spbau.mit.artefacts.MedicineChest
import ru.spbau.mit.artefacts.Shield
import ru.spbau.mit.artefacts.Sword
import ru.spbau.mit.creatures.Goblin
import ru.spbau.mit.creatures.Scavenger
import ru.spbau.mit.creatures.Player
import ru.spbau.mit.world.ArrayMap
import ru.spbau.mit.world.World

interface Painter {
    fun clear()
    fun show()

    fun draw(artifact: Sword, params: DrawingParameters?)
    fun draw(artifact: Shield, params: DrawingParameters?)
    fun draw(artifact: MedicineChest, params: DrawingParameters?)

    fun draw(creature: Player, params: DrawingParameters?)
    fun draw(creature: Goblin, params: DrawingParameters?)
    fun draw(creature: Scavenger, params: DrawingParameters?)

    fun draw(world: World, params: DrawingParameters?)
    fun draw(map: ArrayMap, params: DrawingParameters?)
}