package com.daniil.shevtsov.idle.feature.flavor

import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tag
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tags

object Flavors {
    val invisibilityAction = flavor(
        id = FlavorId.InvisibilityAction,
        placeholder = Flavors.placeholder("INVISIBILITY_ACTION"),
        values = mapOf(
            Tags.Nature.Magic to "become ethereal",
            Tags.Nature.Tech to "activate the cloaking device",
        ),
        default = "become invisible",
    )

    val invisibilityGain = flavor(
        id = FlavorId.InvisibilityGain,
        placeholder = Flavors.placeholder("INVISIBILITY_GAIN"),
        values = mapOf(
            Tags.Nature.Magic to "Learn dark arts of invisibility",
            Tags.Nature.Tech to "Build a cloaking device from scrap",
        )
    )

    val personName = flavor(
        id = FlavorId.PersonName,
        placeholder = Flavors.placeholder("PERSON_NAME"),
        values = mapOf(
            Tags.Immortal to "mortal",
            Tags.Species.Alien to "lifeform",
        ),
        default = "person"
    )

    val peopleName = flavor(
        id = FlavorId.PeopleName,
        placeholder = Flavors.placeholder("PEOPLE_NAME"),
        values = mapOf(
            Tags.Immortal to "mortals",
            Tags.Species.Alien to "lifeforms",
        ),
        default = "people"
    )

    val derogativePeopleName = flavor(
        id = FlavorId.DerogativePeopleName,
        placeholder = Flavors.placeholder("DEROGATIVE_PEOPLE_NAME"),
        values = mapOf(
            Tags.Immortal to "mere mortals",
            Tags.Species.Alien to "primitive lifeforms",
        ),
        default = "puny ${peopleName.placeholder}"
    )

    val objectifiedPeopleName = flavor(
        placeholder = Flavors.placeholder("OBJECTIFIED-PEOPLE-NAME"),
        values = mapOf(
            Tags.Species.Devourer to "food",
            Tags.Species.ShapeShifter to "clothes",
            Tags.Species.Demon to "shells",
            Tags.Species.Parasite to "homes",
            Tags.Species.Alien to "lifeforms",
            Tags.Immortal to "mortals",
        ),
        default = peopleName.placeholder
    )

    val GraveyardInterpretation = flavor(
        placeholder = Flavors.placeholder("GRAVEYARD-INTERPRETATION"),
        default = "where they hide people in the ground"
    )

    fun placeholder(key: String) = "$PREFIX$key$POSTFIX"
}

fun String.splitByTokens(prefix: Char, postfix: Char): List<String> {
    val splitTokens = mutableListOf<String>()
    var textToSplit = this

    if (!textToSplit.contains(prefix)) {
        splitTokens.add(textToSplit)
    }

    while (textToSplit.contains(prefix)) {
        val prefixIndex = textToSplit.indexOf(prefix)
        val postfixIndex = textToSplit.indexOf(postfix)

        val textBeforeToken = textToSplit.substring(0, prefixIndex)
        val token = textToSplit.substring(prefixIndex, postfixIndex + 1)
        val textAfterToken = textToSplit.substring(postfixIndex + 1, textToSplit.length)

        splitTokens += listOf(textBeforeToken, token)
        if (!textAfterToken.contains(prefix)) {
            splitTokens += textAfterToken
        }
        textToSplit = textAfterToken
    }

    return splitTokens.filter { it.isNotEmpty() }
}

fun flavorMachine(
    original: String,
    flavors: List<Flavor>,
    tags: List<Tag> = emptyList(),
): String {
    var newOriginal = original
    val replacedFlavors = mutableSetOf<Flavor>()
    var isStuck = false

    while (!isStuck && newOriginal.containsFlavorPlaceholder()) {
        flavors.forEach { flavor ->

            if (newOriginal.contains(flavor.placeholder) && flavor in replacedFlavors) {
                println("stuck")
                isStuck = true
            }
            if(newOriginal.contains(flavor.placeholder)) {
                replacedFlavors += flavor
            }
            newOriginal = flavorMinivan(original = newOriginal, flavor = flavor, tags = tags)
        }
    }

    return when {
        isStuck -> "INFINITE LOOP"
        else -> newOriginal
    }
}

fun flavorMinivan(
    original: String,
    flavor: Flavor,
    tags: List<Tag>,
): String {
    val placeholder = flavor.placeholder
    val replacement =
        tags.firstOrNull { tag -> flavor.values.containsKey(tag) }?.let { flavor.values[it] }
            ?: flavor.default
    return original.replace(placeholder, replacement)
}

private const val PREFIX = "8"
private const val POSTFIX = "9"

fun String.containsFlavorPlaceholder() = contains(PREFIX) && contains(POSTFIX)

//TODO: Do the same as with actions and upgrades
fun createFlavors() = listOf(
    Flavors.invisibilityAction,
    Flavors.invisibilityGain,
    Flavors.peopleName,
    Flavors.derogativePeopleName,
    Flavors.objectifiedPeopleName,
)
