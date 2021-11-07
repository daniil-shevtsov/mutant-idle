package com.daniil.shevtsov.idle.main.ui

import com.daniil.shevtsov.idle.main.ui.resource.ResourceModel
import com.daniil.shevtsov.idle.main.ui.shop.ShopState
import com.daniil.shevtsov.idle.main.ui.upgrade.PriceModel
import com.daniil.shevtsov.idle.main.ui.upgrade.UpgradeModel
import com.daniil.shevtsov.idle.main.ui.upgrade.UpgradeStatusModel

internal fun viewStatePreview() = MainViewState.Success(
    resource = resourcePreview(),
    upgrades = upgradeListPreview(),
)

internal fun resourcePreview() = ResourceModel(
    name = "Blood",
    value = "10 000",
)

internal fun shopStatePreview(

) = ShopState(
    upgradeLists = (0..3).map{ index ->
        upgradeListPreview().map { it.copy(title = "$index ${it.title}") }
    }
)

internal fun upgradePreview(
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

internal fun upgradeListPreview() = listOf(
    upgradePreview(
        id = 0L,
        title = "Hand-sword",
        subtitle = "Transform your hand into a sharp blade"
    ),
    upgradePreview(
        id = 1L,
        title = "Fangs",
        subtitle = "Grow very sharp fangs. They are almost useless without stronger jaws though"
    ),
    upgradePreview(
        id = 2L,
        title = "Iron jaws",
        subtitle = "Your jaws become stronger than any shark"
    ),
)