package com.daniil.shevtsov.idle.main.domain.upgrade

import com.daniil.shevtsov.idle.main.ui.resource.GetCurrentResourceUseCase
import javax.inject.Inject

class BuyUpgradeUseCase @Inject constructor(
    private val getUpgradeBy: GetUpgradeByUseCase,
    private val decreaseResourceBy: DecreaseResourceByUseCase,
    private val updateUpgradeStatus: UpdateUpgradeStatusUseCase,
    private val getCurrentResource: GetCurrentResourceUseCase,
) {

    suspend operator fun invoke(id: Long) {
        val upgrade = getUpgradeBy(id) ?: return

        val upgradePrice = upgrade.price.value
        val currentResource = getCurrentResource().value

        if (upgradePrice <= currentResource) {
            decreaseResourceBy(upgradePrice)
            updateUpgradeStatus(id, UpgradeStatus.Bought)
        }
    }

}