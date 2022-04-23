package com.daniil.shevtsov.idle.feature.main.domain

import com.daniil.shevtsov.idle.core.BalanceConfig
import com.daniil.shevtsov.idle.core.domain.balanceConfig
import com.daniil.shevtsov.idle.feature.action.domain.Action
import com.daniil.shevtsov.idle.feature.main.presentation.SectionKey
import com.daniil.shevtsov.idle.feature.player.job.domain.PlayerJob
import com.daniil.shevtsov.idle.feature.ratio.domain.Ratio
import com.daniil.shevtsov.idle.feature.resource.domain.Resource
import com.daniil.shevtsov.idle.feature.resource.domain.ResourceKey
import com.daniil.shevtsov.idle.feature.resource.domain.resource
import com.daniil.shevtsov.idle.feature.upgrade.domain.Upgrade

fun mainFunctionalCoreState(
    blood: Resource = resource(),
    balanceConfig: BalanceConfig = balanceConfig(),
    resources: List<Resource> = emptyList(),
    ratios: List<Ratio> = emptyList(),
    upgrades: List<Upgrade> = emptyList(),
    actions: List<Action> = emptyList(),
    sectionState: Map<SectionKey, Boolean> = mapOf(),
    availableJobs: List<PlayerJob> = emptyList(),
) = MainFunctionalCoreState(
    blood = blood,
    balanceConfig = balanceConfig,
    resources = resources,
    ratios = ratios,
    upgrades = upgrades,
    actions = actions,
    sectionState = sectionState,
    availableJobs = availableJobs
)
