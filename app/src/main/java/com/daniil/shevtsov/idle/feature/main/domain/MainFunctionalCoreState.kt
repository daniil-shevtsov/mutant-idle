package com.daniil.shevtsov.idle.feature.main.domain

import com.daniil.shevtsov.idle.core.BalanceConfig
import com.daniil.shevtsov.idle.feature.action.domain.Action
import com.daniil.shevtsov.idle.feature.main.presentation.SectionKey
import com.daniil.shevtsov.idle.feature.player.job.domain.PlayerJob
import com.daniil.shevtsov.idle.feature.ratio.domain.Ratio
import com.daniil.shevtsov.idle.feature.resource.domain.Resource
import com.daniil.shevtsov.idle.feature.upgrade.domain.Upgrade

data class MainFunctionalCoreState(
    val blood: Resource,
    val balanceConfig: BalanceConfig,
    val resources: List<Resource>,
    val ratios: List<Ratio>,
    val upgrades: List<Upgrade>,
    val actions: List<Action>,
    val sectionState: Map<SectionKey, Boolean>,
    val availableJobs: List<PlayerJob>,
)
