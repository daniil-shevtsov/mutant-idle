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

val TagRelations.requireAllTags: List<Tag>
    get() = this[TagRelation.RequiredAll].orEmpty()
val TagRelations.requireAnyTags: List<Tag>
    get() = this[TagRelation.RequiredAny].orEmpty()
val TagRelations.requireNoneTags: List<Tag>
    get() = this[TagRelation.RequiresNone].orEmpty()
val TagRelations.provideTags: List<Tag>
    get() = this[TagRelation.Provides].orEmpty()
val TagRelations.removeTags: List<Tag>
    get() = this[TagRelation.Removes].orEmpty()


private fun createTagRelations(
    values: List<Pair<TagRelation, List<Tag>>>
): TagRelations = mapOf(*values.toTypedArray())
