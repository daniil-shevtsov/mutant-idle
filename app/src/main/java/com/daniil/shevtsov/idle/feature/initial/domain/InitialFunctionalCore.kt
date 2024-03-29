package com.daniil.shevtsov.idle.feature.initial.domain

import com.daniil.shevtsov.idle.core.BalanceConfig
import com.daniil.shevtsov.idle.core.navigation.Screen
import com.daniil.shevtsov.idle.core.presentation.formatting.formatEnumName
import com.daniil.shevtsov.idle.feature.action.domain.createAllActions
import com.daniil.shevtsov.idle.feature.coreshell.domain.GameState
import com.daniil.shevtsov.idle.feature.drawer.presentation.DrawerTab
import com.daniil.shevtsov.idle.feature.drawer.presentation.DrawerTabId
import com.daniil.shevtsov.idle.feature.flavor.createFlavors
import com.daniil.shevtsov.idle.feature.gamefinish.domain.createEndings
import com.daniil.shevtsov.idle.feature.location.domain.Location
import com.daniil.shevtsov.idle.feature.location.domain.LocationSelectionState
import com.daniil.shevtsov.idle.feature.location.domain.createLocations
import com.daniil.shevtsov.idle.feature.main.domain.Selectable
import com.daniil.shevtsov.idle.feature.main.presentation.SectionKey
import com.daniil.shevtsov.idle.feature.main.presentation.SectionState
import com.daniil.shevtsov.idle.feature.player.core.domain.player
import com.daniil.shevtsov.idle.feature.player.job.domain.Jobs
import com.daniil.shevtsov.idle.feature.player.species.domain.Species
import com.daniil.shevtsov.idle.feature.player.trait.domain.TraitId
import com.daniil.shevtsov.idle.feature.ratio.domain.Ratio
import com.daniil.shevtsov.idle.feature.ratio.domain.RatioKey
import com.daniil.shevtsov.idle.feature.resource.domain.createResources
import com.daniil.shevtsov.idle.feature.settings.domain.Settings
import com.daniil.shevtsov.idle.feature.settings.domain.SettingsCategory
import com.daniil.shevtsov.idle.feature.settings.domain.SettingsControl
import com.daniil.shevtsov.idle.feature.settings.domain.SettingsItem
import com.daniil.shevtsov.idle.feature.settings.domain.SettingsKey
import com.daniil.shevtsov.idle.feature.tagsystem.domain.TagRelation
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tags
import com.daniil.shevtsov.idle.feature.unlocks.domain.UnlockState
import com.daniil.shevtsov.idle.feature.upgrade.domain.createUpgrades
import kotlinx.collections.immutable.persistentListOf

fun createInitialGameState(): GameState {
    return GameState(
        balanceConfig = createBalanceConfig(),
        selectables = createSelectables(),
        resources = createResources(),
        ratios = createInitialRatios(),
        sections = createInitialSectionState(),
        drawerTabs = createInitialDrawerTabs(),
        availableTraits = createInitialTraits(),
        plotEntries = emptyList(),
        locationSelectionState = createLocationSelectionState(),
        flavors = createFlavors(),
        player = createInitialPlayer(),
        currentScreen = Screen.Menu,
        screenStack = listOf(Screen.Menu),
        unlockState = UnlockState(
            traits = TraitId.values().associate { traitId ->
                val traits = createInitialTraits().filter { trait -> trait.traitId == traitId }

                traitId to when (traitId) {
                    TraitId.Species -> traits.associate { trait ->
                        trait.id to when (trait.id) {
                            Species.Devourer.id, Species.Vampire.id -> true
                            else -> false
                        }
                    }

                    TraitId.Job -> traits.associate { trait ->
                        trait.id to when (trait.id) {
                            Jobs.Unemployed.id -> true
                            else -> false
                        }
                    }
                }
            }
        ),
        allEndings = createEndings(),
        currentEndingId = null,
        settings = Settings(
            categories = persistentListOf(
                SettingsCategory(id = 0L, title = "General"),
                SettingsCategory(id = 1L, title = "Accessibility"),
            ),
            settingsItems = persistentListOf(
                SettingsItem(
                    key = SettingsKey.DebugEnabled,
                    title = "Debug",
                    hint = "Enables debug menu",
                    value = SettingsControl.BooleanValue(true),
                ),
                SettingsItem(
                    key = SettingsKey.ColorOverrideEnabled,
                    title = "Custom Colors",
                    hint = "Overrides colors with the colors from settings",
                    value = SettingsControl.BooleanValue(false),
                ),
                SettingsItem(
                    key = SettingsKey.BackgroundColor,
                    title = "Background Color",
                    hint = "Overrides this color with selected value",
                    value = SettingsControl.StringValue("#FF0000"),
                )
            ),
            selectedCategoryId = 0L,
        )
    )
}

fun createBalanceConfig() = BalanceConfig(
    tickRateMillis = 1L,
    resourcePerMillisecond = 0.0002,
    resourceSpentForFullMutant = 100.0,
)

private fun createSelectables(): List<Selectable> =
    createLocations() + createUpgrades() + createAllActions()

private fun createInitialRatios() = RatioKey.values().map { ratioKey ->
    Ratio(
        key = ratioKey,
        title = formatEnumName(ratioKey.name),
        value = 0.0,
    )
}

private fun createInitialSectionState() = listOf(
    SectionState(key = SectionKey.Resources, isCollapsed = false),
    SectionState(key = SectionKey.Actions, isCollapsed = false),
    SectionState(key = SectionKey.Upgrades, isCollapsed = false),
)

private fun createLocationSelectionState() = LocationSelectionState(
    selectedLocation = createLocations().filterIsInstance<Location>()
        .find { it.tagRelations[TagRelation.Provides]?.contains(Tags.Locations.Streets) == true }
        ?: createLocations().filterIsInstance<Location>().first(),
    isSelectionExpanded = false,
)

private fun createInitialDrawerTabs() = listOf(
    DrawerTab(id = DrawerTabId.PlayerInfo, title = "Player Info", isSelected = false),
    DrawerTab(id = DrawerTabId.Debug, title = "Debug", isSelected = false),
)

private fun createInitialPlayer() = player(
    generalTags = listOf(
        Tags.Form.Human,
        Tags.Knowledge.SocialNorms,
        Tags.Nimble,
    ),
    traits = mapOf(
        TraitId.Job to Jobs.Unemployed,
        TraitId.Species to Species.Devourer,
    )
)

fun createInitialTraits() = listOf(
    Jobs.Unemployed,
    Jobs.Mortician,
    Jobs.Gravedigger,
    Jobs.Butcher,
    Jobs.ScrapyardMechanic,
    Species.Devourer,
    Species.Vampire,
    Species.Shapeshifter,
    Species.Parasite,
    Species.Demon,
    Species.Alien,
    Species.Android,
)
