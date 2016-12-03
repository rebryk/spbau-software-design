package ru.spbau.mit.creatures

import ru.spbau.mit.strategies.Action
import ru.spbau.mit.artefacts.Artifact
import ru.spbau.mit.painter.Drawable
import ru.spbau.mit.strategies.Strategy
import ru.spbau.mit.world.World

abstract class Creature : CreatureParameters(), Drawable {
    abstract val strategy: Strategy
    val artifacts: MutableList<Artifact> = mutableListOf()
    val activatedArtifacts: MutableList<Artifact> = mutableListOf()

    fun addArtifact(artifact: Artifact) = artifacts.add(artifact)

    /**
     * Runs the strategy to get next move
     * @param from current position of the creature
     * @param world description of the world
     */
    fun move(from: Pair<Int, Int>, world: World): Action = strategy.move(this, from, world)

    /**
     * Activates the given artifact
     * Activation of the artifact improves creature parameters according to the artifact properties
     * @param artifact for activation
     */
    fun activateArtifact(artifact: Artifact) {
        if (artifacts.remove(artifact)) {
            artifact.activate(this)
            activatedArtifacts.add(artifact)
        }
    }

    /**
     * Deactivates the given artifact
     * Deactivation of the artifact returns creature parameters to the initial state
     * @param artifact for deactivation
     */
    fun deactivateArtifact(artifact: Artifact) {
        if (activatedArtifacts.remove(artifact)) {
            artifact.deactivate(this)
            artifacts.add(artifact)
        }
    }

    fun heal() {
        hp = Math.max(0.0, Math.min(hp + hpGenerationSpeed, maxHp))
    }

    fun isDead() = hp == 0.0
}