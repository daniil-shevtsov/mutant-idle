package com.daniil.shevtsov.idle.feature.tagsystem.domain

data class Game(
    override val id: String,
    val lines: List<Line>,
    val locations: List<Location>,
    val locationId: String,
    val player: Player,
    val npcs: List<Npc>,
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
    tags: SpikeTags = tags(),
    plot: List<String> = emptyList(),
) = Game(
    id = id,
    locationId = locationId,
    locations = locations,
    lines = plotLines + lines,
    player = player,
    npcs = npcs,
    tags = tags,
    plot = plot,
)
