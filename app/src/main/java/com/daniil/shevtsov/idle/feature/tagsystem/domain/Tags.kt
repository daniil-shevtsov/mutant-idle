package com.daniil.shevtsov.idle.feature.tagsystem.domain

object Tags {
    val Hypnosis = Tag(name = "Hypnosis", description = "Can control people")
    val Heliophobia = Tag(name = "Heliophobia", description = "Can't be in the sun")
    val Immortal = Tag(name = "Immortal", description = "Can't be killed in any way")
    val Mutating = Tag(name = "Mutating")
    val Growth = Tag(name = "Growth")

    val PersonCapturer = Tag(name = "Person Capturer")

    val HumanAppearance = Tag(name = "Human Appearance")
    val Employed = Tag(name = "Employed")

    object Species {
        val Devourer = Tag(name = "Devourer")
        val ShapeShifter = Tag(name = "Shape Shifter")
        val Vampire = Tag(name = "Vampire")
        val Parasite = Tag(name = "Parasite")
        val Demon = Tag(name = "Demon")
        val Alien = Tag(name = "Alien")
        val Android = Tag(name = "Android")
    }

    //TODO: There seems to be a need in a system
    object Access {
        val Graveyard = Tag(name = "Graveyard Access")
        val Morgue = Tag(name = "Morgue Access")
        val ButcherShop = Tag(name = "Butcher Shop Access")
        val Scrapyard = Tag(name = "Scrapyard Access")

        val Meat = Tag(name = "Meat access")
        val FreshCorpses = Tag(name = "Fresh corpses")
        val Incinerator = Tag(name = "Incinerator")
    }

    object Locations {
        val Graveyard = Tag(name = "Graveyard Location")
        val Morgue = Tag(name = "Morgue Location")
        val ButcherShop = Tag(name = "Butcher Shop Location")
        val Scrapyard = Tag(name = "Scrapyard Location")
    }

    object Knowledge {
        val SocialNorms = Tag(name = "Social Norms")
    }

    object Body {
        val HandSword = Tag(name = "Hand Sword")
        val Fangs = Tag(name = "Fangs")
        val IronJaws = Tag(name = "Iron Jaws")
        val Darts = Tag("Darts")
        val LiquidForm = Tag("Liquid Form")
        val SuperStrength = Tag("Super Strength")
    }

    object Abilities {
        val Invisibility = Tag(name = "Invisibility Ability")
        val Flight = Tag(name = "Flight Ability")
        val AnimalForm = Tag(name = "Animal Form Ability")
        val TransferHost = Tag(name = "Transfer Host Ability")
    }

    object State {
        val Invisible = Tag(name = "Invisible")
    }

    object Nature {
        val Magic = Tag(name = "Magic")
        val Tech = Tag(name = "Tech")
    }

    val Surgeon = Tag(name = "Surgeon", description = "You are very precise with the scalpel")
    val SolitaryJob = Tag(name = "Solitary Job", description = "People don't pay attention as long as the job gets done")
    val CorpseAccess = Tag(name = "Corpse Access")
    val MeatAccess = Tag(name = "Meat Access")
    val LaborIntensive = Tag(name = "Labor intensive job")
    val SocialJob = Tag(name = "Social Job")
}
