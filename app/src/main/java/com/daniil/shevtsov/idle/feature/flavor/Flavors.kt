package com.daniil.shevtsov.idle.feature.flavor

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
