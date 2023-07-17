package com.daniil.shevtsov.idle.feature.tagsystem.domain

fun tagKey(key: String, entityId: String? = null) = SpikeTagKey(
    tagKey = key,
    entityId = entityId,
)
