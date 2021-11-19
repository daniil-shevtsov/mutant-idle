package com.daniil.shevtsov.idle.main.ui

import com.daniil.shevtsov.idle.main.ui.actions.ActionModel
import com.daniil.shevtsov.idle.main.ui.actions.ActionPane
import com.daniil.shevtsov.idle.main.ui.actions.ActionsState
import com.daniil.shevtsov.idle.main.ui.resource.ResourceModel
import com.daniil.shevtsov.idle.main.ui.shop.ShopState
import com.daniil.shevtsov.idle.main.ui.upgrade.PriceModel
import com.daniil.shevtsov.idle.main.ui.upgrade.UpgradeModel
import com.daniil.shevtsov.idle.main.ui.upgrade.UpgradeStatusModel

internal fun viewStatePreviewStub() = MainViewState.Success(
    resources = listOf(resourcePreviewStub()),
    actionState = actionStatePreviewStub(),
    shop = shopStatePreviewStub(),
)

internal fun resourcePreviewStub() = ResourceModel(
    name = "Blood",
    value = "10 000",
)

internal fun actionStatePreviewStub() = ActionsState(
    actionPanes = (0..3).map { paneIndex ->
        actionPanePreviewStub(paneIndex = paneIndex)
    }
)

internal fun actionPanePreviewStub(
    paneIndex: Int? = null
) = ActionPane(
    actions = (0..10).map { index ->
        actionPreviewStub(
            id = index.toLong(),
            title = "Action ${paneIndex ?: 0}$index",
            subtitle = "Some very very very very long text",
        )
    }
)

internal fun actionPreviewStub(
    id: Long = 0L,
    title: String = "Lol Kek",
    subtitle: String = "Some very very very very long text",
) = ActionModel(
    id = id,
    title = title,
    subtitle = subtitle,
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