package com.daniil.shevtsov.idle.feature.drawer.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.daniil.shevtsov.idle.core.ui.theme.AppTheme
import com.daniil.shevtsov.idle.feature.drawer.presentation.DrawerTab
import com.daniil.shevtsov.idle.feature.drawer.presentation.DrawerTabId
import com.daniil.shevtsov.idle.feature.drawer.presentation.drawerTab

@Preview(
    widthDp = 320,
)
@Composable
fun DrawerSelectorPreview() {
    val tabs = listOf(
        drawerTab(title = "Lol"),
        drawerTab(title = "Kek"),
        drawerTab(title = "Cheburek"),
    )
    Column {
        tabs.forEachIndexed { previewIndex, tab ->
            DrawerTabSelector(
                tabs = tabs.mapIndexed { index, tab ->
                    tab.copy(isSelected = index == previewIndex)
                },
                onTabSelected = {},
            )
        }
    }
}

@Composable
fun DrawerTabSelector(
    tabs: List<DrawerTab>,
    onTabSelected: (id: DrawerTabId) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = spacedBy(4.dp),
        modifier = modifier
            .background(AppTheme.colors.background)
            .padding(bottom = 4.dp)
            .background(AppTheme.colors.backgroundDarkest)
            .padding(top = 4.dp)
    ) {
        tabs.forEachIndexed { index, tab ->
            Text(
                text = tab.title,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = if (tab.isSelected) {
                    modifier
                        .background(AppTheme.colors.backgroundLight)
                        .let { kek ->
                            when (index) {
                                0 -> kek.padding(top = 1.dp, end = 1.dp)
                                tabs.size - 1 -> kek.padding(start = 1.dp, top = 1.dp)
                                else -> kek.padding(start = 1.dp, top = 1.dp, end = 1.dp)
                            }
                        }
                        .background(AppTheme.colors.background)
                        .clickable { onTabSelected(tab.id) }
                        .padding(4.dp)
                        .weight(1f)
                } else {
                    modifier
                        .background(AppTheme.colors.background)
                        .padding(4.dp)
                        .clickable { onTabSelected(tab.id) }
                        .weight(1f)
                }
            )
        }
    }
}
