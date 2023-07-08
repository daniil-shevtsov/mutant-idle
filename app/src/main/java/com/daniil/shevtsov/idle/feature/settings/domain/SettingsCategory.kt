package com.daniil.shevtsov.idle.feature.settings.domain

data class SettingsCategory(
    val id: Long,
    val title: String,
)

fun settingsCategory(
    id: Long = 0L,
    title: String = "",
) = SettingsCategory(
    id = id,
    title = title,
)
