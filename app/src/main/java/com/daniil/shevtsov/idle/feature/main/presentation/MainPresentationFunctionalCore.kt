package com.daniil.shevtsov.idle.feature.main.presentation

import com.daniil.shevtsov.idle.core.presentation.formatting.formatEnumName
import com.daniil.shevtsov.idle.core.presentation.formatting.formatRound
import com.daniil.shevtsov.idle.core.ui.Icons
import com.daniil.shevtsov.idle.feature.action.domain.Action
import com.daniil.shevtsov.idle.feature.action.presentation.*
import com.daniil.shevtsov.idle.feature.coreshell.domain.GameState
import com.daniil.shevtsov.idle.feature.flavor.flavorMachine
import com.daniil.shevtsov.idle.feature.location.domain.Location
import com.daniil.shevtsov.idle.feature.location.domain.LocationSelectionState
import com.daniil.shevtsov.idle.feature.location.presentation.LocationModel
import com.daniil.shevtsov.idle.feature.location.presentation.LocationSelectionViewState
import com.daniil.shevtsov.idle.feature.player.core.domain.Player
import com.daniil.shevtsov.idle.feature.ratio.domain.Ratio
import com.daniil.shevtsov.idle.feature.ratio.domain.RatioKey
import com.daniil.shevtsov.idle.feature.ratio.presentation.RatioModel
import com.daniil.shevtsov.idle.feature.resource.domain.Resource
import com.daniil.shevtsov.idle.feature.resource.domain.ResourceKey
import com.daniil.shevtsov.idle.feature.resource.presentation.ResourceModel
import com.daniil.shevtsov.idle.feature.shop.presentation.UpgradesViewState
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tag
import com.daniil.shevtsov.idle.feature.tagsystem.domain.TagRelation
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tags
import com.daniil.shevtsov.idle.feature.upgrade.domain.Upgrade
import com.daniil.shevtsov.idle.feature.upgrade.domain.UpgradeStatus
import com.daniil.shevtsov.idle.feature.upgrade.presentation.PriceModel
import com.daniil.shevtsov.idle.feature.upgrade.presentation.UpgradeModel
import com.daniil.shevtsov.idle.feature.upgrade.presentation.UpgradeStatusModel

fun mapMainViewState(
    state: GameState
): MainViewState {
    return createMainViewState(state)
}

private fun createMainViewState(state: GameState): MainViewState {
    return MainViewState.Success(
        resources = state.resources.filter { it.value > 0.0 }.map { resource ->
            with(resource) {
                ResourceModel(
                    key = key,
                    name = name,
                    value = value.formatRound(),
                    icon = key.chooseIcon(),
                )
            }
        },
        ratios = state.ratios
            .filter { ratio -> ratio.key == RatioKey.Suspicion || ratio.key == state.player.mainRatioKey }
            .map { ratio ->
                RatioModel(
                    key = ratio.key,
                    title = formatEnumName(name = ratio.key.name),
                    name = getNameForRatio(ratio),
                    percent = ratio.value,
                    percentLabel = (ratio.value * 100).formatRound(digits = 2) + " %",
                    icon = ratio.key.chooseIcon(),
                )
            },
        actionState = createActionState(state.actions, state.resources, state.player, state),
        locationSelectionViewState = state.locationSelectionState.toViewState(playerTags = state.player.tags)
            .let { viewState ->
                viewState.copy(
                    locations = viewState.locations.map { location ->
                        location.copy(
                            description = flavorMachine(
                                original = location.description,
                                flavors = state.flavors,
                                tags = state.player.tags,
                            )
                        )
                    },
                    selectedLocation = viewState.selectedLocation.copy(
                        description = flavorMachine(
                            original = viewState.selectedLocation.description,
                            flavors = state.flavors,
                            tags = state.player.tags,
                        )
                    )
                )
            },
        shop = state.upgrades
            .filter { upgrade ->
                satisfiesAllTagsRelations(
                    tagRelations = upgrade.tags,
                    tags = state.player.tags
                )
            }
            .map { upgrade ->
                with(upgrade) {
                    UpgradeModel(
                        id = id,
                        title = title,
                        subtitle = flavorMachine(
                            original = upgrade.subtitle,
                            flavors = state.flavors,
                            tags = state.player.tags,
                        ),
                        price = PriceModel(value = price.value.toString()),
                        status = mapStatus(
                            state.resources.find { it.key == ResourceKey.Blood }?.value ?: 0.0
                        ),
                        resourceChanges = mapResourceChanges(resourceChanges),
                        ratioChanges = mapRatioChanges(ratioChanges),
                    )
                }
            }
            .sortedBy {
                when (it.status) {
                    UpgradeStatusModel.Affordable -> 0
                    UpgradeStatusModel.NotAffordable -> 1
                    UpgradeStatusModel.Bought -> 2
                }
            }
            .let { upgrades ->
                UpgradesViewState(
                    upgrades = upgrades,
                )
            },
        sectionCollapse = state.sections.map { it.key to it.isCollapsed }.toMap(),
    )
}

private fun RatioKey.chooseIcon(): String {
    return when (this) {
        RatioKey.Mutanity -> Icons.Mutanity
        RatioKey.Suspicion -> Icons.Suspicion
        RatioKey.Power -> Icons.Power
        RatioKey.ShipRepair -> Icons.ShipReapir
    }
}

private fun ResourceKey.chooseIcon() = when (this) {
    ResourceKey.Blood -> Icons.Blood
    ResourceKey.Money -> Icons.Money
    ResourceKey.HumanFood -> Icons.HumanFood
    ResourceKey.Prisoner -> Icons.Prisoner
    ResourceKey.Remains -> Icons.Remains
    ResourceKey.FreshMeat -> Icons.FreshMeat
    ResourceKey.Organs -> Icons.Organs
    ResourceKey.Familiar -> Icons.Familiar
    ResourceKey.Scrap -> Icons.Scrap
}

private fun satisfiesAllTagsRelations(
    tagRelations: Map<TagRelation, List<Tag>>,
    tags: List<Tag>,
): Boolean {
    val hasAllRequired = tagRelations[TagRelation.RequiredAll].orEmpty()
        .all { requiredTag -> requiredTag in tags }
    val requiredAny = tagRelations[TagRelation.RequiredAny]
    val hasAnyRequired =
        requiredAny == null || requiredAny.any { requiredTag -> requiredTag in tags }
    val requiredNone = tagRelations[TagRelation.RequiresNone]
    val hasNone =
        requiredNone == null || requiredNone.none { forbiddenTag -> forbiddenTag in tags }

    return hasAllRequired && hasAnyRequired && hasNone
}

private fun createActionState(
    actions: List<Action>,
    resources: List<Resource>,
    player: Player,
    state: GameState,
): ActionsState {
    val availableActions = actions
        .filter { action ->
            satisfiesAllTagsRelations(
                tagRelations = action.tags,
                tags = player.tags,
            )
        }
        .filter { action ->
            action.resourceChanges.all { (resourceKey, resourceChange) ->
                val currentResource = resources.find { it.key == resourceKey }!!.value
                currentResource + resourceChange >= 0
            }
        }
        .map { action ->
            action.copy(
                subtitle = flavorMachine(
                    original = action.subtitle,
                    flavors = state.flavors,
                    tags = player.tags,
                )
            )
        }

    val models = availableActions.map { action ->
        with(action) {
            val isActive = resourceChanges.all { (resourceKey, resourceChange) ->
                val currentResource = resources.find { it.key == resourceKey }!!.value
                currentResource + resourceChange >= 0
            }

            ActionModel(
                id = id,
                title = title,
                subtitle = subtitle,
                icon = ActionIcon(
                    value = when {
                        tags[TagRelation.RequiredAll].orEmpty()
                            .contains(Tags.HumanAppearance) -> Icons.Human
                        else -> Icons.Monster
                    }
                ),
                resourceChanges = mapResourceChanges(resourceChanges),
                ratioChanges = mapRatioChanges(ratioChanges),
                isEnabled = isActive,
            )
        }
    }
        .sortedByDescending(ActionModel::isEnabled)

    return ActionsState(
        actionPanes = listOf(
            ActionPane(
                actions = models
            ),
        ),
    )
}

private fun mapResourceChanges(resourceChanges: Map<ResourceKey, Double>) =
    resourceChanges.map { (resourceKey, changeValue) ->
        val formattedValue =
            ("+".takeIf { changeValue > 0 } ?: "") + changeValue.formatRound(digits = 2)
        ResourceChangeModel(
            icon = resourceKey.chooseIcon(),
            value = formattedValue,
        )
    }

private fun mapRatioChanges(ratioChanges: Map<RatioKey, Double>) =
    ratioChanges.map { (ratioKey, changeValue) ->
        val formattedValue =
            ("+".takeIf { changeValue > 0 } ?: "") + (changeValue * 100)
                .formatRound(digits = 2) + " %"
        RatioChangeModel(
            icon = ratioKey.chooseIcon(),
            value = formattedValue,
        )
    }

private fun getNameForRatio(ratio: Ratio) = when (ratio.key) {
    RatioKey.Mutanity -> getMutanityNameForRatio(ratio.value)
    RatioKey.Suspicion -> getSuspicionNameForRatio(ratio.value)
    RatioKey.Power -> ""
    RatioKey.ShipRepair -> ""
}

private fun getSuspicionNameForRatio(
    value: Double
): String = when {
    value < 0.15f -> "Unknown"
    value < 0.25f -> "Rumors"
    value < 0.50f -> "News"
    value < 0.80f -> "Investigation"
    else -> "Manhunt"
}

private fun getMutanityNameForRatio(mutantRatio: Double): String {
    val name = when {
        mutantRatio < 0.15 -> "Human"
        mutantRatio < 0.25 -> "Dormant"
        mutantRatio < 0.50 -> "Hidden"
        mutantRatio < 0.80 -> "Covert"
        else -> "Honest"
    }
    return name
}

private fun initViewState(): MainViewState = MainViewState.Loading

private fun Upgrade.mapStatus(resource: Double): UpgradeStatusModel {
    val statusModel = when {
        status == UpgradeStatus.Bought -> UpgradeStatusModel.Bought
        price.value <= resource -> UpgradeStatusModel.Affordable
        else -> UpgradeStatusModel.NotAffordable
    }
    return statusModel
}

private fun LocationSelectionState.toViewState(playerTags: List<Tag>) =
    LocationSelectionViewState(
        locations = allLocations
            .filter { location ->
                satisfiesAllTagsRelations(
                    tagRelations = location.tags,
                    tags = playerTags,
                )
            }
            .map { location -> location.toModel(selectedLocationId = selectedLocation.id) },
        selectedLocation = selectedLocation.toModel(selectedLocationId = selectedLocation.id),
        isExpanded = isSelectionExpanded,
    )

private fun Location.toModel(selectedLocationId: Long) = LocationModel(
    id = id,
    title = title,
    description = description,
    isSelected = id == selectedLocationId,
)
