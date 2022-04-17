package com.daniil.shevtsov.idle.feature.tagsystem.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class TagSystemTest {
    private val tagSystem = TagSystem()

    @Test
    fun `job should have tags`() {
        val tags = listOf(
            tag(name = "lol"),
            tag(name = "kek"),
            tag(name = "cheburek"),
        )
        val job = playerJob(
            tags = tags,
        )

        val jobTags = job.tags

        assertThat(jobTags).isEqualTo(tags)
    }

}