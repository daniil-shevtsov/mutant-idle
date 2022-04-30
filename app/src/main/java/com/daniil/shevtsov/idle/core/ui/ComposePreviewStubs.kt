package com.daniil.shevtsov.idle.core.ui

import com.daniil.shevtsov.idle.feature.action.presentation.ActionIcon
import com.daniil.shevtsov.idle.feature.action.presentation.ActionModel
import com.daniil.shevtsov.idle.feature.action.presentation.ActionPane
import com.daniil.shevtsov.idle.feature.action.presentation.ActionsState
import com.daniil.shevtsov.idle.feature.debug.presentation.DebugViewState
import com.daniil.shevtsov.idle.feature.player.job.presentation.PlayerJobModel
import com.daniil.shevtsov.idle.feature.player.species.presentation.PlayerSpeciesModel
import com.daniil.shevtsov.idle.feature.ratio.presentation.HumanityRatioModel
import com.daniil.shevtsov.idle.feature.resource.presentation.ResourceModel
import com.daniil.shevtsov.idle.feature.shop.presentation.ShopState
import com.daniil.shevtsov.idle.feature.suspicion.presentation.SuspicionModel
import com.daniil.shevtsov.idle.feature.upgrade.presentation.PriceModel
import com.daniil.shevtsov.idle.feature.upgrade.presentation.UpgradeModel
import com.daniil.shevtsov.idle.feature.upgrade.presentation.UpgradeStatusModel

internal fun resourcePreviewStub() = ResourceModel(
    name = "Blood",
    value = "10 000",
)

internal fun resourceStubs() = listOf(
    ResourceModel(name = "Blood", value = "10 000"),
    ResourceModel(name = "Money", value = "500"),
)

internal fun ratiosStubs() = listOf(
    HumanityRatioModel(
        name = "Dormant",
        percent = 0.5,
    ),
    HumanityRatioModel(
        name = "Suspicion",
        percent = 0.25,
    ),
)

internal fun humanityRatioStub() = HumanityRatioModel(
    name = "Dormant",
    percent = 0.5,
)

internal fun suspicionStub() = SuspicionModel(
    title = "Investigation",
    percent = 0.5
)

internal fun actionStatePreviewStub() = ActionsState(
    actionPanes = listOf(
        actionPanePreviewStub(icon = ActionIcon.Human),
        actionPanePreviewStub(icon = ActionIcon.Mutant),
    ),
)

internal fun actionPanePreviewStub(
    icon: ActionIcon,
) = ActionPane(
    actions = (0..10).map { index ->
        actionPreviewStub(
            id = index.toLong(),
            title = "${icon.name} action",
            subtitle = "Some very very very very long text",
            actionIcon = icon,
        )
    }
)

internal fun actionPreviewStub(
    id: Long = 0L,
    title: String = "Lol Kek",
    subtitle: String = "Some very very very very long text",
    actionIcon: ActionIcon = ActionIcon.Human,
    isEnabled: Boolean = true,
) = ActionModel(
    id = id,
    title = title,
    subtitle = subtitle,
    icon = actionIcon,
    isEnabled = isEnabled,
)

internal fun shopStatePreviewStub(

) = ShopState(
    upgradeLists = (0..3).map { index ->
        upgradeListPreviewStub().map { it.copy(title = "$index ${it.title}") }
    }
)

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
