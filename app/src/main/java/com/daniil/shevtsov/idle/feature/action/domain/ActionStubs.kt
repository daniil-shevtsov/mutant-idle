package com.daniil.shevtsov.idle.feature.action.domain

import com.daniil.shevtsov.idle.feature.action.presentation.ActionIcon
import com.daniil.shevtsov.idle.feature.action.presentation.ActionModel
import com.daniil.shevtsov.idle.feature.ratio.domain.RatioKey
import com.daniil.shevtsov.idle.feature.resource.domain.ResourceKey
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tag
import com.daniil.shevtsov.idle.feature.tagsystem.domain.TagRelation

fun action(
    id: Long = 0L,
    title: String = "",
    subtitle: String = "",
    resourceChanges: Map<ResourceKey, Double> = mapOf(),
    ratioChanges: Map<RatioKey, Float> = mapOf(),
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
    icon: ActionIcon = ActionIcon.Human,
    isEnabled: Boolean = true,
) = ActionModel(
    id = id,
    title = title,
    subtitle = subtitle,
    icon = icon,
    isEnabled = isEnabled,
)
