package com.daniil.shevtsov.idle.main.domain.upgrade

import javax.inject.Inject

class UpdateUpgradeStatusUseCase @Inject constructor(
    private val upgradeRepository: UpgradeRepository,
) {
    suspend operator fun invoke(id: Long, newStatus: UpgradeStatus) {
        upgradeRepository.updateUpgradeStatus(id, newStatus)
    }
}
