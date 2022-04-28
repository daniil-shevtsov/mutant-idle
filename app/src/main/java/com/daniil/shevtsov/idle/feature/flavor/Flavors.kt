package com.daniil.shevtsov.idle.feature.flavor

import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tags

object Flavors {
    const val PREFIX = "PLACEHOLDER_PREFIX_"

    const val InvisibilityAction = "INVISIBILITY_ACTION"

    val newInvisibilityAction = flavor(
        placeholder = "${PREFIX}INVISIBILITY_ACTION",
        values = mapOf(
            Tags.Nature.Magic to "become ethereal",
            Tags.Nature.Tech to "activate the cloaking device",
        ),
        default = "become invisible",
    )

    const val DerogativePeopleName = "DEROGATIVE_PEOPLE_NAME"
}
