package com.daniil.shevtsov.idle.feature.gamestart.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daniil.shevtsov.idle.core.ui.Icons
import com.daniil.shevtsov.idle.core.ui.cavitary
import com.daniil.shevtsov.idle.core.ui.theme.AppTheme
import com.daniil.shevtsov.idle.feature.gamestart.presentation.TraitSelectionItem
import com.daniil.shevtsov.idle.feature.gamestart.presentation.traitSelectionItem

@Preview(
    widthDp = 650,
    heightDp = 600
)
@Composable
fun SpeciesSelectionPreview() {
    Column {
        selectedStatesOfSpecies().forEach { selection ->
            SpeciesSelection(
                selection = selection,
                onItemClicked = {},
            )
        }
    }
}

@Composable
fun SpeciesSelection(
    selection: List<TraitSelectionItem>,
    onItemClicked: (id: Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .background(AppTheme.colors.background)
            .padding(AppTheme.dimensions.paddingSmall)
            .cavitary(
                lightColor = AppTheme.colors.backgroundLight,
                darkColor = AppTheme.colors.backgroundDark
            )
            .background(AppTheme.colors.backgroundDarkest)
            .padding(AppTheme.dimensions.paddingSmall)
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            modifier = modifier
                .background(AppTheme.colors.background)
                .padding(bottom = 4.dp)
                .background(AppTheme.colors.backgroundDarkest)
                .padding(top = 4.dp)
                .height(IntrinsicSize.Max)
                .fillMaxWidth(),
        ) {
            selection.forEachIndexed { index, item ->
                Column(
                    horizontalAlignment = CenterHorizontally,
                    modifier = if (item.isSelected) {
                        modifier
                    } else {
                        modifier
                            .padding(bottom = 2.dp)
                    }
                        .clickable { onItemClicked(item.id) }
                        .width(IntrinsicSize.Min)
                ) {
                    Box(
                        modifier = modifier
                            .size(35.dp)
                            .let { modifier ->
                                if (item.isSelected) {
                                    modifier
                                        .background(AppTheme.colors.backgroundLight)
                                        .padding(start = 1.dp, top = 1.dp, end = 1.dp)
                                } else {
                                    modifier
                                }
                            }

                            .background(AppTheme.colors.background),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = when (item.isUnlocked) {
                                true -> item.icon ?: Icons.TraitDefault
                                else -> Icons.LockedCharacter
                            },
                            style = AppTheme.typography.icon,
                        )
                    }
                    Row(
                        modifier = modifier
                            .height(1.dp)
                            .fillMaxWidth()
                            .let { modifier ->
                                if (item.isSelected) {
                                    modifier.background(AppTheme.colors.backgroundLight)
                                } else {
                                    modifier
                                }
                            },
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = modifier
                                .weight(1f)
                                .height(1.dp)
                                .let { modifier ->
                                    if (item.isSelected) {
                                        modifier
                                    } else {
                                        modifier
                                            .background(AppTheme.colors.background)
                                    }
                                }
                        )
                        Box(
                            modifier = modifier
                                .width(35.dp)
                                .height(1.dp)
                                .let { modifier ->
                                    if (item.isSelected) {
                                        modifier.padding(horizontal = 1.dp)
                                    } else {
                                        modifier
                                    }
                                }
                                .background(AppTheme.colors.background)
                        )
                        Box(
                            modifier = modifier
                                .weight(1f)
                                .height(1.dp)
                                .let { modifier ->
                                    if (item.isSelected) {
                                        modifier
                                    } else {
                                        modifier
                                            .background(AppTheme.colors.background)
                                    }
                                }
                        )
                    }
                    Column(
                        modifier = modifier
                            .let { modifier ->
                                if (item.isSelected) {
                                    modifier
                                        .background(AppTheme.colors.backgroundLight)
                                        .let { kek ->
                                            when (index) {
                                                0 -> kek.padding(end = 1.dp)
                                                selection.size - 1 -> kek.padding(
                                                    start = 1.dp,
                                                )
                                                else -> kek.padding(
                                                    start = 1.dp,
                                                    end = 1.dp
                                                )
                                            }
                                        }
                                        .background(AppTheme.colors.background)
                                } else {
                                    modifier
                                }
                            }
                            .background(AppTheme.colors.background)
                            .padding(AppTheme.dimensions.paddingSmall),
                    ) {
                        Text(
                            modifier = modifier.fillMaxWidth(),
                            text = when (item.isUnlocked) {
                                true -> item.title
                                else -> "Locked"
                            },
                            textAlign = TextAlign.Center,
                            style = AppTheme.typography.title,
                            color = AppTheme.colors.textLight,
                        )
                        Text(
                            modifier = modifier
                                .fillMaxHeight()
                                .padding(AppTheme.dimensions.paddingSmall)
                                .cavitary(
                                    lightColor = AppTheme.colors.backgroundLight,
                                    darkColor = AppTheme.colors.backgroundDark
                                )
                                .background(AppTheme.colors.backgroundText)
                                .padding(AppTheme.dimensions.paddingSmall)
                                .width(110.dp),
                            text = when (item.isUnlocked) {
                                true -> item.description
                                else -> "Do X to unlock this species"
                            },
                            color = AppTheme.colors.textDark,
                            style = AppTheme.typography.body,
                        )
                    }
                }

            }
        }
    }
}

fun selectedStatesOfSpecies() = (0..2)
    .mapIndexed { index, selectedIndex ->
        speciesSelectionComposeStub(selectedIndex = selectedIndex)
    }

fun speciesSelectionComposeStub(selectedIndex: Int = 1) = listOf(
    traitSelectionItem(
        title = "Devourer",
        icon = Icons.Devourer,
        description = "You are a growing creature with insatiable hunger",
        isSelected = selectedIndex == 0,
    ),
    traitSelectionItem(
        title = "Vampire",
        icon = Icons.Vampire,
        description = "You are a bloodsucking immortal creature",
        isSelected = selectedIndex == 1,
    ),
    traitSelectionItem(
        title = "Alien",
        icon = Icons.Alien,
        description = "You have crashed on this planet and need to find a way home",
        isSelected = selectedIndex == 2,
    ),
    traitSelectionItem(
        title = "Lol",
        icon = Icons.Alien,
        description = "Kek",
        isSelected = false,
        isUnlocked = false,
    ),
)
