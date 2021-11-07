package com.daniil.shevtsov.idle.main.domain.upgrade

import javax.inject.Inject

class BuyUpgradeUseCase @Inject constructor(
    private val getUpgradeBy: GetUpgradeByUseCase,
    private val decreaseResourceBy: DecreaseResourceByUseCase,
    private val updateUpgradeStatus: UpdateUpgradeStatusUseCase,
) {

    suspend operator fun invoke(id: Long) {

    }

}