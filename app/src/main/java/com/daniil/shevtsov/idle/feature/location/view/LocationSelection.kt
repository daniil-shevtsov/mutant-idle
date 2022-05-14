package com.daniil.shevtsov.idle.feature.location.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
        description = "A place where they hide people in the ground",
    )
    LocationSelection(
        state = locationSelectionViewState(
            locations = listOf(selectedLocation),
            selectedLocation = selectedLocation,
            isExpanded = false,
        ),
        onExpandChange = {},
        onLocationSelected = {},
    )
}

@Composable
fun LocationSelection(
    state: LocationSelectionViewState,
    onExpandChange: () -> Unit,
    onLocationSelected: (id: Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .background(AppTheme.colors.background)
            .padding(4.dp)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .background(AppTheme.colors.background),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                modifier = modifier.weight(1f),
                text = "Current location:",
                fontSize = 24.sp,
                color = Color.White
            )
            Box(modifier = modifier.weight(1f)) {
                Text(
                    text = state.selectedLocation.title,
                    fontSize = 16.sp,
                    modifier = modifier
                        .fillMaxWidth()
                        .cavitary(
                            lightColor = AppTheme.colors.backgroundLight,
                            darkColor = AppTheme.colors.backgroundDark
                        )
                        .background(Color.White)
                        .clickable(onClick = { onExpandChange() })
                        .padding(4.dp)
                )
                DropdownMenu(
                    expanded = state.isExpanded,
                    modifier = modifier.wrapContentHeight(),
                    onDismissRequest = { onExpandChange() }) {
                    state.locations.forEach { location ->
                        DropdownMenuItem(
                            modifier = modifier.background(Color.White),
                            onClick = {
                                onLocationSelected(location.id)
                                onExpandChange()
                            }
                        ) {
                            Text(text = location.title)
                        }
                    }
                }
            }
        }
        Text(
            text = state.selectedLocation.description,
            fontSize = 16.sp,
            modifier = modifier
                .fillMaxWidth()
                .cavitary(
                    lightColor = AppTheme.colors.backgroundLight,
                    darkColor = AppTheme.colors.backgroundDark
                )
                .background(Color.White)
                .clickable(onClick = { onExpandChange() })
                .padding(4.dp)
        )
    }
}
