package com.daniil.shevtsov.idle.feature.main.presentation

import com.daniil.shevtsov.idle.core.presentation.formatting.formatEnumName
import com.daniil.shevtsov.idle.core.presentation.formatting.formatRound
import com.daniil.shevtsov.idle.core.ui.Icons
import com.daniil.shevtsov.idle.feature.action.domain.Action
import com.daniil.shevtsov.idle.feature.action.domain.RatioChanges
import com.daniil.shevtsov.idle.feature.action.presentation.ActionIcon
import com.daniil.shevtsov.idle.feature.action.presentation.ActionModel
import com.daniil.shevtsov.idle.feature.action.presentation.ActionPane
import com.daniil.shevtsov.idle.feature.action.presentation.ActionsState
import com.daniil.shevtsov.idle.feature.action.presentation.RatioChangeModel
import com.daniil.shevtsov.idle.feature.action.presentation.ResourceChangeModel
import com.daniil.shevtsov.idle.feature.coreshell.domain.GameState
import com.daniil.shevtsov.idle.feature.flavor.flavorMachine
import com.daniil.shevtsov.idle.feature.location.domain.Location
import com.daniil.shevtsov.idle.feature.location.domain.LocationSelectionState
import com.daniil.shevtsov.idle.feature.location.presentation.LocationModel
import com.daniil.shevtsov.idle.feature.location.presentation.LocationSelectionViewState
import com.daniil.shevtsov.idle.feature.player.core.domain.Player
import com.daniil.shevtsov.idle.feature.plot.domain.PlotEntry
import com.daniil.shevtsov.idle.feature.ratio.domain.Ratio
import com.daniil.shevtsov.idle.feature.ratio.domain.RatioKey
import com.daniil.shevtsov.idle.feature.ratio.presentation.RatioModel
import com.daniil.shevtsov.idle.feature.resource.domain.Resource
import com.daniil.shevtsov.idle.feature.resource.domain.ResourceChanges
import com.daniil.shevtsov.idle.feature.resource.domain.ResourceKey
import com.daniil.shevtsov.idle.feature.resource.presentation.ResourceModel
import com.daniil.shevtsov.idle.feature.shop.presentation.UpgradesViewState
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tag
import com.daniil.shevtsov.idle.feature.tagsystem.domain.TagRelation
import com.daniil.shevtsov.idle.feature.tagsystem.domain.TagRelations
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
    val resources = state.resources.filter { it.value > 0.0 }.map { resource ->
        with(resource) {
            ResourceModel(
                key = key,
                name = name,
                value = value.formatRound(),
                icon = key.chooseIcon(),
            )
        }
    }
    val ratios = state.ratios
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
        }
    val actionState = createActionState(state.actions, state.resources, state.player, state)
    val locationSelectionState = state.locationSelectionState.toViewState(
        locations = state.locations,
        playerTags = state.player.tags,
        state
    )
    val plotEntries = state.plotEntries
        .map { it.text }
        .let { entries -> countSequentialDuplicates(entries) }
        .map { group ->
            when {
                group.count > 1 -> PlotEntry(text = "${group.value} (x${group.count})")
                else -> PlotEntry(text = group.value)
            }
        }
        .map { it.copy(text = it.text.withFlavor(state)) }
    val shop = state.upgrades
        .filter { upgrade ->
            upgrade.status == UpgradeStatus.Bought || satisfiesAllTagsRelations(
                tagRelations = upgrade.tagRelations,
                tags = state.player.tags
            )
        }
        .map { upgrade ->
            with(upgrade) {
                UpgradeModel(
                    id = id,
                    title = upgrade.title.withFlavor(state),
                    subtitle = upgrade.subtitle.withFlavor(state),
                    price = PriceModel(value = price.value.toString()),
                    status = mapStatus(
                        state.resources.find { it.key == ResourceKey.Blood }?.value ?: 0.0
                    ),
                    resourceChanges = mapResourceChanges(resourceChanges),
                    ratioChanges = mapRatioChanges(ratioChanges, state.player.tags),
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
        }
    val sectionCollapse = state.sections.map { it.key to it.isCollapsed }.toMap()

    return MainViewState.Success(
        resources = resources,
        ratios = ratios,
        actionState = actionState,
        locationSelectionViewState = locationSelectionState,
        plotEntries = plotEntries,
        shop = shop,
        sectionCollapse = sectionCollapse,
    )
}

private fun countSequentialDuplicates(values: List<String>): List<Group> {
    val groups = mutableListOf<Group>()
    values.forEach {
        val last = groups.lastOrNull()
        if (last?.value == it) {
            last.count++
        } else {
            groups.add(Group(it, 1))
        }
    }
    return groups
}

data class Group(val value: String, var count: Int)

private fun RatioKey.chooseIcon(): String {
    return when (this) {
        RatioKey.Mutanity -> Icons.Mutanity
        RatioKey.Suspicion -> Icons.Suspicion
        RatioKey.Power -> Icons.Power
        RatioKey.ShipRepair -> Icons.ShipRepair
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
    ResourceKey.ControlledMind -> Icons.Familiar //TODO: Find another icon
    ResourceKey.Scrap -> Icons.Scrap
    ResourceKey.Information -> Icons.Information
    ResourceKey.Singularity -> Icons.Singularity
    ResourceKey.MainResource -> ""
}

private fun satisfiesAllTagsRelations(
    tagRelations: TagRelations,
    tags: List<Tag>,
): Boolean {
    val hasAllRequired = tagRelations[TagRelation.RequiredAll].orEmpty()
        .all { requiredTag -> requiredTag in tags }
    val requiredAny = tagRelations[TagRelation.RequiredAny]
    val hasAnyRequired =
        requiredAny == null || requiredAny.any { requiredTag -> requiredTag in tags }
    val forbiddenTags = tagRelations[TagRelation.RequiresNone]

    val tagsToRemove = tagRelations[TagRelation.Removes].orEmpty().toSet()
    val presentTagsToRemove = tags.filter { tag -> tag in tagsToRemove }

    val tagsToProvide = tagRelations[TagRelation.Provides].orEmpty().toSet()
    val tagsToProvideThatAlreadyPresent = tags.filter { tag -> tag in tagsToProvide }

    val noForbiddenTags =
        forbiddenTags == null || forbiddenTags.none { forbiddenTag -> forbiddenTag in tags }
    val removeMakesSense = (tagsToRemove.isEmpty() || presentTagsToRemove.isNotEmpty())
    val provideMakesSense =
        (tagsToProvide.isEmpty() || tagsToProvide.any { tag -> tag !in tagsToProvideThatAlreadyPresent })

    return hasAllRequired && hasAnyRequired && noForbiddenTags && removeMakesSense && provideMakesSense
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
                tagRelations = action.tagRelations,
                tags = player.tags,
            )
        }
        .filter { action ->
            action.resourceChanges.all { (resourceKey, resourceChange) ->
                val currentResource = resources.find { it.key == resourceKey }?.value
                currentResource != null && currentResource + resourceChange >= 0
            }
        }
        .map { action ->
            action.copy(
                title = action.title.withFlavor(state),
                subtitle = action.subtitle.withFlavor(state),
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
                        tagRelations[TagRelation.RequiredAll].orEmpty()
                            .contains(Tags.Form.Human) -> Icons.Human

                        else -> Icons.Monster
                    }
                ),
                resourceChanges = mapResourceChanges(resourceChanges),
                ratioChanges = mapRatioChanges(ratioChanges, state.player.tags),
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

private fun mapResourceChanges(resourceChanges: ResourceChanges) =
    resourceChanges.map { (resourceKey, changeValue) ->
        val formattedValue =
            ("+".takeIf { changeValue > 0 } ?: "") + changeValue.formatRound(digits = 2)
        ResourceChangeModel(
            icon = resourceKey.chooseIcon(),
            value = formattedValue,
        )
    }

private fun mapRatioChanges(
    ratioChanges: RatioChanges,
    tags: List<Tag>
) =
    ratioChanges.map { (ratioKey, tagChanges) ->
        val changeValue = tagChanges[emptyList()] ?: 0.0
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

private fun LocationSelectionState.toViewState(
    locations: List<Location>,
    playerTags: List<Tag>,
    state: GameState,
) =
    LocationSelectionViewState(
        locations = locations
            .filter { location ->
                satisfiesAllTagsRelations(
                    tagRelations = location.tagRelations,
                    tags = playerTags,
                )
            }
            .map { location -> location.toModel(selectedLocationId = selectedLocation.id, state) },
        selectedLocation = selectedLocation.toModel(
            selectedLocationId = selectedLocation.id,
            state
        ),
        isExpanded = isSelectionExpanded,
    )

private fun Location.toModel(
    selectedLocationId: Long,
    state: GameState,
) = LocationModel(
    id = id,
    title = title.withFlavor(state),
    subtitle = subtitle.withFlavor(state),
    isSelected = id == selectedLocationId,
)

private fun String.withFlavor(state: GameState): String = flavorMachine(
    original = this,
    flavors = state.flavors,
    tags = state.player.tags
)

interface Flavorable {
    val title: String
    val subtitle: String
    val plot: String?

    fun copy(
        title: String? = null,
        subtitle: String? = null,
        plot: String? = null,
    ): Flavorable
}
