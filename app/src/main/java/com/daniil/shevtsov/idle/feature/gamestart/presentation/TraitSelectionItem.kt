package com.daniil.shevtsov.idle.feature.gamestart.presentation

data class TraitSelectionItem(
    val id: Long,
    val title: String,
    val description: String,
    val icon: String,
    val isSelected: Boolean,
    val isUnlocked: Boolean,
)

fun traitSelectionItem(
    id: Long = 0L,
    title: String = "",
    description: String = "",
    icon: String = "",
    isSelected: Boolean = false,
    isUnlocked: Boolean = true,
) = TraitSelectionItem(
    id = id,
    title = title,
    description = description,
    icon = icon,
    isSelected = isSelected,
    isUnlocked = isUnlocked,
)
