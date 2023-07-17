package com.daniil.shevtsov.idle.feature.tagsystem.domain

data class SpikeTag(
    val key: SpikeTagKey,
    val value: SpikeTagValue,
)

fun spikeTag(
    key: SpikeTagKey = tagKey(key = ""),
    value: SpikeTagValue = "",
) = SpikeTag(
    key = key,
    value = value,
)
