package com.daniil.shevtsov.idle.feature.flavor

fun flavorMachine(original: String): String {
    return original.replace(Flavor.InvisibilityAction, "become invisible")
}
