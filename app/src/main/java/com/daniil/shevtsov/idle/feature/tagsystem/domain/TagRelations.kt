package com.daniil.shevtsov.idle.feature.tagsystem.domain

typealias TagRelations = Map<TagRelation, List<Tag>>

@JvmName("tagRelationsWithList")
fun tagRelations(
    vararg values: Pair<TagRelation, List<Tag>>
): TagRelations = createTagRelations(values.toList())

fun tagRelations(
    vararg values: Pair<TagRelation, Tag>
): TagRelations = createTagRelations(
    values
        .map { (tagRelation, tag) -> tagRelation to listOf(tag) }
)

fun noTagRelations(): TagRelations = createTagRelations(emptyList())

private fun createTagRelations(
    values: List<Pair<TagRelation, List<Tag>>>
): TagRelations = mapOf(*values.toTypedArray())
