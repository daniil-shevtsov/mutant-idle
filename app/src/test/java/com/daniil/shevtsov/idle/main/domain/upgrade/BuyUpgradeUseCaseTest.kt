package com.daniil.shevtsov.idle.main.domain.upgrade

import com.daniil.shevtsov.idle.main.ui.resource.GetCurrentResourceUseCase
import com.daniil.shevtsov.idle.util.resource
import com.daniil.shevtsov.idle.util.upgrade
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test

internal class BuyUpgradeUseCaseTest {

    private val getUpgradeBy: GetUpgradeByUseCase = mockk()
    private val decreaseResourceBy: DecreaseResourceByUseCase = mockk(relaxUnitFun = true)
    private val updateUpgradeStatus: UpdateUpgradeStatusUseCase = mockk(relaxUnitFun = true)
    private val getCurrentResource: GetCurrentResourceUseCase = mockk()

    private val buyUpgrade: BuyUpgradeUseCase by lazy {
        createUseCase()
    }

    @Test
    fun `should buy upgrade if affordable`() = runBlockingTest {
        val upgradeId = 1L
        val upgradePrice = 30.0
        val currentResource = 50.0

        coEvery { getUpgradeBy(upgradeId) } returns upgrade(id = upgradeId, price = upgradePrice)
        coEvery { getCurrentResource() } returns resource(value = currentResource)

        buyUpgrade(id = upgradeId)

        coVerify { decreaseResourceBy(upgradePrice) }
        coVerify { updateUpgradeStatus(upgradeId, UpgradeStatus.Bought) }
    }

    @Test
    fun `should not buy upgrade if price higher than resource`() = runBlockingTest {
        val upgradeId = 1L
        val upgradePrice = 30.0
        val currentResource = 10.0

        coEvery { getUpgradeBy(upgradeId) } returns upgrade(id = upgradeId, price = upgradePrice)
        coEvery { getCurrentResource() } returns resource(value = currentResource)

        buyUpgrade(id = upgradeId)

        coVerify(exactly = 0) { decreaseResourceBy(upgradePrice) }
        coVerify(exactly = 0) { updateUpgradeStatus(upgradeId, UpgradeStatus.Bought) }
    }

    private fun createUseCase() = BuyUpgradeUseCase(
        getUpgradeBy = getUpgradeBy,
        decreaseResourceBy = decreaseResourceBy,
        updateUpgradeStatus = updateUpgradeStatus,
        getCurrentResource = getCurrentResource,
    )

}
