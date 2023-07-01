package com.daniil.shevtsov.idle.feature.location.domain

import com.daniil.shevtsov.idle.feature.action.domain.makeIdsUnique
import com.daniil.shevtsov.idle.feature.flavor.Flavors
import com.daniil.shevtsov.idle.feature.tagsystem.domain.TagRelation
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tags
import com.daniil.shevtsov.idle.feature.tagsystem.domain.tagRelations

fun createLocations() = listOf(
    location(
        title = "Home",
        subtitle = "It's safe space where no one can bother you",
        tagRelations = tagRelations(TagRelation.RequiredAll to Tags.Access.Home),
    ),
    location(
        title = "Graveyard",
        subtitle = "A place where they hide ${Flavors.objectifiedPeopleName.placeholder} in the ground",
        tagRelations = tagRelations(
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
        tagRelations = tagRelations(
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
        tagRelations = tagRelations(
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
        tagRelations = tagRelations(
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
        tagRelations = tagRelations(TagRelation.Provides to Tags.Locations.Streets)
    ),
    location(
        title = "Dark Alley",
        subtitle = "Sometimes ${Flavors.peopleName.placeholder} go through here to save time",
        tagRelations = tagRelations(TagRelation.Provides to Tags.Locations.DarkAlley)
    ),
    location(
        title = "Super Market",
        subtitle = "The food is everywhere",
        tagRelations = tagRelations(TagRelation.Provides to Tags.Locations.SuperMarket)
    ),
    location(
        title = "Rooftops",
        subtitle = "They look like ants from here",
        tagRelations = tagRelations(
            TagRelation.RequiredAll to listOf(
                Tags.State.Flying,
            ),
            TagRelation.Provides to listOf(
                Tags.Locations.Rooftops
            )
        )
    ),
    location(
        title = "Forest",
        subtitle = "It's rare to see ${Flavors.objectifiedPeopleName.placeholder} here",
        tagRelations = tagRelations(TagRelation.Provides to Tags.Locations.Forest)
    ),
    location(
        title = "Ship Crash Site",
        subtitle = "It won't fly again, but there are still some useful things inside.",
        tagRelations = tagRelations(
            TagRelation.RequiredAll to listOf(
                Tags.Access.UfoCrashSite,
            ),
            TagRelation.Provides to listOf(
                Tags.Locations.UfoCrashSite
            )
        )
    ),
).makeIdsUnique()
