package com.daniil.shevtsov.idle.main.ui

import com.daniil.shevtsov.idle.main.ui.resource.ResourceModel
import com.daniil.shevtsov.idle.main.ui.upgrade.UpgradeModel

internal fun viewStatePreview() = MainViewState.Success(
    resource = resourcePreview(),
    upgrades = upgradeListPreview(),
)

internal fun resourcePreview() = ResourceModel(
    name = "Blood",
    value = "10 000",
)

internal fun upgradePreview() = UpgradeModel(
    id = 0L,
    title = "Hand-sword",
    subtitle = "Transform your hand into a sharp blade"
)

internal fun upgradeListPreview() = listOf(
    UpgradeModel(
        id = 0L,
        title = "Hand-sword",
        subtitle = "Transform your hand into a sharp blade"
    ),
    UpgradeModel(
        id = 1L,
        title = "Fangs",
        subtitle = "Grow very sharp fangs. They are almost useless without stronger jaws though"
    ),
    UpgradeModel(
        id = 2L,
        title = "Iron jaws",
        subtitle = "Your jaws become stronger than any shark"
    ),
)