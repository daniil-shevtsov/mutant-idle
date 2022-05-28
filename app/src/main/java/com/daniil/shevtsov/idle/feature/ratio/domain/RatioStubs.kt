package com.daniil.shevtsov.idle.feature.ratio.domain

import com.daniil.shevtsov.idle.feature.action.domain.RatioChangeForTags
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tag

fun ratio(
    key: RatioKey = RatioKey.Mutanity,
    title: String = "",
    value: Double = 0.0,
) = Ratio(
    key = key,
    title = title,
    value = value,
)

fun ratioChange(
    tags: List<Tag> = emptyList(),
    change: Double = 0.0,
): RatioChangeForTags = mapOf(tags to change)
