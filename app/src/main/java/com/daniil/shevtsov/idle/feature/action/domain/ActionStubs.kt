package com.daniil.shevtsov.idle.feature.action.domain

import com.daniil.shevtsov.idle.feature.action.presentation.ActionIcon
import com.daniil.shevtsov.idle.feature.action.presentation.ActionModel

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
