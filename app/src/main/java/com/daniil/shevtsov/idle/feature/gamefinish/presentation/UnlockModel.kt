package com.daniil.shevtsov.idle.feature.gamefinish.presentation

data class UnlockModel(
    val title: String,
    val subtitle: String,
    val unlockFeatures: List<UnlockFeatureModel>,
)
