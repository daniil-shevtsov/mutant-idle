package com.daniil.shevtsov.idle.feature.action.domain

import com.daniil.shevtsov.idle.core.ui.Icons
import com.daniil.shevtsov.idle.feature.action.presentation.ActionIcon
import com.daniil.shevtsov.idle.feature.action.presentation.ActionModel
import com.daniil.shevtsov.idle.feature.action.presentation.RatioChangeModel
import com.daniil.shevtsov.idle.feature.action.presentation.ResourceChangeModel
import com.daniil.shevtsov.idle.feature.ratio.domain.RatioKey
import com.daniil.shevtsov.idle.feature.ratio.domain.ratioChange
import com.daniil.shevtsov.idle.feature.resource.domain.ResourceChanges
import com.daniil.shevtsov.idle.feature.resource.domain.noResourceChanges
import com.daniil.shevtsov.idle.feature.tagsystem.domain.TagRelations
import com.daniil.shevtsov.idle.feature.tagsystem.domain.noTagRelations

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
    resourceChanges: ResourceChanges = noResourceChanges(),
    ratioChanges: RatioChanges = mapOf(),
    tagRelations: TagRelations = noTagRelations(),
    plot: String? = null,
) = Action(
    id = id,
    title = title,
    subtitle = subtitle,
    resourceChanges = resourceChanges,
    ratioChanges = ratioChanges,
    tagRelations = tagRelations,
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
