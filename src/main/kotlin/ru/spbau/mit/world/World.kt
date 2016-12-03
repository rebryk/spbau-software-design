package ru.spbau.mit.world

import ru.spbau.mit.artefacts.Artifact
import ru.spbau.mit.creatures.Creature
import ru.spbau.mit.painter.Drawable
import ru.spbau.mit.painter.DrawingParameters
import ru.spbau.mit.painter.Painter

/**
 * Class to describe the state of the game
 * Stores map, creatures, artifacts
 */
class World private constructor(val map: WorldMap,
                                val creatures: MutableMap<Pair<Int, Int>, Creature>,
                                val artifacts: MutableMap<Pair<Int, Int>, MutableList<Artifact>>) : Drawable {

    private constructor(builder: Builder) : this(builder.map, builder.creatures, builder.artifacts)

    override fun draw(painter: Painter, params: DrawingParameters?) = painter.draw(this, params)

    companion object {
        fun create(init: Builder.() -> Unit) = Builder(init).build()
    }

    /**
     * Builder for creating instances of the World
     */
    class Builder private constructor() {
        lateinit var map: WorldMap
        val creatures: MutableMap<Pair<Int, Int>, Creature> = mutableMapOf()
        val artifacts: MutableMap<Pair<Int, Int>, MutableList<Artifact>> = mutableMapOf()

        constructor(init: Builder.() -> Unit) : this() {
            init()
        }

        fun build(): World = World(this)

        /**
         * Creates a creature in the given position
         * @param x coordinate of the position
         * @param y coordinate of the position
         * @param init method to create creature
         * @return builder
         */
        fun creatureAt(x: Int, y: Int, init: Builder.() -> Creature): Builder = apply {
            creatures.put(Pair(x, y), init())
        }

        /**
         * Creates an artifact in the given position
         * @param x coordinate of the position
         * @param y coordinate of the position
         * @param init method to create artifact
         * @return builder
         */
        fun artifactAt(x: Int, y: Int, init: Builder.() -> Artifact): Builder = apply {
            artifacts.getOrPut(Pair(x, y), { mutableListOf() }).add(init())
        }
    }
}