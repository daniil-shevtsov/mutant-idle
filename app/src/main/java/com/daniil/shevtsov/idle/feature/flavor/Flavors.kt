package com.daniil.shevtsov.idle.feature.flavor

import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tags

object Flavors {
    const val PREFIX = "8"

    val invisibilityAction = flavor(
        id = FlavorId.InvisibilityAction,
        placeholder = "${PREFIX}INVISIBILITY_ACTION",
        values = mapOf(
            Tags.Nature.Magic to "become ethereal",
            Tags.Nature.Tech to "activate the cloaking device",
        ),
        default = "become invisible",
    )

    val invisibilityGain = flavor(
        id = FlavorId.InvisibilityGain,
        placeholder = "${PREFIX}INVISIBILITY_GAIN",
        values = mapOf(
            Tags.Nature.Magic to "Learn dark arts of invisibility",
            Tags.Nature.Tech to "Build a cloaking device from scrap",
        )
    )

    val peopleName = flavor(
        id = FlavorId.PeopleName,
        placeholder = "${PREFIX}PEOPLE_NAME",
        values = mapOf(
            Tags.Immortal to "mortals",
            Tags.Species.Alien to "lifeforms",
        ),
        default = "people"
    )

    val derogativePeopleName = flavor(
        id = FlavorId.DerogativePeopleName,
        placeholder = "${PREFIX}DEROGATIVE_PEOPLE_NAME",
        values = mapOf(
            Tags.Immortal to "mere mortals",
            Tags.Species.Alien to "primitive lifeforms",
        ),
        default = "puny people"
    )

    val objectifiedPeopleName = flavor(
        placeholder = "$PREFIX-OBJECTIFIED-PEOPLE-NAME",
        values = mapOf(
            Tags.Species.Devourer to "food",
            Tags.Species.ShapeShifter to "clothes",
            Tags.Immortal to "mortal shells",
            Tags.Species.Parasite to "homes",
        ),
        default = "people"
    )

    val GraveyardInterpretation = flavor(
        placeholder = "$PREFIX-GRAVEYARD-INTERPRETATION",
        default = "where they hide people in the ground"
    )
}

//TODO: Do the same as with actions and upgrades
fun createFlavors() = listOf(
    Flavors.invisibilityAction,
    Flavors.invisibilityGain,
    Flavors.peopleName,
    Flavors.derogativePeopleName,
)
