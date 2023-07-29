package com.daniil.shevtsov.idle.feature.tagsystem.domain

import assertk.assertThat
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.tagKey
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.tagValue
import org.junit.jupiter.api.Test

class VerifyContainsNoTagsTest {
    @Test
    fun `should pass when both empty`() {
        val assertResult = verifyContainsNoTags(
            expected = spikeTags(),
            actual = spikeTags(),
        )
        assertThat(assertResult).isPass()
    }

    @Test
    fun `should pass when present tags empty`() {
        val assertResult = verifyContainsNoTags(
            expected = spikeTags(tagKey("lol") to tagValue("cheburek")),
            actual = spikeTags(),
        )
        assertThat(assertResult).isPass()
    }

    @Test
    fun `should pass when expected tags empty`() {
        val assertResult = verifyContainsNoTags(
            expected = spikeTags(),
            actual = spikeTags(tagKey("lol") to tagValue("cheburek")),
        )
        assertThat(assertResult).isPass()
    }

    @Test
    fun `should pass when actual tag do not contain expected`() {
        val assertResult = verifyContainsNoTags(
            expected = spikeTags(tagKey("kek") to tagValue("cheburek2")),
            actual = spikeTags(tagKey("lol") to tagValue("cheburek")),
        )
        assertThat(assertResult).isPass()
    }

    @Test
    fun `should pass when actual tags do not contain expected`() {
        val assertResult = verifyContainsNoTags(
            expected = spikeTags(tagKey("kek") to tagValue("cheburek2")),
            actual = spikeTags(
                tagKey("lol") to tagValue("cheburek"),
                tagKey("kek2") to tagValue("cheburek2"),
            ),
        )
        assertThat(assertResult).isPass()
    }

    @Test
    fun `should fail when present tags equal to expected`() {
        val assertResult = verifyContainsNoTags(
            expected = spikeTags(tagKey("lol") to tagValue("cheburek")),
            actual = spikeTags(tagKey("lol") to tagValue("cheburek")),
        )
        assertThat(assertResult).isFail(
            """to not contain:
                |(key=lol, value=cheburek)
                |but present:
                |(key=lol, value=cheburek)
                |actual:
                |(key=lol, value=cheburek)
                |
                |""".trimMargin()
        )
    }

    @Test
    fun `should fail when present tags contain expected`() {
        val assertResult = verifyContainsNoTags(
            expected = spikeTags(
                tagKey("lol") to tagValue("cheburek")
            ),
            actual = spikeTags(
                tagKey("lol") to tagValue("cheburek"),
                tagKey("kek") to tagValue("cheburek2"),
            ),
        )
        assertThat(assertResult).isFail(
            """to not contain:
                |(key=lol, value=cheburek)
                |but present:
                |(key=lol, value=cheburek)
                |actual:
                |(key=lol, value=cheburek)
                |(key=kek, value=cheburek2)
                |
                |""".trimMargin()
        )
    }

    @Test
    fun `should fail when present tags contain one of the expected`() {
        val assertResult = verifyContainsNoTags(
            expected = spikeTags(
                tagKey("lol") to tagValue("cheburek"),
                tagKey("kek") to tagValue("cheburek2"),
            ),
            actual = spikeTags(
                tagKey("lol") to tagValue("cheburek"),
            ),
        )
        assertThat(assertResult).isFail(
            """to not contain:
                |(key=lol, value=cheburek)
                |(key=kek, value=cheburek2)
                |but present:
                |(key=lol, value=cheburek)
                |actual:
                |(key=lol, value=cheburek)
                |
                |""".trimMargin()
        )
    }

    @Test
    fun `should fail when present tags equal to two of the expected`() {
        val assertResult = verifyContainsNoTags(
            expected = spikeTags(
                tagKey("lol") to tagValue("cheburek"),
                tagKey("kek") to tagValue("cheburek2"),
            ),
            actual = spikeTags(
                tagKey("lol") to tagValue("cheburek"),
                tagKey("kek") to tagValue("cheburek2"),
            ),
        )
        assertThat(assertResult).isFail(
            """to not contain:
                |(key=lol, value=cheburek)
                |(key=kek, value=cheburek2)
                |but present:
                |(key=lol, value=cheburek)
                |(key=kek, value=cheburek2)
                |actual:
                |(key=lol, value=cheburek)
                |(key=kek, value=cheburek2)
                |
                |""".trimMargin()
        )
    }

    @Test
    fun `should fail when present tags contain two of the expected`() {
        val assertResult = verifyContainsNoTags(
            expected = spikeTags(
                tagKey("lol") to tagValue("cheburek"),
                tagKey("kek") to tagValue("cheburek2"),
            ),
            actual = spikeTags(
                tagKey("lol") to tagValue("cheburek"),
                tagKey("kek") to tagValue("cheburek2"),
                tagKey("kek2") to tagValue("cheburek3"),
            ),
        )
        assertThat(assertResult).isFail(
            """to not contain:
                |(key=lol, value=cheburek)
                |(key=kek, value=cheburek2)
                |but present:
                |(key=lol, value=cheburek)
                |(key=kek, value=cheburek2)
                |actual:
                |(key=lol, value=cheburek)
                |(key=kek, value=cheburek2)
                |(key=kek2, value=cheburek3)
                |
                |""".trimMargin()
        )
    }
}
