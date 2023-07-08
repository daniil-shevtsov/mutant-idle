package com.daniil.shevtsov.idle.feature.settings.domain

data class SettingsItem(
    val key: SettingsKey,
    val title: String,
    val hint: String,
    val value: SettingsControl,
)

fun settingsItem(
    key: SettingsKey = SettingsKey.DebugEnabled,
    title: String = "",
    hint: String = "",
    value: SettingsControl = settingsControlBoolean(),
) = SettingsItem(
    key = key,
    title = title,
    hint = hint,
    value = value,
)

enum class SettingsKey {
    DebugEnabled
}
