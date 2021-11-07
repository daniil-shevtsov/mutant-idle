package com.daniil.shevtsov.idle.main.domain.upgrade

import com.daniil.shevtsov.idle.util.upgrade
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test

internal class BuyUpgradeUseCaseTest {

    private val getUpgradeBy: GetUpgradeByUseCase = mockk()
    private val decreaseResourceBy: DecreaseResourceByUseCase = mockk()
    private val updateUpgradeStatus: UpdateUpgradeStatusUseCase = mockk()

    private val buyUpgrade: BuyUpgradeUseCase by lazy {
        createUseCase()
    }

    @Test
    fun `should buy upgrade if affordable`() = runBlockingTest {
        val upgradeId = 1L
        val upgradePrice = 30.0

        coEvery { getUpgradeBy(upgradeId) } returns upgrade(id = upgradeId, price = upgradePrice)

        buyUpgrade(id = upgradeId)

        coVerify { decreaseResourceBy(upgradePrice) }
        coVerify { updateUpgradeStatus(upgradeId, UpgradeStatus.Bought) }
    }

    private fun createUseCase() = BuyUpgradeUseCase(
        getUpgradeBy = getUpgradeBy,
        decreaseResourceBy = decreaseResourceBy,
        updateUpgradeStatus = updateUpgradeStatus,
    )

}
