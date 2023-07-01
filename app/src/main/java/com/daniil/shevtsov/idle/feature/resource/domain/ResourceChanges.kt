package com.daniil.shevtsov.idle.feature.resource.domain

typealias ResourceChanges = Map<ResourceKey, Double>

data class ResourceChange(val key: ResourceKey, val amount: Double)

fun noResourceChanges(): ResourceChanges = createResourceChanges(emptyList())
fun resourceChanges(vararg values: Pair<ResourceKey, Double>): ResourceChanges =
    createResourceChanges(values.toList())

private fun createResourceChanges(values: List<Pair<ResourceKey, Double>>): ResourceChanges =
    mapOf(*values.toTypedArray())
