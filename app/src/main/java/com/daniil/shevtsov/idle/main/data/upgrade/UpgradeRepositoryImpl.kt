package com.daniil.shevtsov.idle.main.data.upgrade

import com.daniil.shevtsov.idle.main.domain.upgrade.Price
import com.daniil.shevtsov.idle.main.domain.upgrade.Upgrade
import com.daniil.shevtsov.idle.main.domain.upgrade.UpgradeStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

//TODO: Replace with real logic
class UpgradeRepositoryImpl @Inject constructor() : UpgradeRepository {

    private val upgradeStorage: MutableStateFlow<Map<Long, Upgrade>> =
        MutableStateFlow(createUpgrades())

    override fun observe(): Flow<List<Upgrade>> {
        return upgradeStorage.map { it.values.toList() }
    }

    override suspend fun getUpgradeBy(id: Long): Upgrade? {
        return upgradeStorage.value[id]
    }

    override suspend fun updateUpgradeStatus(id: Long, status: UpgradeStatus) {
        val upgradeMap = upgradeStorage.value.toMutableMap()
        upgradeStorage.value = upgradeMap.apply {
            val oldUpgrade = get(id)
            if(oldUpgrade != null) {
                set(id, oldUpgrade.copy(status = status))
            }

        }
    }

    private fun createUpgrades() = listOf(
        Upgrade(
            id = 0L,
            title = "Hand-sword",
            subtitle = "Transform your hand into a sharp blade",
            price = Price(value = 50.0),
            status = UpgradeStatus.NotBought,
        ),
        Upgrade(
            id = 1L,
            title = "Fangs",
            subtitle = "Grow very sharp fangs. They are almost useless without stronger jaws though",
            price = Price(value = 25.0),
            status = UpgradeStatus.NotBought,
        ),
        Upgrade(
            id = 2L,
            title = "Iron jaws",
            subtitle = "Your jaws become stronger than any shark",
            price = Price(value = 10.0),
            status = UpgradeStatus.NotBought,
        ),
    ).associateBy { it.id }
}