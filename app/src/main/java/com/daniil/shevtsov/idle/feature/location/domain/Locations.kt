package com.daniil.shevtsov.idle.feature.location.domain

import com.daniil.shevtsov.idle.feature.flavor.Flavors
import com.daniil.shevtsov.idle.feature.tagsystem.domain.TagRelation
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tags

fun createLocations() = listOf(
    location(
        id = 0L,
        title = "Home",
        description = "It's safe space where no one can bother you",
        tags = mapOf(),
    ),
    location(
        id = 1L,
        title = "Graveyard",
        description = "A place where they hide ${Flavors.objectifiedPeopleName.placeholder} in the ground",
        tags = mapOf(
            TagRelation.RequiredAll to listOf(
                Tags.Access.Graveyard
            ),
            TagRelation.Provides to listOf(
                Tags.Locations.Graveyard,
            )
        )
    ),
    location(
        id = 2L,
        title = "Morgue",
        description = "A place where they store ${Flavors.objectifiedPeopleName.placeholder} for a while",
        tags = mapOf(
            TagRelation.RequiredAll to listOf(
                Tags.Access.Morgue
            ),
            TagRelation.Provides to listOf(
                Tags.Locations.Morgue,
                Tags.Access.Incinerator,
                Tags.Access.FreshCorpses,
            )
        )
    ),
    location(
        id = 3L,
        title = "Butcher Shop",
        description = "A place where they share pieces of meat",
        tags = mapOf(
            TagRelation.RequiredAll to listOf(
                Tags.Access.ButcherShop,
            ),
            TagRelation.Provides to listOf(
                Tags.Locations.ButcherShop,
                Tags.Access.Meat,
            )
        )
    ),
    location(
        id = 4L,
        title = "Scrap Yard",
        description = "A place where they pile up metal",
        tags = mapOf(
            TagRelation.RequiredAll to listOf(
                Tags.Access.Scrapyard,
            ),
            TagRelation.Provides to listOf(
                Tags.Locations.Scrapyard,
            )
        )
    ),
    location(
        id = 5L,
        title = "Streets",
        description = "A busy place with crowds of people",
        tags = mapOf(
            TagRelation.Provides to listOf(
                Tags.Locations.Streets
            )
        )
    ),
    location(
        id = 6L,
        title = "Dark Alley",
        description = "Sometimes people go through here to save time",
        tags = mapOf(
            TagRelation.Provides to listOf(
                Tags.Locations.DarkAlley
            )
        )
    ),
    location(
        id = 7L,
        title = "Super Market",
        description = "The food everywhere",
        tags = mapOf(
            TagRelation.Provides to listOf(
                Tags.Locations.SuperMarket
            )
        )
    )
)
