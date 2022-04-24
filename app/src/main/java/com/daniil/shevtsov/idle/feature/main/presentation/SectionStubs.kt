package com.daniil.shevtsov.idle.feature.main.presentation

fun sectionState(
    key: SectionKey = SectionKey.Resources,
    isCollapsed: Boolean = false,
) = SectionState(
    key = key,
    isCollapsed = isCollapsed
)
