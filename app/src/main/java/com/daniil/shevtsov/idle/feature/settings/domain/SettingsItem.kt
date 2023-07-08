package com.daniil.shevtsov.idle.feature.settings.domain

data class SettingsItem(
    val key: SettingsKey,
    val title: String,
    val hint: String,
    val value: SettingsControl,
)

enum class SettingsKey {
    DebugEnabled
}
