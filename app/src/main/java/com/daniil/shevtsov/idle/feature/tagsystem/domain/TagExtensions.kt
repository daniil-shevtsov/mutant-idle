package com.daniil.shevtsov.idle.feature.tagsystem.domain

fun Map<Tag, TagRelation>.requiredTags(): List<Tag> =
    filter { it.value == TagRelation.RequiredAll }.keys.toList()

fun Map<Tag, TagRelation>.hasRequiredTag(tag: Tag): Boolean = requiredTags().contains(tag)

fun List<Tag>.satisfies(relation: TagRelation, tag: Tag): Boolean = satisfies(relation, listOf(tag))

fun List<Tag>.satisfies(relation: TagRelation, tags: List<Tag>): Boolean = when (relation) {
    TagRelation.Provides -> false
    TagRelation.Removes -> false
    TagRelation.RequiresNone -> tags.none { tag -> tag in this }
    TagRelation.RequiredAll -> tags.all { tag -> tag in this }
    TagRelation.RequiredAny -> tags.any { tag -> tag in this }
}

