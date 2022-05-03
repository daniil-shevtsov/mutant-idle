package com.daniil.shevtsov.idle.core.ui

import com.daniil.shevtsov.idle.feature.debug.presentation.DebugViewState
import com.daniil.shevtsov.idle.feature.player.job.presentation.PlayerJobModel
import com.daniil.shevtsov.idle.feature.player.species.presentation.PlayerSpeciesModel
import com.daniil.shevtsov.idle.feature.upgrade.presentation.PriceModel
import com.daniil.shevtsov.idle.feature.upgrade.presentation.UpgradeModel
import com.daniil.shevtsov.idle.feature.upgrade.presentation.UpgradeStatusModel

internal fun upgradePreviewStub(
    id: Long = 0L,
    title: String = "Hand-sword",
    subtitle: String = "Transform your hand into a sharp blade",
    status: UpgradeStatusModel = UpgradeStatusModel.Affordable,
) = UpgradeModel(
    id = id,
    title = title,
    subtitle = subtitle,
    price = PriceModel("75"),
    status = status,
)

internal fun upgradeListPreviewStub() = listOf(
    upgradePreviewStub(
        id = 0L,
        title = "Hand-sword",
        subtitle = "Transform your hand into a sharp blade"
    ),
    upgradePreviewStub(
        id = 1L,
        title = "Fangs",
        subtitle = "Grow very sharp fangs. They are almost useless without stronger jaws though"
    ),
    upgradePreviewStub(
        id = 2L,
        title = "Iron jaws",
        subtitle = "Your jaws become stronger than any shark"
    ),
)

internal fun debugViewState(
    jobSelection: List<PlayerJobModel> = emptyList(),
    speciesSelection: List<PlayerSpeciesModel> = emptyList(),
) = DebugViewState(
    jobSelection = jobSelection,
    speciesSelection = speciesSelection,
)
