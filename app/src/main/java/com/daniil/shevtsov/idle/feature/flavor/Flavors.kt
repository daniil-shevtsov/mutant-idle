package com.daniil.shevtsov.idle.feature.flavor

import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tags

object Flavors {
    const val PREFIX = "8"

    val invisibilityAction = flavor(
        placeholder = "${PREFIX}INVISIBILITY_ACTION",
        values = mapOf(
            Tags.Nature.Magic to "become ethereal",
            Tags.Nature.Tech to "activate the cloaking device",
        ),
        default = "become invisible",
    )

    val invisibilityGain = flavor(
        placeholder = "${PREFIX}INVISIBILITY_GAIN",
        values = mapOf(
            Tags.Nature.Magic to "learn dark arts of invisibility",
            Tags.Nature.Tech to "build a cloaking device from scrap",
        )
    )

    val peopleName = flavor(
        placeholder = "${PREFIX}PEOPLE_NAME",
        values = mapOf(
            Tags.Immortal to "mortals",
            Tags.Species.Alien to "lifeforms",
        ),
        default = "people"
    )

    val derogativePeopleName = flavor(
        placeholder = "${PREFIX}DEROGATIVE_PEOPLE_NAME",
        values = mapOf(
            Tags.Immortal to "mere mortals",
            Tags.Species.Alien to "primitive lifeforms",
        ),
        default = "puny people"
    )

    //TODO: Do the same as with actions and upgrades
    val flavors = listOf(
        invisibilityAction,
        invisibilityGain,
        peopleName,
        derogativePeopleName,
    )//.map { flavor -> flavor.copy(placeholder = PREFIX + flavor.placeholder) }
}
