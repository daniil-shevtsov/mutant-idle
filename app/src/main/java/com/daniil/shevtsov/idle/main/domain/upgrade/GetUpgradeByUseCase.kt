package com.daniil.shevtsov.idle.main.domain.upgrade

import javax.inject.Inject

class GetUpgradeByUseCase @Inject constructor(
    private val upgradeRepository: UpgradeRepository,
) {
    suspend operator fun invoke(id: Long): Upgrade? {
        return upgradeRepository.getUpgradeBy(id)
    }
}