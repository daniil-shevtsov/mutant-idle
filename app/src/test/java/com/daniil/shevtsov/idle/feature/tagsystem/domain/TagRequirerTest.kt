package com.daniil.shevtsov.idle.feature.tagsystem.domain

import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.entry
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.line
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.spikeTags
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.tagKey
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.tagValue
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class TagRequirerTest {

    @Test
    fun `!true means not true (not present or false)`() {
        val tagLine =
            line(requiredTags = spikeTags(tagKey("lol") to tagValue("!true")), entry = entry(""))

        assertThat(tagLine.suitableFor(spikeTags(tagKey("lol") to tagValue("false")))).isTrue()
        assertThat(tagLine.suitableFor(spikeTags(tagKey("lol") to tagValue("null")))).isTrue()
        assertThat(tagLine.suitableFor(spikeTags(tagKey("kek") to tagValue("true")))).isTrue()
        assertThat(tagLine.suitableFor(spikeTags())).isTrue()
        assertThat(tagLine.suitableFor(spikeTags(tagKey("lol") to tagValue("true")))).isFalse()
    }

    @Test
    fun `brackets mean that should be contained in the list`() {
        val tagLine =
            line(
                requiredTags = spikeTags(tagKey("objects") to tagValue("knife")),
                entry = entry("")
            )

        assertThat(tagLine.suitableFor(spikeTags(tagKey("objects") to tagValue("[]")))).isFalse()
        assertThat(tagLine.suitableFor(spikeTags(tagKey("objects") to tagValue("knife")))).isTrue()
        assertThat(tagLine.suitableFor(spikeTags(tagKey("objects") to tagValue("[knife,spear]")))).isTrue()
        assertThat(tagLine.suitableFor(spikeTags())).isFalse()
        assertThat(tagLine.suitableFor(spikeTags(tagKey("objects") to tagValue("[knife, spear]")))).isTrue()
    }

    @Test
    fun `should mark values with subtraction greater than current tag value as not suitable`() {
        val currentTags = spikeTags(tagKey("lol") to tagValue("5"))
        val equal = line(
            requiredTags = spikeTags(),
            entry = entry("", tagChanges = spikeTags(tagKey("lol") to tagValue("\${-5}")))
        )
        val less = line(
            requiredTags = spikeTags(),
            entry = entry("", tagChanges = spikeTags(tagKey("lol") to tagValue("\${-1}")))
        )
        val addition = line(
            requiredTags = spikeTags(),
            entry = entry("", tagChanges = spikeTags(tagKey("lol") to tagValue("\${+10}")))
        )
        val more = line(
            requiredTags = spikeTags(),
            entry = entry("", tagChanges = spikeTags(tagKey("lol") to tagValue("\${-10}")))
        )

        assertThat(equal.suitableFor(currentTags)).isTrue()
        assertThat(less.suitableFor(currentTags)).isTrue()
        assertThat(addition.suitableFor(currentTags)).isTrue()
        assertThat(more.suitableFor(currentTags)).isFalse()
    }

}
