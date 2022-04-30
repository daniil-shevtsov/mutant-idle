package com.daniil.shevtsov.idle.feature.upgrade.domain

import com.daniil.shevtsov.idle.feature.flavor.Flavors
import com.daniil.shevtsov.idle.feature.tagsystem.domain.TagRelation
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tags

fun createUpgrades() = listOf(
    upgrade(
        id = 1L,
        title = "Fangs",
        subtitle = "Grow very sharp fangs. They are almost useless without stronger jaws though",
        price = 25.0,
        status = UpgradeStatus.NotBought,
        tags = mapOf(
            TagRelation.Provides to listOf(Tags.Body.Fangs),
            TagRelation.RequiredAny to listOf(Tags.Species.Devourer),
        )
    ),
    upgrade(
        id = 2L,
        title = "Iron jaws",
        subtitle = "Your jaws become stronger than any shark",
        price = 10.0,
        status = UpgradeStatus.NotBought,
        tags = mapOf(
            TagRelation.Provides to listOf(Tags.Body.IronJaws),
            TagRelation.RequiredAny to listOf(Tags.Species.Devourer),
        )
    ),
    upgrade(
        id = 3L,
        title = "Super Strength",
        subtitle = "Lifting a car or crushing a lock is not a problem anymore",
        price = 25.0,
        tags = mapOf(
            TagRelation.Provides to listOf(Tags.Body.SuperStrength),
            TagRelation.RequiredAny to listOf(
                Tags.Species.Devourer,
                Tags.Species.ShapeShifter,
                Tags.Species.Demon,
                Tags.Species.Vampire,
                Tags.Species.Android,
            ),
        )
    ),
    upgrade(
        id = 4L,
        title = "Invisibility",
        subtitle = Flavors.invisibilityGain.placeholder,
        price = 1.0,
        tags = mapOf(
            TagRelation.Provides to listOf(Tags.Abilities.Invisibility),
            TagRelation.RequiredAny to listOf(
                Tags.Species.Vampire,
                Tags.Species.Android,
                Tags.Species.Alien,
            ),
        )
    ),

)
