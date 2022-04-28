package com.daniil.shevtsov.idle.feature.flavor

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tags
import org.junit.jupiter.api.Test

class FlavorMachineTest {

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
        val placeholder = Flavor.InvisibilityAction

        val withFlavor = flavorMachine(original = placeholder)

        assertThat(withFlavor).isEqualTo("become invisible")
    }

    @Test
    fun `should replace placeholder with magic flavor`() {
        val placeholder = "You ${Flavor.InvisibilityAction}."

        val withFlavor = flavorMachine(
            original = placeholder,
            tags = listOf(Tags.Nature.Magic),
        )

        assertThat(withFlavor).isEqualTo("You become ethereal.")
    }

    @Test
    fun `should replace placeholder with value in text`() {
        val placeholder = "You ${Flavor.InvisibilityAction}."

        val withFlavor = flavorMachine(original = placeholder)

        assertThat(withFlavor).isEqualTo("You become invisible.")
    }

}
