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
            params.maxHp = Math.max(0.0, params.maxHp + maxHp)
            params.hp = Math.max(0.0, Math.min(params.hp + hp, params.maxHp))
            params.damage = Math.max(0.0, params.damage + damage)
            params.armor = Math.max(0.0, params.armor + armor)
            params.hpGenerationSpeed += hpGenerationSpeed
        }

        used = disposable
    }

    /**
     * Returns parameters of the careature to the initial state
     * @param params parameters of the creature
     */
    fun deactivate(params: CreatureParameters) {
        params.maxHp = Math.max(0.0, params.maxHp - maxHp)
        params.damage = Math.max(0.0, params.damage - damage)
        params.armor = Math.max(0.0, params.armor - armor)
        params.hpGenerationSpeed -= hpGenerationSpeed
    }
}