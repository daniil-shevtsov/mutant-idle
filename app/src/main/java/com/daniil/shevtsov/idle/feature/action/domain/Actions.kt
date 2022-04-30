package com.daniil.shevtsov.idle.feature.action.domain

import com.daniil.shevtsov.idle.feature.flavor.Flavors
import com.daniil.shevtsov.idle.feature.ratio.domain.RatioKey
import com.daniil.shevtsov.idle.feature.resource.domain.ResourceKey
import com.daniil.shevtsov.idle.feature.tagsystem.domain.TagRelation
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tags

fun createAllActions() = listOf(
    action(
        id = 0L,
        title = "Work",
        subtitle = "The sun is high",
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
        id = 3L,
        title = "Buy Groceries",
        subtitle = "It's a short walk",
        resourceChanges = mapOf(
            ResourceKey.Money to -15.0,
            ResourceKey.HumanFood to 1.0,
        ),
        tags = mapOf(
            TagRelation.RequiredAll to listOf(
                Tags.HumanAppearance,
            )
        ),
    ),
    action(
        id = 8L,
        title = "Capture a person",
        subtitle = "I think I can do it if I grow enough",
        resourceChanges = mapOf(
            ResourceKey.Blood to -10.0,
            ResourceKey.Prisoner to 1.0,
        ),
        ratioChanges = mapOf(
            RatioKey.Suspicion to 0.1f,
        ),
        tags = mapOf(
            TagRelation.RequiredAll to listOf(
                Tags.PersonCapturer,
            )
        ),
    ),
    action(
        id = 9L,
        title = "Eat captured person",
        subtitle = "What to do about the mess",
        resourceChanges = mapOf(
            ResourceKey.Blood to 25.0,
            ResourceKey.Prisoner to -1.0,
            ResourceKey.Remains to 1.0,
        ),
        ratioChanges = mapOf(
            RatioKey.Suspicion to 0.05f,
        ),
        tags = mapOf(
            TagRelation.RequiredAll to listOf(
                Tags.Species.Devourer,
            )
        ),
    ),
    action(
        id = 10L,
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
        id = 11L,
        title = "Bury remains",
        subtitle = "You better hope there is space",
        resourceChanges = mapOf(
            ResourceKey.Remains to -1.0,
        ),
        ratioChanges = mapOf(
            RatioKey.Suspicion to -0.05f,
        ),
        tags = mapOf(
            TagRelation.RequiredAll to listOf(
                Tags.GraveyardAccess,
            )
        ),
    ),
    action(
        id = 12L,
        title = "Steal organs from corpse",
        subtitle = "They won't need it",
        resourceChanges = mapOf(
            ResourceKey.Organs to 1.0,
        ),
        ratioChanges = mapOf(
            RatioKey.Suspicion to 0.1f,
        ),
        tags = mapOf(
            TagRelation.RequiredAll to listOf(
                Tags.Species.Devourer,
                Tags.FreshCorpseAccess,
            )
        ),
    ),
    action(
        id = 13L,
        title = "Burn remains",
        subtitle = "It won't leave a trace",
        resourceChanges = mapOf(
            ResourceKey.Remains to -1.0,
        ),
        ratioChanges = mapOf(
            RatioKey.Suspicion to -0.1f,
        ),
        tags = mapOf(
            TagRelation.RequiredAll to listOf(
                Tags.IncineratorAccess,
            )
        ),
    ),
    action(
        id = 14L,
        title = "Become invisible",
        subtitle = "You ${Flavors.invisibilityAction.placeholder}, ${Flavors.derogativePeopleName.placeholder} can't see you now",
        tags = mapOf(
            TagRelation.RequiredAll to listOf(
                Tags.Abilities.Invisibility,
            ),
            TagRelation.Provides to listOf(
                Tags.State.Invisible
            ),
            TagRelation.RequiresNone to listOf( //TODO: Maybe this should be default behavior for providing tags?
                Tags.State.Invisible,
            )
        ),
    ),
    action(
        id = 15L,
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
)
