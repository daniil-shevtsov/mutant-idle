package com.daniil.shevtsov.idle.feature.upgrade.domain

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
            Tags.Body.Fangs to TagRelation.Provides,
            Tags.Species.Devourer to TagRelation.Required,
        )
    ),
    upgrade(
        id = 2L,
        title = "Iron jaws",
        subtitle = "Your jaws become stronger than any shark",
        price = 10.0,
        status = UpgradeStatus.NotBought,
        tags = mapOf(
            Tags.Body.IronJaws to TagRelation.Provides,
            Tags.Species.Devourer to TagRelation.Required,
        )
    ),
    upgrade(
        id = 3L,
        title = "Super Strength",
        subtitle = "Lifting a car or crushing a lock is not a problem anymore",
        price = 25.0,
        tags = mapOf(
            Tags.Body.SuperStrength to TagRelation.Provides,
            Tags.Species.Devourer to TagRelation.Required,
        )
    ),
    upgrade(

    )

)
