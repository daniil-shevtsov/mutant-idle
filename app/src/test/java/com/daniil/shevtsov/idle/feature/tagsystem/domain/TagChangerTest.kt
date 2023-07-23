package com.daniil.shevtsov.idle.feature.tagsystem.domain

import assertk.assertThat
import assertk.assertions.containsAll
import assertk.assertions.isEqualTo
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.SpikeTags
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.entry
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.line
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.spikeTags
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.tagKey
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.tagValue
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class TagChangerTest {
    @Test
    fun `should do nothing when no changed`() {
        val tags = spikeTags(tagKey("lol") to tagValue("cheburek"))
        val changed = tags.apply(tagChanger(changes = spikeTags()))
        assertThat(changed).isEqualTo(tags)
    }

    @Test
    fun `should add tags when tag not present`() {
        val tags = spikeTags(tagKey("lol") to tagValue("cheburek"))
        val changed =
            tags.apply(tagChanger(changes = spikeTags(tagKey("lol2") to tagValue("cheburek2"))))
        assertThat(changed)
            .strings()
            .containsAll(
                "lol" to "cheburek",
                "lol2" to "cheburek2",
            )
    }

    @Test
    fun `should replace value when tag present`() {
        val tags = spikeTags(tagKey("lol") to tagValue("cheburek"))
        val changed =
            tags.apply(tagChanger(changes = spikeTags(tagKey("lol") to tagValue("cheburek2"))))
        assertThat(changed)
            .strings()
            .containsAll("lol" to "cheburek2")
    }

    @Test
    fun `should create list when tag not present and plus with brackets`() {
        val tags = spikeTags()
        val changed =
            tags.apply(tagChanger(changes = spikeTags(tagKey("lol") to tagValue("+[cheburek]"))))
        assertThat(changed)
            .strings()
            .containsAll("lol" to "[cheburek]")
    }

    @Test
    fun `should create list when tag present and plus with brackets`() {
        val tags = spikeTags(tagKey("lol") to tagValue("cheburek"))
        val changed =
            tags.apply(tagChanger(changes = spikeTags(tagKey("lol") to tagValue("+[cheburek2]"))))
        assertThat(changed)
            .strings()
            .containsAll("lol" to "[cheburek,cheburek2]")
    }

    @Test
    fun `should add to list when tag list present and plus with brackets`() {
        val tags = spikeTags(tagKey("lol") to tagValue("[cheburek,cheburek2]"))
        val changed =
            tags.apply(tagChanger(changes = spikeTags(tagKey("lol") to tagValue("+[cheburek3]"))))
        assertThat(changed)
            .strings()
            .containsAll("lol" to "[cheburek,cheburek2,cheburek3]")
    }

    @Test
    fun `should set to number when just a number value`() {
        val tags = spikeTags()
        val changed = tags.apply(tagChanger(changes = spikeTags(tagKey("lol") to tagValue("5"))))
        assertThat(changed)
            .strings()
            .containsAll("lol" to "5")
    }

    @Test
    fun `should subtract from number when has number tag and tag change with minus and curly brackets`() {
        val tags = spikeTags(tagKey("lol") to tagValue("5"))
        val changed = tags.apply(tagChanger(changes = spikeTags(tagKey("lol") to tagValue("\${-5}"))))
        assertThat(changed)
            .strings()
            .containsAll("lol" to "0")
    }

    @Test
    fun `should create number when no tag and tag change with plus and curly brackets`() {
        val tags = spikeTags()
        val changed = tags.apply(tagChanger(changes = spikeTags(tagKey("lol") to tagValue("\${+7}"))))
        assertThat(changed)
            .strings()
            .containsAll("lol" to "7")
    }

    @Test
    fun `should add to number when has number tag and tag change with plus and curly brackets`() {
        val tags = spikeTags(tagKey("lol") to tagValue("5"))
        val changed = tags.apply(tagChanger(changes = spikeTags(tagKey("lol") to tagValue("\${+7}"))))
        assertThat(changed)
            .strings()
            .containsAll("lol" to "12")
    }

    private fun tagChanger(changes: SpikeTags): TagChanger = entry(plot = "", tagChanges = changes)
}
