package ru.spbau.mit.artefacts

import ru.spbau.mit.creatures.CreatureParameters
import ru.spbau.mit.painter.Drawable

abstract class Artifact : CreatureParameters(), Drawable {
    protected var used: Boolean = false
    protected var disposable: Boolean = false

    /**
     * Improves parameters of the creature according to artifact properties
     * If artifact is disposable, next activation will improve nothing
     * @param params parameters of the creature
     */
    fun activate(params: CreatureParameters) {
        if (!used) {
            params.hp += hp
            params.maxHp += maxHp
            params.damage += damage
            params.armor += armor
            params.hpGenerationSpeed += hpGenerationSpeed
        }

        used = disposable
    }

    /**
     * Returns parameters of the creature to the initial state
     * @param params parameters of the creature
     */
    fun deactivate(params: CreatureParameters) {
        params.maxHp -= maxHp
        params.damage -= damage
        params.armor -= armor
        params.hpGenerationSpeed -= hpGenerationSpeed
    }
}