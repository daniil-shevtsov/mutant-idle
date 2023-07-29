package com.daniil.shevtsov.idle.feature.tagsystem.domain

import assertk.Assert
import assertk.assertThat
import assertk.assertions.support.expected
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.SpikeTagKey
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.SpikeTagValue
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.tagKey
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.tagValue
import org.junit.jupiter.api.Test
import java.util.StringTokenizer
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.spikeTags as oldSpikeTags


class VerifyContainsTagsTest {

    @Test
    fun `should pass when both empty`() {
        val assertResult = verifyContainsTags(
            expected = spikeTags(),
            actual = spikeTags(),
        )
        assertThat(assertResult).isPass()
    }

    @Test
    fun `should pass when equal`() {
        val assertResult = verifyContainsTags(
            expected = spikeTags(tagKey("lol") to tagValue("cheburek")),
            actual = spikeTags(tagKey("lol") to tagValue("cheburek")),
        )
        assertThat(assertResult).isPass()
    }

    @Test
    fun `should fail when tag with key does not exist because tags are empty`() {
        val assertResult = verifyContainsTags(
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
        val assertResult = verifyContainsTags(
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
        val assertResult = verifyContainsTags(
            expected = spikeTags(tagKey("lol", entityId = "lol-entity") to tagValue("cheburek")),
            actual = spikeTags(tagKey("kek", entityId = "kek-entity") to tagValue("keburek")),
        )
        assertThat(assertResult).isFail(
            """to contain:
                |(entity=lol-entity, key=lol, value=cheburek)
                |but
                |no tag with (entity=lol-entity, key=lol)
                |actual:
                |(entity=kek-entity, key=kek, value=keburek)
                |
                |""".trimMargin()
        )
    }

    @Test
    fun `should fail when value differs`() {
        val assertResult = verifyContainsTags(
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
        val assertResult = verifyContainsTags(
            expected = spikeTags(
                tagKey("lol") to tagValue("cheburek"),
                tagKey("kek") to tagValue("keburek")
            ),
            actual = spikeTags(
                tagKey("lol") to tagValue("cheburek2"),
                tagKey("kek") to tagValue("keburek2")
            ),
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

}

fun Assert<TagAssertResult>.isPass() = given { assertResult ->
    when (assertResult) {
        is TagAssertResult.Pass -> return@given
        is TagAssertResult.Fail -> {
            expected("result to be Pass, but was Fail with message: >{${assertResult.message}}<")
        }
    }
}

fun Assert<TagAssertResult>.isFail(expectedMessage: String) = given { assertResult ->
    val failInstance = assertResult as? TagAssertResult.Fail
        ?: expected("result to be instance of TagAssertResult.Fail")
    val failMessage = failInstance.message

    if (failMessage == expectedMessage) {
        return@given
    }

    expected("message to be >{$expectedMessage}< but was >{$failMessage}< difference: ${findNotMatching(expectedMessage, failMessage)}")
}

fun spikeTags(vararg tags: Pair<SpikeTagKey, SpikeTagValue>): Map<SpikeTagKey, SpikeTagValue> =
    oldSpikeTags(
        *tags
    ).map { it.key to it.value.value }.toMap()

fun findNotMatching(sourceStr: String?, anotherStr: String?): List<String?>? {
    val at = StringTokenizer(sourceStr, " ")
    var bt: StringTokenizer? = null
    var i = 0
    var token_count = 0
    var token: String? = null
    var flag = false
    val missingWords: MutableList<String?> = ArrayList()
    while (at.hasMoreTokens()) {
        token = at.nextToken()
        bt = StringTokenizer(anotherStr, " ")
        token_count = bt.countTokens()
        while (i < token_count) {
            val s = bt.nextToken()
            if (token == s) {
                flag = true
                break
            } else {
                flag = false
            }
            i++
        }
        i = 0
        if (flag == false) missingWords.add(token)
    }
    return missingWords
}
