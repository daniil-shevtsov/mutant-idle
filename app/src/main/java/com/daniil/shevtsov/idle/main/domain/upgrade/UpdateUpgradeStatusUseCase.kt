package com.daniil.shevtsov.idle.main.domain.upgrade

import javax.inject.Inject

class UpdateUpgradeStatusUseCase @Inject constructor() {
    suspend operator fun invoke(id: Long, newStatus: UpgradeStatus) {

    }
}
