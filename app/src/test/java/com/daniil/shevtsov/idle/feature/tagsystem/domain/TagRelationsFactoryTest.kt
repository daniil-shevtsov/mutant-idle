package com.daniil.shevtsov.idle.feature.tagsystem.domain

import assertk.all
import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import org.junit.jupiter.api.Test

internal class TagRelationsFactoryTest {
    private val provideTag1 = tag("provide1")
    private val provideTag2 = tag(name = "provide2")

    private val provideTags = listOf(
        provideTag1,
        provideTag2,
    )

    private val removeTag = tag(name = "remove1")
    private val removeTags = listOf(
        removeTag,
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

    @Test
    fun `should add single tag`() {
        val tagRelations = tagRelations(TagRelation.Provides to provideTag1)
        assertThat(tagRelations).prop(TagRelations::provideTags).containsExactly(provideTag1)
    }

    @Test
    fun `should add two same relation single tags`() {
        val tagRelations = tagRelations(
            TagRelation.Provides to provideTag1,
            TagRelation.Provides to provideTag2,
        )
        assertThat(tagRelations)
            .prop(TagRelations::provideTags)
            .containsExactly(provideTag1, provideTag2)
    }

    @Test
    fun `should add two different relation single tags`() {
        val tagRelations = tagRelations(
            TagRelation.Provides to provideTag1,
            TagRelation.Removes to removeTag,
        )
        assertThat(tagRelations)
            .all {
                prop(TagRelations::provideTags).containsExactly(provideTag1)
                prop(TagRelations::removeTags).containsExactly(removeTag)
            }
    }

    @Test
    fun `should add relation only once if there are duplicates`() {
        val tagRelations = tagRelations(
            TagRelation.Provides to provideTag1,
            TagRelation.Provides to provideTag1,
        )
        assertThat(tagRelations)
            .prop(TagRelations::provideTags)
            .containsExactly(provideTag1)
    }

}
