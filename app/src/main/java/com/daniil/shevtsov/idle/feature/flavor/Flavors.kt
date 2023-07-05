package com.daniil.shevtsov.idle.feature.flavor

import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tag
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tags

//TODO: Do the same as with actions and upgrades
fun createFlavors() = listOf(
    Flavors.appearanceChange,
    Flavors.appearanceChangeAction,
    Flavors.invisibilityAction,
    Flavors.invisibilityGain,
    Flavors.personName,
    Flavors.peopleName,
    Flavors.derogativePeopleName,
    Flavors.objectifiedPeopleName,
)

//TODO: If I forget to add new flavor into flavors list in state there is an infinite loop.
object Flavors {
    val invisibilityAction = flavor(
        id = FlavorId.InvisibilityAction,
        placeholder = placeholder("INVISIBILITY_ACTION"),
        values = mapOf(
            Tags.Nature.Magic to "become ethereal",
            Tags.Nature.Tech to "activate the cloaking device",
        ),
        default = "become invisible",
    )

    val invisibilityGain = flavor(
        id = FlavorId.InvisibilityGain,
        placeholder = placeholder("INVISIBILITY_GAIN"),
        values = mapOf(
            Tags.Nature.Magic to "Learn dark arts of invisibility",
            Tags.Nature.Tech to "Build a cloaking device from scrap",
        )
    )

    val personName = flavor(
        id = FlavorId.PersonName,
        placeholder = placeholder("PERSON_NAME"),
        values = mapOf(
            Tags.Immortal to "mortal",
            Tags.Species.Alien to "lifeform",
        ),
        default = "person"
    )

    val peopleName = flavor(
        id = FlavorId.PeopleName,
        placeholder = placeholder("PEOPLE_NAME"),
        values = mapOf(
            Tags.Immortal to "mortals",
            Tags.Species.Alien to "lifeforms",
            Tags.Species.Android to "meatbags",
        ),
        default = "people"
    )

    val derogativePeopleName = flavor(
        id = FlavorId.DerogativePeopleName,
        placeholder = placeholder("DEROGATIVE_PEOPLE_NAME"),
        values = mapOf(
            Tags.Immortal to "mere mortals",
            Tags.Species.Alien to "primitive lifeforms",
        ),
        default = "puny ${peopleName.placeholder}"
    )

    val objectifiedPeopleName = flavor(
        placeholder = placeholder("OBJECTIFIED-PEOPLE-NAME"),
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

    val appearanceChangeAction = flavor(
        placeholder = placeholder("APPEARANCE-CHANGE-ACTION"),
        values = mapOf(
            Tags.Species.Alien to "Change your skin to another ${Flavors.personName.placeholder}",
            Tags.Species.ShapeShifter to "Shapeshift into another ${Flavors.personName.placeholder}",
        ),
        default = "Change your appearance"
    )
    val appearanceChange = flavor(
        placeholder = placeholder("APPEARANCE-CHANGE"),
        values = mapOf(
            Tags.Species.Alien to "${Flavors.personName.placeholder}'s skin",
            Tags.Species.ShapeShifter to "${objectifiedPeopleName.placeholder} change",
        ),
        default = "Appearance Change"
    )

    val GraveyardInterpretation = flavor(
        placeholder = placeholder("GRAVEYARD-INTERPRETATION"),
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
    var unknownPlaceholder: String? = null

    while (newOriginal.containsFlavorPlaceholder() && !isStuck) {
        val placeholders =
            newOriginal.splitByTokens(PREFIX[0], POSTFIX[0]).filter { it.contains(PREFIX[0]) }
        unknownPlaceholder =
            placeholders.find { placeholder -> flavors.none { flavor -> flavor.placeholder == placeholder } }
        if (unknownPlaceholder != null) {
            break
        }
        placeholders.forEach { placeholder ->
            val flavor = flavors.find { it.placeholder == placeholder }!!
            if (newOriginal.contains(placeholder) && flavor in replacedFlavors) {
                println("stuck")
                isStuck = true
            }
            if (newOriginal.contains(placeholder)) {
                replacedFlavors += flavor
            }
            newOriginal = flavorMinivan(original = newOriginal, flavor = flavor, tags = tags)
        }
    }

    return when {
        isStuck -> "INFINITE LOOP"
        unknownPlaceholder != null -> "UNKNOWN_PLACEHOLDER=$unknownPlaceholder"
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
