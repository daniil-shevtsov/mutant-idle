package com.daniil.shevtsov.idle.feature.upgrade.domain

import com.daniil.shevtsov.idle.feature.action.domain.makeIdsUnique
import com.daniil.shevtsov.idle.feature.flavor.Flavors
import com.daniil.shevtsov.idle.feature.resource.domain.ResourceKey
import com.daniil.shevtsov.idle.feature.tagsystem.domain.TagRelation
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tags
import com.daniil.shevtsov.idle.feature.tagsystem.domain.tagRelations

fun createUpgrades() = listOf(
    upgrade(
        title = "Iron jaws",
        subtitle = "Your bite is stronger than of a shark",
        price = 10.0,
        resourceChanges = mapOf(ResourceKey.Blood to -10.0),
        status = UpgradeStatus.NotBought,
        tagRelations = tagRelations(
            TagRelation.Provides to Tags.Body.IronJaws,
            TagRelation.RequiredAny to Tags.Species.Devourer,
        )
    ),
    upgrade(
        title = "Super Strength",
        subtitle = "Lifting a car or crushing a lock is not a problem anymore",
        price = 25.0,
        resourceChanges = mapOf(ResourceKey.Blood to -25.0),
        tagRelations = tagRelations(
            TagRelation.Provides to listOf(
                Tags.Body.SuperStrength,
                Tags.PersonCapturer, //TODO: It seems the system should work in other direction. Person Capturer should define that it can be because of super srength, because of weapons, etc
            ),
            TagRelation.RequiredAny to listOf(
                Tags.Species.Devourer,
                Tags.Species.ShapeShifter,
                Tags.Species.Demon,
                Tags.Species.Vampire,
                Tags.Species.Android,
            ),
        )
    ),
    upgrade(
        title = "Becoming a bat",
        subtitle = "People don't pay attention to such critters, it is small and it can fly",
        price = 10.0,
        resourceChanges = mapOf(ResourceKey.Blood to -10.0),
        tagRelations = tagRelations(
            TagRelation.Provides to Tags.Abilities.BatForm,
            TagRelation.RequiredAny to Tags.Species.Vampire,
        )
    ),
    upgrade(
        title = "Invisibility",
        subtitle = Flavors.invisibilityGain.placeholder,
        price = 1.0,
        resourceChanges = mapOf(ResourceKey.Blood to -1.0),
        tagRelations = tagRelations(
            TagRelation.Provides to listOf(Tags.Abilities.Invisibility),
            TagRelation.RequiredAny to listOf(
                Tags.Species.Vampire,
                Tags.Species.Android,
                Tags.Species.Alien,
            ),
        )
    ),
).makeIdsUnique()
