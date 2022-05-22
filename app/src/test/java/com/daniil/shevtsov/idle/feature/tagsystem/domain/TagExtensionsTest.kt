package com.daniil.shevtsov.idle.feature.tagsystem.domain

import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import org.junit.jupiter.api.Test

internal class TagExtensionsTest {

    private val tags = listOf(tag(name = "lol"), tag(name = "kek"), tag(name = "cheburek"))

    @Test
    fun `should work for require any`() {
        assertThat(tags.satisfies(TagRelation.RequiredAny, tag("kek")))
            .isTrue()
        assertThat(tags.satisfies(TagRelation.RequiredAny, listOf(tag("kek"), tag("goulash"))))
            .isTrue()
        assertThat(tags.satisfies(TagRelation.RequiredAny, tag("goulash")))
            .isFalse()
    }

}
