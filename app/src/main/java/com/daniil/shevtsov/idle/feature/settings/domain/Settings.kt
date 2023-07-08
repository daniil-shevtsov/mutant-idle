package com.daniil.shevtsov.idle.feature.settings.domain

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class Settings(
    val categories: ImmutableList<SettingsCategory>,
    val settingsItems: ImmutableList<SettingsItem>,
    val selectedCategoryId: Long,
)

fun settings(
    categories: ImmutableList<SettingsCategory> = persistentListOf(),
    settingsItems: ImmutableList<SettingsItem> = persistentListOf(),
    selectedCategoryId: Long = 0L,
): Settings = Settings(
    categories = categories,
    settingsItems = settingsItems,
    selectedCategoryId = selectedCategoryId
)
