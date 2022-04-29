package com.daniil.shevtsov.idle.feature.flavor

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tags
import org.junit.jupiter.api.Test

class FlavorsMachineTest {

    @Test
    fun `should return empty for empty`() {
        val withFlavor = flavorMachine(
            original = "",
            flavors = emptyList(),
        )

        assertThat(withFlavor).isEmpty()
    }

    @Test
    fun `should return plain text for plain text`() {
        val plainText = "lol"

        val withFlavor = flavorMachine(
            original = plainText,
            flavors = emptyList(),
        )

        assertThat(withFlavor).isEqualTo(plainText)
    }

    @Test
    fun `should replace placeholder with default value`() {
        val flavor = Flavors.invisibilityAction

        val withFlavor = flavorMachine(
            original = flavor.placeholder,
            flavors = listOf(flavor),
            tags = emptyList(),
        )

        assertThat(withFlavor).isEqualTo("become invisible")
    }

    @Test
    fun `should replace placeholder with magic flavor`() {
        val flavor = Flavors.invisibilityAction
        val original = "You ${flavor.placeholder}."

        val withFlavor = flavorMachine(
            original = original,
            flavors = listOf(flavor),
            tags = listOf(Tags.Nature.Magic),
        )

        assertThat(withFlavor).isEqualTo("You become ethereal.")
    }

    @Test
    fun `should replace placeholder with tech flavor`() {
        val flavor = Flavors.invisibilityAction
        val original = "You ${flavor.placeholder}."

        val withFlavor = flavorMachine(
            original = original,
            flavors = listOf(flavor),
            tags = listOf(Tags.Nature.Tech),
        )

        assertThat(withFlavor).isEqualTo("You activate the cloaking device.")
    }

    @Test
    fun `should replace placeholder with value in text`() {
        val flavor = Flavors.invisibilityAction
        val original = "You ${flavor.placeholder}."

        val withFlavor = flavorMachine(
            original = original,
            flavors = listOf(flavor),
        )

        assertThat(withFlavor).isEqualTo("You become invisible.")
    }

    @Test
    fun `should replace placeholder with alien flavor`() {
        val flavor = Flavors.derogativePeopleName
        val original = "You are beyond comprehension of ${flavor.placeholder}."

        val withFlavor = flavorMachine(
            original = original,
            flavors = listOf(flavor),
            tags = listOf(Tags.Species.Alien),
        )

        assertThat(withFlavor).isEqualTo("You are beyond comprehension of primitive lifeforms.")
    }

    @Test
    fun `should replace placeholder with immortal flavor`() {
        val flavor = Flavors.derogativePeopleName
        val original = "You are beyond comprehension of ${flavor.placeholder}."

        val withFlavor = flavorMachine(
            original = original,
            flavors  = listOf(flavor),
            tags = listOf(Tags.Immortal),
        )

        assertThat(withFlavor).isEqualTo("You are beyond comprehension of mere mortals.")
    }

    @Test
    fun `minivan `() {
        val flavor = Flavors.invisibilityAction
        val original = "You ${flavor.placeholder}."

        assertThat(
            flavorMinivan(
                original = original,
                flavor = Flavors.invisibilityAction,
                tags = listOf(Tags.Nature.Tech),
            )
        ).isEqualTo("You activate the cloaking device.")
    }


}
