package com.daniil.shevtsov.idle.feature.upgrade.domain

import com.daniil.shevtsov.idle.feature.tagsystem.domain.TagRelation
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tags

fun createUpgrades() = listOf(
    upgrade(
        id = 0L,
        title = "Hand-sword",
        subtitle = "Transform your hand into a sharp blade",
        price = 50.0,
        status = UpgradeStatus.NotBought,
        tags = mapOf(
            Tags.Body.HandSword to TagRelation.Provides,
        )
    ),
    upgrade(
        id = 1L,
        title = "Fangs",
        subtitle = "Grow very sharp fangs. They are almost useless without stronger jaws though",
        price = 25.0,
        status = UpgradeStatus.NotBought,
        tags = mapOf(
            Tags.Body.Fangs to TagRelation.Provides,
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
        )
    ),
)
