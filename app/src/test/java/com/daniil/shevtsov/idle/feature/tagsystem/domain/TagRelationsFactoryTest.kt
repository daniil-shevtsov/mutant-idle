package com.daniil.shevtsov.idle.feature.tagsystem.domain

import assertk.all
import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import org.junit.jupiter.api.Test

internal class TagRelationsFactoryTest {

    private val provideTags = listOf(
        tag(name = "provide1"),
        tag(name = "provide2"),
    )
    private val removeTags = listOf(
        tag(name = "remove1"),
        tag(name = "remove2"),
    )
    private val requireAllTags = listOf(
        tag(name = "requiredAll1"),
        tag(name = "requiredAll2"),
    )
    private val requireAnyTags = listOf(
        tag(name = "requiredAny1"),
        tag(name = "requiredAny2"),
    )
    private val requireNoneTags = listOf(
        tag(name = "requiredNone1"),
        tag(name = "requiredNone2"),
    )

    @Test
    fun `should create empty for no relations`() {
        val tagRelations = noTagRelations()
        assertThat(tagRelations).isEmpty()
    }

    @Test
    fun `should create one tag relation list`() {
        val tagRelations = tagRelations(TagRelation.Provides to provideTags)
        assertThat(tagRelations)
            .prop(TagRelations::provideTags)
            .isEqualTo(provideTags)
    }

    @Test
    fun `should create all relation lists`() {
        val tagRelations = tagRelations(
            TagRelation.RequiredAll to requireAllTags,
            TagRelation.RequiredAny to requireAnyTags,
            TagRelation.RequiresNone to requireNoneTags,
            TagRelation.Provides to provideTags,
            TagRelation.Removes to removeTags,
        )
        assertThat(tagRelations)
            .all {
                prop(TagRelations::requireAllTags).isEqualTo(requireAllTags)
                prop(TagRelations::requireAnyTags).isEqualTo(requireAnyTags)
                prop(TagRelations::requireNoneTags).isEqualTo(requireNoneTags)
                prop(TagRelations::provideTags).isEqualTo(provideTags)
                prop(TagRelations::removeTags).isEqualTo(removeTags)
            }
    }

}
