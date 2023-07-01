package com.daniil.shevtsov.idle.feature.tagsystem.domain

typealias TagRelations = Map<TagRelation, List<Tag>>

fun tagRelations(
    vararg values: Pair<TagRelation, List<Tag>>
): TagRelations = mapOf(*values)

fun noTagRelations(): TagRelations = tagRelations()
