package com.daniil.shevtsov.idle.feature.tagsystem.domain.entity

import com.daniil.shevtsov.idle.feature.tagsystem.domain.Item
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Location
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Npc
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Player
import com.daniil.shevtsov.idle.feature.tagsystem.domain.TagHolder
import com.daniil.shevtsov.idle.feature.tagsystem.domain.player
import com.daniil.shevtsov.idle.feature.tagsystem.domain.plotLines

data class Game(
    override val id: String,
    val lines: List<Line>,
    val locations: List<Location>,
    val locationId: String,
    val player: Player,
    val npcs: List<Npc>,
    val items: List<Item>,
    override val tags: SpikeTags,
    val plot: List<String>,
) : TagHolder

fun game(
    id: String = "game",
    locations: List<Location> = emptyList(),
    locationId: String = "",
    player: Player = player(),
    lines: List<Line> = emptyList(),
    npcs: List<Npc> = emptyList(),
    items: List<Item> = emptyList(),
    tags: SpikeTags = tags(),
    plot: List<String> = emptyList(),
) = Game(
    id = id,
    locationId = locationId,
    locations = locations,
    lines = plotLines + lines,
    player = player,
    npcs = npcs,
    items = items,
    tags = tags,
    plot = plot,
)
