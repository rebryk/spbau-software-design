package ru.spbau.mit

import ru.spbau.mit.artefacts.ArtifactFactory
import ru.spbau.mit.creatures.CreatureFactory
import ru.spbau.mit.painter.ASCIIPainter
import ru.spbau.mit.painter.Painter
import ru.spbau.mit.world.ArrayMap
import ru.spbau.mit.world.World

class Roguelike(fileName: String) {
    private val painter: Painter = ASCIIPainter()
    private val world: World

    /**
     * Initializes the world
     * Also it's possible to generate it
     */
    init {
        world = World.create {
            map = ArrayMap(fileName)
            creatureAt(2, 2) { CreatureFactory.create(CreatureFactory.CreatureType.GOBLIN) }
            creatureAt(5, 5) { CreatureFactory.create(CreatureFactory.CreatureType.PLAYER) }
            artifactAt(4, 14) { ArtifactFactory.create(ArtifactFactory.ArtifactType.SWORD) }
            artifactAt(7, 18) { ArtifactFactory.create(ArtifactFactory.ArtifactType.SHIELD) }
        }
    }

    fun start() {
        do {
            painter.clear()
            world.draw(painter)
            painter.show()
        } while (Model.run(world))
    }
}