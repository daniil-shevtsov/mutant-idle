package com.daniil.shevtsov.idle.feature.action.domain

import com.daniil.shevtsov.idle.core.ui.Icons
import com.daniil.shevtsov.idle.feature.action.presentation.ActionIcon
import com.daniil.shevtsov.idle.feature.action.presentation.ActionModel
import com.daniil.shevtsov.idle.feature.action.presentation.RatioChangeModel
import com.daniil.shevtsov.idle.feature.action.presentation.ResourceChangeModel
import com.daniil.shevtsov.idle.feature.ratio.domain.RatioKey
import com.daniil.shevtsov.idle.feature.ratio.domain.ratioChange

fun ratioChanges(
    vararg entries: Pair<RatioKey, Double>,
): RatioChanges = ratioChangesWithTags(
    *entries.map { it.first to ratioChange(tags = emptyList(), change = it.second) }.toTypedArray()
)

fun ratioChangesWithTags(
    vararg entries: Pair<RatioKey, RatioChangeForTags>,
): RatioChanges = entries.associate { it.first to it.second }

fun action(
    id: Long = 0L,
    title: String = "",
    subtitle: String = "",
    resourceChanges: ResourceChanges = mapOf(),
    ratioChanges: RatioChanges = mapOf(),
    tags: TagRelations = mapOf(),
    plot: String? = null,
) = Action(
    id = id,
    title = title,
    subtitle = subtitle,
    resourceChanges = resourceChanges,
    ratioChanges = ratioChanges,
    tags = tags,
    plot = plot,
)

fun actionModel(
    id: Long = 0L,
    title: String = "",
    subtitle: String = "",
    icon: ActionIcon = ActionIcon(value = Icons.Human),
    resourceChanges: List<ResourceChangeModel> = emptyList(),
    ratioChanges: List<RatioChangeModel> = emptyList(),
    isEnabled: Boolean = true,
) = ActionModel(
    id = id,
    title = title,
    subtitle = subtitle,
    icon = icon,
    resourceChanges = resourceChanges,
    ratioChanges = ratioChanges,
    isEnabled = isEnabled,
)
