package com.daniil.shevtsov.idle.feature.tagsystem.domain

fun Map<Tag, TagRelation>.requiredTags(): List<Tag> =
    filter { it.value == TagRelation.Required }.keys.toList()

fun Map<Tag, TagRelation>.hasRequiredTag(tag: Tag): Boolean = requiredTags().contains(tag)

