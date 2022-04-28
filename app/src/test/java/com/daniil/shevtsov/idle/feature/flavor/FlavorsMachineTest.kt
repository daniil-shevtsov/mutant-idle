package com.daniil.shevtsov.idle.feature.flavor

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tags
import org.junit.jupiter.api.Test

class FlavorsMachineTest {

    @Test
    fun `should return empty for empty`() {
        val withFlavor = flavorMachine(original = "")

        assertThat(withFlavor).isEmpty()
    }

    @Test
    fun `should return plain text for plain text`() {
        val plainText = "lol"

        val withFlavor = flavorMachine(original = plainText)

        assertThat(withFlavor).isEqualTo(plainText)
    }

    @Test
    fun `should replace placeholder with default value`() {
        val placeholder = Flavors.newInvisibilityAction.placeholder

        val withFlavor = flavorMachine(original = placeholder)

        assertThat(withFlavor).isEqualTo("become invisible")
    }

    @Test
    fun `should replace placeholder with magic flavor`() {
        val placeholder = "You ${Flavors.newInvisibilityAction.placeholder}."

        val withFlavor = flavorMachine(
            original = placeholder,
            tags = listOf(Tags.Nature.Magic),
        )

        assertThat(withFlavor).isEqualTo("You become ethereal.")
    }

    @Test
    fun `should replace placeholder with tech flavor`() {
        val placeholder = "You ${Flavors.newInvisibilityAction.placeholder}."

        val withFlavor = flavorMachine(
            original = placeholder,
            tags = listOf(Tags.Nature.Tech),
        )

        assertThat(withFlavor).isEqualTo("You activate the cloaking device.")
    }

    @Test
    fun `should replace placeholder with value in text`() {
        val placeholder = "You ${Flavors.newInvisibilityAction.placeholder}."

        val withFlavor = flavorMachine(original = placeholder)

        assertThat(withFlavor).isEqualTo("You become invisible.")
    }

    @Test
    fun `should replace placeholder with alien flavor`() {
        val placeholder = "You are beyond comprehension of ${Flavors.DerogativePeopleName}."

        val withFlavor = flavorMachine(
            original = placeholder,
            tags = listOf(Tags.Species.Alien),
        )

        assertThat(withFlavor).isEqualTo("You are beyond comprehension of primitive life forms.")
    }

    @Test
    fun `should replace placeholder with immortal flavor`() {
        val placeholder = "You are beyond comprehension of ${Flavors.DerogativePeopleName}."

        val withFlavor = flavorMachine(
            original = placeholder,
            tags = listOf(Tags.Immortal),
        )

        assertThat(withFlavor).isEqualTo("You are beyond comprehension of mere mortals.")
    }

    @Test
    fun `minivan `() {
        val original = "You ${Flavors.newInvisibilityAction.placeholder}."

        assertThat(
            flavorMinivan(
                original = original,
                flavor = Flavors.newInvisibilityAction,
                tags = listOf(Tags.Nature.Tech),
            )
        ).isEqualTo("You activate the cloaking device.")
    }


}
