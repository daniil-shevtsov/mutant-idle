package com.daniil.shevtsov.idle.main.domain.upgrade

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveUpgradesUseCase @Inject constructor(
    private val upgradeRepository: UpgradeRepository,
) {

    operator fun invoke(): Flow<List<Upgrade>> = upgradeRepository.observe()

}