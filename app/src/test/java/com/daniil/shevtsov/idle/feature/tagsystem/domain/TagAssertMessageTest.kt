package com.daniil.shevtsov.idle.feature.tagsystem.domain

import assertk.Assert
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.prop
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.SpikeTagKey
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.SpikeTagValue
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.SpikeTags
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.spikeTag
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.tagKey
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.tagValue
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.spikeTags as oldSpikeTags
import org.junit.jupiter.api.Test

class TagAssertMessageTest {

    @Test
    fun `should pass when both empty`() {
        val assertResult = tagAssertMessage(
            expected = spikeTags(),
            actual = spikeTags(),
        )
        assertThat(assertResult).isPass()
    }

    @Test
    fun `should pass when equal`() {
        val assertResult = tagAssertMessage(
            expected = spikeTags(tagKey("lol") to tagValue("cheburek")),
            actual = spikeTags(tagKey("lol") to tagValue("cheburek")),
        )
        assertThat(assertResult).isPass()
    }

    @Test
    fun `should fail when tag with key does not exist because tags are empty`() {
        val assertResult = tagAssertMessage(
            expected = spikeTags(tagKey("lol") to tagValue("cheburek")),
            actual = spikeTags(),
        )
        assertThat(assertResult).isFail(
            """to contain:
                |(key=lol, value=cheburek)
                |but
                |no tag with (key=lol)
                |actual:
                |tags are empty
                |
                |""".trimMargin()
        )
    }

    @Test
    fun `should fail when tag with key does not exist`() {
        val assertResult = tagAssertMessage(
            expected = spikeTags(tagKey("lol") to tagValue("cheburek")),
            actual = spikeTags(tagKey("kek") to tagValue("keburek")),
        )
        assertThat(assertResult).isFail(
            """to contain:
                |(key=lol, value=cheburek)
                |but
                |no tag with (key=lol)
                |actual:
                |(key=kek, value=keburek)
                |
                |""".trimMargin()
        )
    }

    @Test
    fun `should fail when tag with key with entity does not exist`() {
        val assertResult = tagAssertMessage(
            expected = spikeTags(tagKey("lol", entityId="lol-entity") to tagValue("cheburek")),
            actual = spikeTags(tagKey("kek", entityId="kek-entity") to tagValue("keburek")),
        )
        assertThat(assertResult).isFail(
            """to contain:
                |(key=lol, entity=lol-entity, value=cheburek)
                |but
                |no tag with (key=lol, entity=lol-entity)
                |actual:
                |(key=kek, entity=kek-entity, value=keburek)
                |
                |""".trimMargin()
        )
    }

    @Test
    fun `should fail when value differs`() {
        val assertResult = tagAssertMessage(
            expected = spikeTags(tagKey("lol") to tagValue("cheburek")),
            actual = spikeTags(tagKey("lol") to tagValue("cheburek2")),
        )
        assertThat(assertResult).isFail(
            """to contain:
                |(key=lol, value=cheburek)
                |but
                |(key=lol) tag's value is cheburek2 instead of cheburek
                |actual:
                |(key=lol, value=cheburek2)
                |
                |""".trimMargin()
        )
    }

    @Test
    fun `should fail when two values differ`() {
        val assertResult = tagAssertMessage(
            expected = spikeTags(tagKey("lol") to tagValue("cheburek"), tagKey("kek") to tagValue("keburek")),
            actual = spikeTags(tagKey("lol") to tagValue("cheburek2"), tagKey("kek") to tagValue("keburek2")),
        )
        assertThat(assertResult).isFail(
            """to contain:
                |(key=lol, value=cheburek)
                |(key=kek, value=keburek)
                |but
                |(key=lol) tag's value is cheburek2 instead of cheburek
                |(key=kek) tag's value is keburek2 instead of keburek
                |actual:
                |(key=lol, value=cheburek2)
                |(key=kek, value=keburek2)
                |
                |""".trimMargin()
        )
    }

    private fun spikeTags(vararg tags: Pair<SpikeTagKey, SpikeTagValue>): Map<SpikeTagKey, SpikeTagValue> =
        oldSpikeTags(
            *tags
        ).map { it.key to it.value.value }.toMap()

    private fun Assert<TagAssertResult>.isPass() = isInstanceOf(TagAssertResult.Pass::class)
    private fun Assert<TagAssertResult>.isFail(expectedMessage: String) =
        isInstanceOf(TagAssertResult.Fail::class)
            .prop(TagAssertResult.Fail::message)
            .isEqualTo(expectedMessage)

}
