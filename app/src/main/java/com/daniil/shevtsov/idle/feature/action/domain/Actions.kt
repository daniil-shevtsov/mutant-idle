package com.daniil.shevtsov.idle.feature.action.domain

import com.daniil.shevtsov.idle.feature.flavor.Flavors
import com.daniil.shevtsov.idle.feature.location.domain.Location
import com.daniil.shevtsov.idle.feature.main.domain.Selectable
import com.daniil.shevtsov.idle.feature.ratio.domain.RatioKey
import com.daniil.shevtsov.idle.feature.resource.domain.ResourceKey
import com.daniil.shevtsov.idle.feature.resource.domain.resourceChanges
import com.daniil.shevtsov.idle.feature.tagsystem.domain.TagRelation
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tags
import com.daniil.shevtsov.idle.feature.tagsystem.domain.tagRelations
import com.daniil.shevtsov.idle.feature.upgrade.domain.Upgrade

fun createAllActions() = listOf(
    action(
        title = "Work",
        subtitle = "The sun is high",
        plot = "You worked some more",
        resourceChanges = resourceChanges(ResourceKey.Money to 25.0),
        tagRelations = tagRelations(
            TagRelation.RequiredAll to Tags.Form.Human,
            TagRelation.RequiredAll to Tags.Employed,
        ),
    ),
    action(
        title = "Buy Groceries",
        subtitle = "It's a short walk",
        plot = "You've bought some groceries",
        resourceChanges = resourceChanges(
            ResourceKey.Money to -15.0,
            ResourceKey.HumanFood to 1.0,
        ),
        tagRelations = tagRelations(
            TagRelation.RequiredAll to Tags.Form.Human,
            TagRelation.RequiredAll to Tags.Locations.SuperMarket,
        ),
    ),
    action(
        title = "Capture a ${Flavors.personName.placeholder}",
        subtitle = "I think I can do it if I grow enough",
        plot = "You have captured a ${Flavors.personName.placeholder}. What next?",
        resourceChanges = resourceChanges(
            ResourceKey.Blood to -10.0,
            ResourceKey.Prisoner to 1.0,
        ),
        ratioChanges = ratioChanges(
            RatioKey.Suspicion to 0.2,
        ),
        tagRelations = tagRelations(
            TagRelation.RequiredAll to listOf(
                Tags.PersonCapturer,
                Tags.Locations.DarkAlley,
            )
        ),
    ),
    action(
        title = "Eat captured ${Flavors.personName.placeholder}",
        subtitle = "Finally a good meal",
        plot = "You've eaten. What to do about the mess?",
        resourceChanges = resourceChanges(
            ResourceKey.Blood to 25.0,
            ResourceKey.Prisoner to -1.0,
            ResourceKey.Remains to 1.0,
        ),
        ratioChanges = ratioChanges(
            RatioKey.Suspicion to 0.05,
        ),
        tagRelations = tagRelations(
            TagRelation.RequiredAll to listOf(
                Tags.Species.Devourer,
            )
        ),
    ),
    action(
        title = "Drink blood of the captured ${Flavors.personName.placeholder}",
        subtitle = "Drink up",
        plot = "You drank the ${Flavors.personName.placeholder} dry. What to do about the mess?",
        resourceChanges = resourceChanges(
            ResourceKey.Blood to 10.0,
            ResourceKey.Prisoner to -1.0,
            ResourceKey.Remains to 1.0,
        ),
        ratioChanges = ratioChanges(
            RatioKey.Suspicion to 0.05,
        ),
        tagRelations = tagRelations(
            TagRelation.RequiredAll to listOf(
                Tags.Species.Vampire,
            )
        ),
    ),
    action(
        title = "Eat human food",
        subtitle = "It's not enough",
        resourceChanges = resourceChanges(
            ResourceKey.Blood to 2.0,
            ResourceKey.HumanFood to -1.0,
        ),
        tagRelations = tagRelations(
            TagRelation.RequiredAll to listOf(
                Tags.Species.Devourer,
            )
        )
    ),
    action(
        title = "Bury remains",
        subtitle = "You better hope there is space",
        resourceChanges = resourceChanges(
            ResourceKey.Remains to -1.0,
        ),
        ratioChanges = ratioChanges(
            RatioKey.Suspicion to -0.05,
        ),
        tagRelations = tagRelations(
            TagRelation.RequiredAll to listOf(
                Tags.Locations.Graveyard,
            )
        ),
    ),
    action(
        title = "Steal organs from corpse",
        subtitle = "They won't need it",
        resourceChanges = resourceChanges(
            ResourceKey.Organs to 1.0,
        ),
        ratioChanges = ratioChanges(
            RatioKey.Suspicion to 0.1,
        ),
        tagRelations = tagRelations(
            TagRelation.RequiredAll to listOf(
                Tags.Species.Devourer,
                Tags.Access.FreshCorpses,
            )
        ),
    ),
    action(
        title = "Burn remains",
        subtitle = "It won't leave a trace",
        resourceChanges = resourceChanges(
            ResourceKey.Remains to -1.0,
        ),
        ratioChanges = ratioChanges(
            RatioKey.Suspicion to -0.1,
        ),
        tagRelations = tagRelations(
            TagRelation.RequiredAll to listOf(
                Tags.Access.Incinerator,
            )
        ),
    ),
    action(
        title = "Become invisible",
        subtitle = "You ${Flavors.invisibilityAction.placeholder}, ${Flavors.derogativePeopleName.placeholder} can't see you now",
        tagRelations = tagRelations(
            TagRelation.RequiredAll to listOf(Tags.Abilities.Invisibility),
            TagRelation.Provides to listOf(Tags.State.Invisible),
        ),
    ),
    action(
        title = "Become visible",
        subtitle = "You become visible again",
        tagRelations = tagRelations(TagRelation.Removes to Tags.State.Invisible),
    ),
    action(
        title = "Become a bat",
        subtitle = "Fly fly away",
        tagRelations = tagRelations(
            TagRelation.RequiredAll to listOf(Tags.Abilities.BatForm),
            TagRelation.Provides to listOf(
                Tags.Form.Animal,
                Tags.Abilities.Flight
            ),
            TagRelation.Removes to listOf(Tags.Form.Human),
            TagRelation.RequiresNone to listOf(Tags.Form.Animal),
        ),
    ),
    action(
        title = "Return to human form",
        subtitle = "You become human again",
        tagRelations = tagRelations(
            TagRelation.RequiredAll to listOf(
                Tags.Abilities.BatForm,
                Tags.Form.Animal,
            ),
            TagRelation.Provides to listOf(Tags.Form.Human),
            TagRelation.Removes to listOf(
                Tags.Form.Animal,
                Tags.Abilities.Flight
            ),
        ),
    ),
    action(
        title = "Fly",
        subtitle = "You can get to high places",
        plot = "You are flying",
        tagRelations = tagRelations(
            TagRelation.RequiredAll to listOf(
                Tags.Abilities.Flight,
            ),
            TagRelation.Provides to listOf(
                Tags.State.Flying,
            ),
            TagRelation.RequiresNone to listOf(
                Tags.State.Flying
            ),
        ),
    ),
    action(
        title = "Land",
        subtitle = "Enough of flying",
        plot = "You've landed",
        tagRelations = tagRelations(
            TagRelation.RequiredAll to listOf(
                Tags.Abilities.Flight,
                Tags.State.Flying,
            ),
            TagRelation.Removes to listOf(
                Tags.State.Flying,
            ),
        ),
    ),
    action(
        title = "Steal money from people",
        subtitle = "For some reason they don't like it",
        resourceChanges = resourceChanges(
            ResourceKey.Money to 15.0
        ),
        ratioChanges = ratioChanges(
            RatioKey.Suspicion to 0.005, //TODO:  Need easy scalable way to say that when invisible you do it with less suspicion
        ),
        tagRelations = tagRelations(
            TagRelation.RequiredAll to listOf(
                Tags.Nimble,
                Tags.Locations.Streets,
            ),
            TagRelation.RequiredAny to listOf(
                Tags.Form.Human,
                Tags.State.Invisible,
            )
        )
    ),
    action(
        title = "Rob people",
        subtitle = "They scare so easily",
        resourceChanges = resourceChanges(
            ResourceKey.Money to 40.0
        ),
        ratioChanges = ratioChanges(
            RatioKey.Suspicion to 0.015
        ),
        tagRelations = tagRelations(
            TagRelation.RequiredAll to listOf(
                Tags.Armed,
                Tags.Locations.DarkAlley,
            )
        )
    ),
    action(
        title = "Buy a knife",
        subtitle = "It's useful in a myriad of situations",
        resourceChanges = resourceChanges(
            ResourceKey.Money to -80.0
        ),
        tagRelations = tagRelations(
            TagRelation.RequiredAll to listOf(
                Tags.Form.Human,
                Tags.Locations.Streets,
            ),
            TagRelation.Provides to listOf(
                Tags.Armed
            )
        )
    ),
    action(
        title = "Beg for money",
        subtitle = "It's not much but they don't seem to mind",
        resourceChanges = resourceChanges(
            ResourceKey.Money to 0.5
        ),
        tagRelations = tagRelations(
            TagRelation.RequiredAll to listOf(
                Tags.Form.Human,
                Tags.Knowledge.SocialNorms,
                Tags.Locations.Streets,
            )
        )
    ),
    action(
        title = "Steal Food",
        subtitle = "It's just lying there",
        plot = "You've stolen some food, but it's not enough",
        resourceChanges = resourceChanges(
            ResourceKey.HumanFood to 1.0,
        ),
        ratioChanges = ratioChanges(
            RatioKey.Suspicion to 0.005,
        ),
        tagRelations = tagRelations(
            TagRelation.RequiredAll to listOf(
                Tags.Form.Human,
                Tags.Nimble,
                Tags.Locations.SuperMarket,
            )
        ),
    ),
    action(
        title = "Capture a rat",
        subtitle = "It's small but tasty",
        plot = "You've caught a rat",
        resourceChanges = resourceChanges(
            ResourceKey.FreshMeat to 1.0
        ),
        tagRelations = tagRelations(
            TagRelation.RequiredAll to listOf(
                Tags.Locations.DarkAlley,
            ),
            TagRelation.RequiredAny to listOf(
                Tags.Species.Devourer,
                Tags.Species.Demon,
                Tags.Species.Vampire,
            )
        )
    ),
    action(
        title = "Eat fresh meat",
        subtitle = "It's good but I need something bigger",
        resourceChanges = resourceChanges(
            ResourceKey.FreshMeat to -1.0,
            ResourceKey.Blood to 1.0,
        ),
        tagRelations = tagRelations(
            TagRelation.RequiredAny to listOf(
                Tags.Species.Devourer,
                Tags.Species.Demon,
                Tags.Species.Vampire,
            )
        )
    ),
    action(
        title = "Eat the remains",
        subtitle = "They can't find anything if there is nothing",
        plot = "You have devoured the remains",
        resourceChanges = resourceChanges(
            ResourceKey.Blood to -5.0,
            ResourceKey.Remains to -1.0,
        ),
        tagRelations = tagRelations(
            TagRelation.RequiredAll to listOf(
                Tags.Species.Devourer,
                Tags.Body.IronJaws,
            )
        ),
        ratioChanges = ratioChanges(
            RatioKey.Suspicion to -0.1,
        )
    ),
    action(
        title = "Observe ${Flavors.objectifiedPeopleName.placeholder}",
        subtitle = "Living their lives, not knowing what lurks in shadows",
        plot = "You have learned something important about them",
        resourceChanges = resourceChanges(
            ResourceKey.Information to +1.0,
        ),
        tagRelations = tagRelations(TagRelation.RequiredAny to Tags.Locations.Rooftops),
    ),
    action(
        title = "Capture ${Flavors.personName.placeholder} when they don't suspect it",
        subtitle = "You've learned enough about them to do this undetected",
        plot = "Everything went smoothly, no one will know they are gone",
        resourceChanges = resourceChanges(
            ResourceKey.Information to -10.0,
            ResourceKey.Prisoner to 1.0,
        ),
        tagRelations = tagRelations(
            TagRelation.RequiredAll to Tags.PersonCapturer
        ),
    ),
    action(
        title = "You can influence this ${Flavors.personName.placeholder}'s thoughts.",
        subtitle = "It works in subtle ways",
        plot = "Everything went smoothly, no one will know they are gone",
        resourceChanges = resourceChanges(
            ResourceKey.Blood to -10.0,
            ResourceKey.ControlledMind to 1.0,
        ),
        tagRelations = tagRelations(
            TagRelation.RequiredAll to Tags.Abilities.Hypnosis
        ),
    ),
    action(
        title = "Make them want to give you a gift",
        subtitle = "It's not really expensive",
        plot = "\"Wow, how thoughtful, what gave you this idea?\"",
        resourceChanges = resourceChanges(
            ResourceKey.ControlledMind to 0.0, //TODO: I need to be able to require resource without spending it
            ResourceKey.Money to 10.0,
        ),
        tagRelations = tagRelations(
            TagRelation.RequiredAll to Tags.Abilities.Hypnosis
        ),
    ),
    action(
        title = "Make them think they've done your crime",
        subtitle = "They are going to go away for a long time, but you'll get less attention.",
        plot = "Breaking News: seemingly ordinary person confessed to unthinkable", //TODO: Random variations would work well here
        resourceChanges = resourceChanges(
            ResourceKey.ControlledMind to -1.0,
        ),
        ratioChanges = ratioChanges(
            RatioKey.Suspicion to -0.05
        ),
        tagRelations = tagRelations(
            TagRelation.RequiredAll to Tags.Abilities.Hypnosis
        ),
    ),
    action(
        title = "Hide in the woods for a while",
        subtitle = "Need to stockpile on food for this",
        plot = "Some time has passed, things wound down a little bit",
        resourceChanges = resourceChanges(
            //TODO: workaround would be like ratioWithTags but resourceChanges is not refactored to that
            ResourceKey.HumanFood to -5.0, //TODO: Resources should use tags too i.e. <food> does not matter what kind. Or even energy source, like for android it's not food, it's electricity
        ),
        ratioChanges = ratioChanges(
            RatioKey.Suspicion to -0.05
        ),
        tagRelations = tagRelations(
            TagRelation.RequiredAll to Tags.Locations.Forest
        ),
    ),
    action(
        title = "Gather some scrap from your ship",
        subtitle = "This is soul-crushing to tear it apart",
        plot = "You've managed to gather some scrap",
        resourceChanges = resourceChanges(
            ResourceKey.Scrap to 1.0
        ),
        tagRelations = tagRelations(
            TagRelation.RequiredAll to Tags.Locations.UfoCrashSite
        ),
    ),
    action(
        title = "Gather Scrap",
        subtitle = "These ${Flavors.peopleName.placeholder} throw away the most useful things",
        resourceChanges = resourceChanges(
            ResourceKey.Scrap to 1.0,
        ),
        tagRelations = tagRelations(
            TagRelation.RequiredAll to Tags.Locations.Scrapyard
        )
    ),
    action(
        title = "Loose the game",
        subtitle = "Instantly loose for debugging purposes",
        tagRelations = tagRelations(TagRelation.RequiredAll to Tags.Species.Devourer),
        ratioChanges = ratioChanges(RatioKey.Suspicion to 1.0),
    ),
    action(
        title = "Win the game",
        subtitle = "Instantly win for debugging purposes",
        tagRelations = tagRelations(TagRelation.RequiredAll to Tags.Species.Devourer),
        ratioChanges = ratioChanges(RatioKey.Mutanity to 1.0)
    ),
    action(
        title = Flavors.appearanceChangeAction.placeholder,
        subtitle = "This will make your enemies loose track",
        tagRelations = tagRelations(TagRelation.RequiredAll to Tags.Abilities.AppearanceChange)
    ),
).makeIdsUnique()

fun List<Selectable>.makeIdsUnique(): List<Selectable> {
    return mapIndexed { index, selectable ->
        val offset = when (selectable) {
            is Action -> 10000L
            is Upgrade -> 20000L
            is Location -> 30000L
            else -> 0L //TODO: Figure out why sealed interface does not work here
        }
        selectable.copy(id = offset + index.toLong())
    }
}
