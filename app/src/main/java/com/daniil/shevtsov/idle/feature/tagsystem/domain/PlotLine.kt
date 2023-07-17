package com.daniil.shevtsov.idle.feature.tagsystem.domain

typealias Plot = String

data class Line(
    override val id: String,
    override val requiredTags: SpikeTags,
    val entry: LineEntry,
) : TagEntry {
    override val tagChanges: SpikeTags
        get() = entry.tagChanges
    override val weight: Float
        get() = entry.weight
}

fun line(
    id: String = "",
    requiredTags: SpikeTags,
    entry: LineEntry,
) = Line(id, requiredTags, entry)

data class LineEntry(
    override val id: String,
    override val tagChanges: SpikeTags,
    val plot: Plot,
    val weight: Float,
) : TagChanger

fun entry(
    plot: String,
    id: String = "",
    tagChanges: SpikeTags = mapOf(),
    weight: Float = 1.0f
): LineEntry = LineEntry(
    id = id,
    tagChanges = tagChanges,
    plot = plot,
    weight = weight,
)

data class DialogLine(
    override val id: String,
    val text: String,
    override val requiredTags: SpikeTags,
    override val tagChanges: SpikeTags,
) : TagRequirer, TagChanger

fun dialogLine(
    id: String = "",
    text: String = "",
    requiredTags: SpikeTags = tags(),
    tagChanges: SpikeTags = tags(),
) = DialogLine(
    id = id,
    text = text,
    requiredTags = requiredTags,
    tagChanges = tagChanges
).let { dialogLine ->
    line(
        id = dialogLine.id,
        requiredTags = dialogLine.requiredTags,
        entry = entry(
            plot = dialogLine.text,
            id = dialogLine.id,
            tagChanges = dialogLine.tagChanges,
            weight = 1.0f,
        )
    )
}
