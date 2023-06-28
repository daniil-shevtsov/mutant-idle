package com.daniil.shevtsov.idle.feature.location.domain

import com.daniil.shevtsov.idle.feature.action.domain.makeIdsUnique
import com.daniil.shevtsov.idle.feature.flavor.Flavors
import com.daniil.shevtsov.idle.feature.tagsystem.domain.TagRelation
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tags

fun createLocations() = listOf(
    location(
        title = "Home",
        subtitle = "It's safe space where no one can bother you",
        tags = mapOf(TagRelation.RequiredAll to listOf(Tags.Access.Home)),
    ),
    location(
        title = "Graveyard",
        subtitle = "A place where they hide ${Flavors.objectifiedPeopleName.placeholder} in the ground",
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
        title = "Morgue",
        subtitle = "A place where they store ${Flavors.objectifiedPeopleName.placeholder} for a while",
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
        title = "Butcher Shop",
        subtitle = "A place where they share pieces of meat",
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
        title = "Scrap Yard",
        subtitle = "A place where they pile up metal",
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
        title = "Streets",
        subtitle = "A busy place with crowds of ${Flavors.peopleName.placeholder}",
        tags = mapOf(
            TagRelation.Provides to listOf(
                Tags.Locations.Streets
            )
        )
    ),
    location(
        title = "Dark Alley",
        subtitle = "Sometimes ${Flavors.peopleName.placeholder} go through here to save time",
        tags = mapOf(
            TagRelation.Provides to listOf(
                Tags.Locations.DarkAlley
            )
        )
    ),
    location(
        title = "Super Market",
        subtitle = "The food is everywhere",
        tags = mapOf(
            TagRelation.Provides to listOf(
                Tags.Locations.SuperMarket
            )
        )
    ),
    location(
        title = "Rooftops",
        subtitle = "They look like ants from here",
        tags = mapOf(
            TagRelation.RequiredAll to listOf(
                Tags.State.Flying,
            ),
            TagRelation.Provides to listOf(
                Tags.Locations.Rooftops
            )
        )
    ),
).makeIdsUnique()
