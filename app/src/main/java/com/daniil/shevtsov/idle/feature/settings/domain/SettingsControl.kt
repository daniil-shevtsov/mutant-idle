package com.daniil.shevtsov.idle.feature.settings.domain

sealed interface SettingsControl {
    data class BooleanValue(val isEnabled: Boolean) : SettingsControl
    data class StringValue(val text: String) : SettingsControl
    //TODO: Add color
}

fun settingsControlBoolean(
    isEnabled: Boolean = false
) = SettingsControl.BooleanValue(isEnabled = isEnabled)

fun settingsControlString(
    text: String = ""
) = SettingsControl.StringValue(text = text)
