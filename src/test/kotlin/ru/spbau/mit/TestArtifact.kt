package ru.spbau.mit

import org.junit.Test
import ru.spbau.mit.artefacts.Artifact
import ru.spbau.mit.artefacts.ArtifactFactory
import ru.spbau.mit.creatures.Creature
import ru.spbau.mit.creatures.CreatureFactory
import kotlin.test.assertEquals

class TestArtifact {
    private val creature: Creature
    private val sword: Artifact
    private val medicineChest: Artifact

    init {
        creature = CreatureFactory.create(CreatureFactory.CreatureType.GOBLIN)
        sword = ArtifactFactory.create(ArtifactFactory.ArtifactType.SWORD)
        medicineChest = ArtifactFactory.create(ArtifactFactory.ArtifactType.MEDICINE_CHEST)

        creature.hp = 0.0
        creature.addArtifact(sword)
        creature.addArtifact(medicineChest)
    }

    @Test
    fun testActivation() {
        creature.activateArtifact(sword)
        assertEquals(15.0, creature.damage)
    }

    @Test
    fun testDeactivation() {
        creature.deactivateArtifact(sword)
        assertEquals(5.0, creature.damage)
    }

    @Test
    fun testDisposableArtifact() {
        creature.activateArtifact(medicineChest)
        assertEquals(10.0, creature.hp)
        creature.deactivateArtifact(medicineChest)

        creature.activateArtifact(medicineChest)
        assertEquals(10.0, creature.hp)
    }
}
