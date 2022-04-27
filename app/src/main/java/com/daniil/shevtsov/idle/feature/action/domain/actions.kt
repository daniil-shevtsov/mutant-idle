package com.daniil.shevtsov.idle.feature.action.domain

import com.daniil.shevtsov.idle.feature.ratio.domain.RatioKey
import com.daniil.shevtsov.idle.feature.resource.domain.ResourceKey
import com.daniil.shevtsov.idle.feature.tagsystem.domain.TagRelation
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tags

fun createAllActions() = listOf(
    action(
        id = 0L,
        title = "Work",
        subtitle = "The sun is high",
        actionType = ActionType.Human,
        resourceChanges = mapOf(
            ResourceKey.Money to 25.0
        ),
        tags = mapOf(
            Tags.HumanAppearance to TagRelation.Required,
            Tags.Employed to TagRelation.Required,
        ),
    ),
    action(
        id = 3L,
        title = "Buy Groceries",
        subtitle = "It's a short walk",
        actionType = ActionType.Human,
        resourceChanges = mapOf(
            ResourceKey.Money to -15.0,
            ResourceKey.HumanFood to 1.0,
        ),
        tags = mapOf(
            Tags.HumanAppearance to TagRelation.Required,
        ),
    ),
    action(
        id = 8L,
        title = "Capture a person",
        subtitle = "I think I can do it if I grow enough",
        actionType = ActionType.Mutant,
        tags = mapOf(
            Tags.PersonCapturer to TagRelation.Required,
        ),
        resourceChanges = mapOf(
            ResourceKey.Blood to -10.0,
            ResourceKey.Prisoner to 1.0,
        ),
        ratioChanges = mapOf(
            RatioKey.Suspicion to 0.1f,
        )
    ),
    action(
        id = 9L,
        title = "Eat captured person",
        subtitle = "What to do about the mess",
        actionType = ActionType.Mutant,
        resourceChanges = mapOf(
            ResourceKey.Blood to 25.0,
            ResourceKey.Prisoner to -1.0,
            ResourceKey.Remains to 1.0,
        ),
        ratioChanges = mapOf(
            RatioKey.Suspicion to 0.05f,
        ),
        tags = mapOf(
            Tags.Devourer to TagRelation.Required,
        ),
    ),
    action(
        id = 10L,
        title = "Eat human food",
        subtitle = "It's not enough",
        actionType = ActionType.Human,
        resourceChanges = mapOf(
            ResourceKey.Blood to 2.0,
            ResourceKey.HumanFood to -1.0,
        )
    ),
    action(
        id = 11L,
        title = "Bury remains",
        subtitle = "You better hope there is space",
        actionType = ActionType.Human,
        resourceChanges = mapOf(
            ResourceKey.Remains to -1.0,
        ),
        tags = mapOf(
            Tags.GraveyardAccess to TagRelation.Required,
        ),
        ratioChanges = mapOf(
            RatioKey.Suspicion to -0.05f,
        )
    ),
    action(
        id = 12L,
        title = "Steal organs from corpse",
        subtitle = "They won't need it",
        actionType = ActionType.Human,
        resourceChanges = mapOf(
            ResourceKey.Organs to 1.0,
        ),
        tags = mapOf(
            Tags.FreshCorpseAccess to TagRelation.Required,
        ),
        ratioChanges = mapOf(
            RatioKey.Suspicion to 0.1f,
        )
    ),
    action(
        id = 13L,
        title = "Burn remains",
        subtitle = "It won't leave a trace",
        actionType = ActionType.Human,
        resourceChanges = mapOf(
            ResourceKey.Remains to -1.0,
        ),
        tags = mapOf(
            Tags.IncineratorAccess to TagRelation.Required,
        ),
        ratioChanges = mapOf(
            RatioKey.Suspicion to -0.1f,
        )
    ),
)
