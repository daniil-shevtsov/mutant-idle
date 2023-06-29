package com.daniil.shevtsov.idle.feature.flavor

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isTrue
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tags
import com.daniil.shevtsov.idle.feature.tagsystem.domain.tag
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
    fun `should handle infinite loop case`() {
        val tag = tag(name = "tag")
        val innerPlaceholder = Flavors.placeholder("INNER_FLAVOR")
        val outerPlaceholder = Flavors.placeholder("OUTER_FLAVOR")
        val originalString = "outer: $outerPlaceholder"
        val innerFlavor = flavor(
            id = FlavorId.PersonName,
            placeholder = innerPlaceholder,
            values = mapOf(
                tag to "inner: $outerPlaceholder"
            ),
            default = "inner default"
        )
        val outerFlavor = flavor(
            id = FlavorId.InvisibilityAction,
            placeholder = outerPlaceholder,
            values = mapOf(
                tag to "outer: $innerPlaceholder"
            ),
            default = "outer default: $innerPlaceholder"
        )

        val withFlavor = runCatching {
            flavorMachine(
                original = originalString,
                tags = listOf(tag),
                flavors = listOf(innerFlavor, outerFlavor),
            )
        }

        assertThat(withFlavor.isSuccess).isTrue()
        assertThat(withFlavor.getOrNull()).isNotNull()
            .isEqualTo("ERROR: infinite loop of flavors: (${FlavorId.PersonName.name}, ${FlavorId.InvisibilityAction.name})")
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
            flavors = listOf(flavor),
            tags = listOf(Tags.Immortal),
        )

        assertThat(withFlavor).isEqualTo("You are beyond comprehension of mere mortals.")
    }

    @Test
    fun `should replace simple two-level placeholder when root then child in list`() {
        val childFlavor = flavor(
            placeholder = Flavors.placeholder("CHILD-FLAVOR"),
            default = "cheburek"
        )
        val rootFlavor = flavor(
            placeholder = Flavors.placeholder("ROOT-FLAVOR"),
            default = "kek ${childFlavor.placeholder}"
        )
        val original = "lol ${rootFlavor.placeholder}"

        val flavored = flavorMachine(
            original = original,
            flavors = listOf(rootFlavor, childFlavor),
        )

        assertThat(flavored).isEqualTo("lol kek cheburek")
    }

    @Test
    fun `should replace simple two-level placeholder when child then root in list`() {
        val childFlavor = flavor(
            placeholder = Flavors.placeholder("CHILD-FLAVOR"),
            default = "cheburek"
        )
        val rootFlavor = flavor(
            placeholder = Flavors.placeholder("ROOT-FLAVOR"),
            default = "kek ${childFlavor.placeholder}"
        )
        val original = "lol ${rootFlavor.placeholder}"

        val flavored = flavorMachine(
            original = original,
            flavors = listOf(childFlavor, rootFlavor),
        )

        assertThat(flavored).isEqualTo("lol kek cheburek")
    }

    @Test
    fun `should replace complex two-level placeholder with correct values`() {
        val cheburekChildTag = tag(name = "cheburek")
        val gulashChildTag = tag(name = "gulash")

        val ayranChildTag = tag(name = "ayran")
        val colaChildTag = tag(name = "cola")

        val foodRootTag = tag("food")
        val drinkRootTag = tag("drink")

        val foodChildFlavor = flavor(
            placeholder = Flavors.placeholder("FOOD-CHILD-FLAVOR"),
            values = mapOf(
                cheburekChildTag to "cheburek",
                gulashChildTag to "gulash"
            ),
            default = "default food"
        )

        val drinkChildFlavor = flavor(
            placeholder = Flavors.placeholder("DRINK-CHILD-FLAVOR"),
            values = mapOf(
                ayranChildTag to "ayran",
                colaChildTag to "cola"
            ),
            default = "default drink"
        )

        val rootFlavor = flavor(
            placeholder = Flavors.placeholder("ROOT-FLAVOR"),
            values = mapOf(
                foodRootTag to "something to eat, ${foodChildFlavor.placeholder} maybe",
                drinkRootTag to "something to drink, ${drinkChildFlavor.placeholder} maybe",
            ),
            default = "I don't know what I want"
        )
        val original = "I want ${rootFlavor.placeholder}"

        val flavors = listOf(drinkChildFlavor, foodChildFlavor, rootFlavor)

        val flavoredWithCheburek = flavorMachine(
            original = original,
            flavors = flavors,
            tags = listOf(foodRootTag, cheburekChildTag)
        )

        val flavoredWithGulash = flavorMachine(
            original = original,
            flavors = flavors,
            tags = listOf(foodRootTag, gulashChildTag)
        )

        val flavoredWithAyran = flavorMachine(
            original = original,
            flavors = flavors,
            tags = listOf(drinkRootTag, ayranChildTag)
        )

        val flavoredWithCola = flavorMachine(
            original = original,
            flavors = flavors,
            tags = listOf(drinkRootTag, colaChildTag)
        )

        assertThat(flavoredWithCheburek)
            .isEqualTo("I want something to eat, cheburek maybe")
        assertThat(flavoredWithGulash)
            .isEqualTo("I want something to eat, gulash maybe")
        assertThat(flavoredWithAyran)
            .isEqualTo("I want something to drink, ayran maybe")
        assertThat(flavoredWithCola)
            .isEqualTo("I want something to drink, cola maybe")
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
