package com.daniil.shevtsov.idle.main.data.upgrade

import com.daniil.shevtsov.idle.main.domain.upgrade.Price
import com.daniil.shevtsov.idle.main.domain.upgrade.Upgrade
import com.daniil.shevtsov.idle.main.domain.upgrade.UpgradeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

//TODO: Replace with real logic
class UpgradeRepositoryImpl @Inject constructor() : UpgradeRepository {

    override fun observe(): Flow<List<Upgrade>> {
        val upgrades = createUpgrades()
        return flowOf(upgrades)
    }

    override suspend fun getUpgradeBy(id: Long): Upgrade? {
        return createUpgrades().map { it.id to it }.toMap()[id]
    }


    private fun createUpgrades() = listOf(
        Upgrade(
            id = 0L,
            title = "Hand-sword",
            subtitle = "Transform your hand into a sharp blade",
            price = Price(value = 150.0),
        ),
        Upgrade(
            id = 1L,
            title = "Fangs",
            subtitle = "Grow very sharp fangs. They are almost useless without stronger jaws though",
            price = Price(value = 25.0),
        ),
        Upgrade(
            id = 2L,
            title = "Iron jaws",
            subtitle = "Your jaws become stronger than any shark",
            price = Price(value = 70.0),
        ),
    )
}