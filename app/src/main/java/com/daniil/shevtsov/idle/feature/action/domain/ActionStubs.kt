package com.daniil.shevtsov.idle.feature.action.domain

import com.daniil.shevtsov.idle.core.ui.Icons
import com.daniil.shevtsov.idle.feature.action.presentation.ActionIcon
import com.daniil.shevtsov.idle.feature.action.presentation.ActionModel
import com.daniil.shevtsov.idle.feature.action.presentation.RatioChangeModel
import com.daniil.shevtsov.idle.feature.action.presentation.ResourceChangeModel
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tag
import com.daniil.shevtsov.idle.feature.tagsystem.domain.TagRelation

fun action(
    id: Long = 0L,
    title: String = "",
    subtitle: String = "",
    resourceChanges: ResourceChanges = mapOf(),
    ratioChanges: RatioChanges = mapOf(),
    tags: Map<TagRelation, List<Tag>> = mapOf(),
) = Action(
    id = id,
    title = title,
    subtitle = subtitle,
    resourceChanges = resourceChanges,
    ratioChanges = ratioChanges,
    tags = tags
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
