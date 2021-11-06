package com.daniil.shevtsov.idle.main.data.upgrade

import com.daniil.shevtsov.idle.main.domain.upgrade.Upgrade
import com.daniil.shevtsov.idle.main.domain.upgrade.UpgradeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class UpgradeRepositoryImpl @Inject constructor() : UpgradeRepository {

    override fun observe(): Flow<List<Upgrade>> {
        val upgrades = createUpgrades() + createUpgrades() + createUpgrades()
        return flowOf(upgrades)
    }

    //TODO: Replace with real logic
    private fun createUpgrades() = listOf(
        Upgrade(
            id = 0L,
            title = "Hand-sword",
            subtitle = "Transform your hand into a sharp blade"
        ),
        Upgrade(
            id = 1L,
            title = "Fangs",
            subtitle = "Grow very sharp fangs. They are almost useless without stronger jaws though"
        ),
        Upgrade(
            id = 2L,
            title = "Iron jaws",
            subtitle = "Your jaws become stronger than any shark"
        ),
    )
}