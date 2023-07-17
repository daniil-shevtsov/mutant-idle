package com.daniil.shevtsov.idle.feature.tagsystem.domain

import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.SpikeTags
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.tags

data class Location(override val id: String, val title: String, override val tags: SpikeTags) :
    TagHolder

fun location(id: String = "location", title: String = "", tags: SpikeTags = tags()) = Location(
    id = id,
    title = title,
    tags = tags,
)

data class Container(override val id: String, val title: String, override val tags: SpikeTags) :
    TagHolder

fun container(id: String = "container", title: String = "", tags: SpikeTags = tags()) = Container(
    id = id,
    title = title,
    tags = tags,
)

data class Player(override val id: String, val title: String, override val tags: SpikeTags) :
    TagHolder

fun player(id: String = "player", title: String = "", tags: SpikeTags = tags()) = Player(
    id = id,
    title = title,
    tags = tags,
)

data class Item(override val id: String, val title: String, override val tags: SpikeTags) :
    TagHolder

fun item(id: String = "item", title: String = "", tags: SpikeTags = tags()) = Item(
    id = id,
    title = title,
    tags = tags,
)

data class Npc(override val id: String, val title: String, override val tags: SpikeTags) : TagHolder

fun npc(id: String = "npc", title: String = "", tags: SpikeTags = tags()) = Npc(
    id = id,
    title = title,
    tags = tags,
)
