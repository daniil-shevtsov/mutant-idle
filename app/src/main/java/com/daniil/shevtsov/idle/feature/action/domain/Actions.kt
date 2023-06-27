package com.daniil.shevtsov.idle.feature.action.domain

import com.daniil.shevtsov.idle.feature.flavor.Flavors
import com.daniil.shevtsov.idle.feature.ratio.domain.RatioKey
import com.daniil.shevtsov.idle.feature.resource.domain.ResourceKey
import com.daniil.shevtsov.idle.feature.tagsystem.domain.TagRelation
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tags

fun createAllActions() = listOf(
    action(
        id = makeActionIdUnique(0L),
        title = "Work",
        subtitle = "The sun is high",
        plot = "You worked some more",
        resourceChanges = mapOf(
            ResourceKey.Money to 25.0
        ),
        tags = mapOf(
            TagRelation.RequiredAll to listOf(
                Tags.HumanAppearance,
                Tags.Employed,
            )
        ),
    ),
    action(
        id = makeActionIdUnique(3L),
        title = "Buy Groceries",
        subtitle = "It's a short walk",
        plot = "You've bought some groceries",
        resourceChanges = mapOf(
            ResourceKey.Money to -15.0,
            ResourceKey.HumanFood to 1.0,
        ),
        tags = mapOf(
            TagRelation.RequiredAll to listOf(
                Tags.HumanAppearance,
                Tags.Locations.SuperMarket,
            )
        ),
    ),
    action(
        id = makeActionIdUnique(8L),
        title = "Capture a person",
        subtitle = "I think I can do it if I grow enough",
        plot = "You have captured a person. What next?",
        resourceChanges = mapOf(
            ResourceKey.Blood to -10.0,
            ResourceKey.Prisoner to 1.0,
        ),
        ratioChanges = ratioChanges(
            RatioKey.Suspicion to 0.2,
        ),
        tags = mapOf(
            TagRelation.RequiredAll to listOf(
                Tags.PersonCapturer,
                Tags.Locations.DarkAlley,
            )
        ),
    ),
    action(
        id = makeActionIdUnique(9L),
        title = "Eat captured person",
        subtitle = "Finally a good meal",
        plot = "You've eaten. What to do about the mess?",
        resourceChanges = mapOf(
            ResourceKey.Blood to 25.0,
            ResourceKey.Prisoner to -1.0,
            ResourceKey.Remains to 1.0,
        ),
        ratioChanges = ratioChanges(
            RatioKey.Suspicion to 0.05,
        ),
        tags = mapOf(
            TagRelation.RequiredAll to listOf(
                Tags.Species.Devourer,
            )
        ),
    ),
    action(
        id = makeActionIdUnique(10L),
        title = "Eat human food",
        subtitle = "It's not enough",
        resourceChanges = mapOf(
            ResourceKey.Blood to 2.0,
            ResourceKey.HumanFood to -1.0,
        ),
        tags = mapOf(
            TagRelation.RequiredAll to listOf(
                Tags.Species.Devourer,
            )
        )
    ),
    action(
        id = makeActionIdUnique(11L),
        title = "Bury remains",
        subtitle = "You better hope there is space",
        resourceChanges = mapOf(
            ResourceKey.Remains to -1.0,
        ),
        ratioChanges = ratioChanges(
            RatioKey.Suspicion to -0.05,
        ),
        tags = mapOf(
            TagRelation.RequiredAll to listOf(
                Tags.Locations.Graveyard,
            )
        ),
    ),
    action(
        id = makeActionIdUnique(12L),
        title = "Steal organs from corpse",
        subtitle = "They won't need it",
        resourceChanges = mapOf(
            ResourceKey.Organs to 1.0,
        ),
        ratioChanges = ratioChanges(
            RatioKey.Suspicion to 0.1,
        ),
        tags = mapOf(
            TagRelation.RequiredAll to listOf(
                Tags.Species.Devourer,
                Tags.Access.FreshCorpses,
            )
        ),
    ),
    action(
        id = makeActionIdUnique(13L),
        title = "Burn remains",
        subtitle = "It won't leave a trace",
        resourceChanges = mapOf(
            ResourceKey.Remains to -1.0,
        ),
        ratioChanges = ratioChanges(
            RatioKey.Suspicion to -0.1,
        ),
        tags = mapOf(
            TagRelation.RequiredAll to listOf(
                Tags.Access.Incinerator,
            )
        ),
    ),
    action(
        id = makeActionIdUnique(14L),
        title = "Become invisible",
        subtitle = "You ${Flavors.invisibilityAction.placeholder}, ${Flavors.derogativePeopleName.placeholder} can't see you now",
        tags = mapOf(
            TagRelation.RequiredAll to listOf(
                Tags.Abilities.Invisibility,
            ),
            TagRelation.Provides to listOf(
                Tags.State.Invisible
            ),
            TagRelation.RequiresNone to listOf(
                //TODO: Maybe this should be default behavior for providing tags?
                Tags.State.Invisible,
            )
        ),
    ),
    action(
        id = makeActionIdUnique(15L),
        title = "Become visible",
        subtitle = "You become visible again",
        tags = mapOf(
            TagRelation.RequiredAll to listOf(
                Tags.Abilities.Invisibility,
                Tags.State.Invisible,
            ),
            TagRelation.Removes to listOf( //TODO: I sense inconsistency in this one
                Tags.State.Invisible
            )
        ),
    ),
    action(
        id = makeActionIdUnique(16L),
        title = "Steal money from people",
        subtitle = "For some reason they don't like it",
        resourceChanges = mapOf(
            ResourceKey.Money to 15.0
        ),
        ratioChanges = ratioChanges(
            RatioKey.Suspicion to 0.005,
        ),
        tags = mapOf(
            TagRelation.RequiredAll to listOf(
                Tags.Nimble,
                Tags.Locations.Streets,
            ),
            TagRelation.RequiredAny to listOf(
                Tags.HumanAppearance,
                Tags.State.Invisible,
            )
        )
    ),
    action(
        id = makeActionIdUnique(17L),
        title = "Rob people",
        subtitle = "They scare so easily",
        resourceChanges = mapOf(
            ResourceKey.Money to 40.0
        ),
        ratioChanges = ratioChanges(
            RatioKey.Suspicion to 0.015
        ),
        tags = mapOf(
            TagRelation.RequiredAll to listOf(
                Tags.Armed,
                Tags.Locations.DarkAlley,
            )
        )
    ),
    action(
        id = makeActionIdUnique(18L),
        title = "Buy a knife",
        subtitle = "It's useful in a myriad of situations",
        resourceChanges = mapOf(
            ResourceKey.Money to -80.0
        ),
        tags = mapOf(
            TagRelation.RequiredAll to listOf(
                Tags.HumanAppearance,
                Tags.Locations.Streets,
            ),
            TagRelation.Provides to listOf(
                Tags.Armed
            )
        )
    ),
    action(
        id = makeActionIdUnique(19L),
        title = "Beg for money",
        subtitle = "It's not much but they don't seem to mind",
        resourceChanges = mapOf(
            ResourceKey.Money to 0.5
        ),
        tags = mapOf(
            TagRelation.RequiredAll to listOf(
                Tags.HumanAppearance,
                Tags.Knowledge.SocialNorms,
                Tags.Locations.Streets,
            )
        )
    ),
    action(
        id = makeActionIdUnique(20L),
        title = "Steal Food",
        subtitle = "It's just lying there",
        plot = "You've stolen some food, but it's not enough",
        resourceChanges = mapOf(
            ResourceKey.HumanFood to 1.0,
        ),
        ratioChanges = ratioChanges(
            RatioKey.Suspicion to 0.005,
        ),
        tags = mapOf(
            TagRelation.RequiredAll to listOf(
                Tags.HumanAppearance,
                Tags.Nimble,
                Tags.Locations.SuperMarket,
            )
        ),
    ),
    action(
        id = makeActionIdUnique(21L),
        title = "Capture a rat",
        subtitle = "It's small but tasty",
        plot = "You've caught a rat",
        resourceChanges = mapOf(
            ResourceKey.FreshMeat to 1.0
        ),
        tags = mapOf(
            TagRelation.RequiredAll to listOf(
                Tags.Species.Devourer,
                Tags.Locations.DarkAlley,
            )
        )
    ),
    action(
        id = makeActionIdUnique(22L),
        title = "Eat fresh meat",
        subtitle = "It's good but I need something bigger",
        resourceChanges = mapOf(
            ResourceKey.FreshMeat to -1.0,
            ResourceKey.Blood to 1.0,
        ),
        tags = mapOf(
            TagRelation.RequiredAll to listOf(
                Tags.Species.Devourer
            )
        )
    ),
    action(
        id = makeActionIdUnique(23L),
        title = "Eat the remains",
        subtitle = "They can't find anything if there is nothing",
        plot = "You have devoured the remains",
        resourceChanges = mapOf(
            ResourceKey.Blood to -5.0,
            ResourceKey.Remains to -1.0,
        ),
        tags = mapOf(
            TagRelation.RequiredAll to listOf(
                Tags.Species.Devourer,
                Tags.Body.IronJaws,
            )
        ),
        ratioChanges = ratioChanges(
            RatioKey.Suspicion to -0.1,
        )
    ),
)

private fun makeActionIdUnique(id: Long): Long = 10000L + id
