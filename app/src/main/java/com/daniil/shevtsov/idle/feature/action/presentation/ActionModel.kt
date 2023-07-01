package com.daniil.shevtsov.idle.feature.action.presentation

import com.daniil.shevtsov.idle.feature.upgrade.presentation.FlavoredModel
import com.daniil.shevtsov.idle.feature.upgrade.presentation.SelectableModel

data class ActionModel(
    override val id: Long,
    override val title: String,
    override val subtitle: String,
    val icon: ActionIcon,
    val resourceChanges: List<ResourceChangeModel>,
    val ratioChanges: List<RatioChangeModel>,
    val isEnabled: Boolean = true,
) : FlavoredModel, SelectableModel
