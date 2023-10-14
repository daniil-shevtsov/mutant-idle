package com.daniil.shevtsov.idle.feature.location.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daniil.shevtsov.idle.core.ui.cavitary
import com.daniil.shevtsov.idle.core.ui.theme.AppTheme
import com.daniil.shevtsov.idle.feature.location.presentation.LocationSelectionViewState
import com.daniil.shevtsov.idle.feature.location.presentation.locationModel
import com.daniil.shevtsov.idle.feature.location.presentation.locationSelectionViewState

@Preview
@Composable
fun LocationSelectionPreview() {
    val selectedLocation = locationModel(
        title = "Graveyard",
        subtitle = "A place where they hide people in the ground",
    )
    LocationSelection(
        state = locationSelectionViewState(
            locations = listOf(selectedLocation),
            selectedLocation = selectedLocation,
            isExpanded = false,
        ),
        onExpandChange = {},
        onLocationSelected = {},
        slotBeforeLocation = {},
    )
}

@Composable
fun LocationSelection(
    state: LocationSelectionViewState,
    onExpandChange: () -> Unit,
    onLocationSelected: (id: Long) -> Unit,
    modifier: Modifier = Modifier,
    slotBeforeLocation: @Composable () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.paddingSmall),
        modifier = modifier
            .background(AppTheme.colors.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(AppTheme.colors.background),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            slotBeforeLocation()
            Box(modifier = Modifier.weight(1f)) {
                Text(
                    text = state.selectedLocation.title,
                    style = AppTheme.typography.body,
                    color = AppTheme.colors.textDark,
                    modifier = Modifier
                        .fillMaxWidth()
                        .cavitary(
                            lightColor = AppTheme.colors.backgroundLight,
                            darkColor = AppTheme.colors.backgroundDark
                        )
                        .background(AppTheme.colors.backgroundText)
                        .clickable(onClick = { onExpandChange() })
                        .padding(AppTheme.dimensions.paddingSmall)
                )
                DropdownMenu(
                    expanded = state.isExpanded,
                    modifier = Modifier.wrapContentHeight(),
                    onDismissRequest = { onExpandChange() }) {
                    state.locations.forEach { location ->
                        DropdownMenuItem(
                            modifier = Modifier.background(AppTheme.colors.backgroundText),
                            onClick = {
                                onLocationSelected(location.id)
                                onExpandChange()
                            }
                        ) {
                            Text(text = location.title, color = AppTheme.colors.textDark)
                        }
                    }
                }
            }
        }
        Text(
            text = state.selectedLocation.subtitle,
            style = AppTheme.typography.body,
            color = AppTheme.colors.textDark,
            modifier = modifier
                .fillMaxWidth()
                .cavitary(
                    lightColor = AppTheme.colors.backgroundLight,
                    darkColor = AppTheme.colors.backgroundDark
                )
                .background(AppTheme.colors.backgroundText)
                .clickable(onClick = { onExpandChange() })
                .padding(AppTheme.dimensions.paddingSmall)
        )
    }
}
