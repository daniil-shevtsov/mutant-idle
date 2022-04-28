package com.daniil.shevtsov.idle.feature.tagsystem.domain

object Tags {
    val Hypnosis = Tag(name = "Hypnosis")
    val Heliophobia = Tag(name = "Heliophobia")
    val Immortal = Tag(name = "Immortal")
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

    val Surgeon = Tag(name = "Surgeon")
    val FreshCorpseAccess = Tag(name = "Fresh Corpse Access")
    val SolitaryJob = Tag(name = "Solitary Job")
    val IncineratorAccess = Tag(name = "Incinerator Access")
    val CorpseAccess = Tag(name = "Corpse Access")
    val MeatAccess = Tag(name = "Meat Access")
    val GraveyardAccess = Tag(name = "Graveyard Access")
    val LaborIntensive = Tag(name = "Labor intensive job")
    val SocialJob = Tag(name = "Social Job")
}
