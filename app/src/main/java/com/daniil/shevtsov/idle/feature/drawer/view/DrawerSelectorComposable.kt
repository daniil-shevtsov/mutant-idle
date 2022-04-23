package com.daniil.shevtsov.idle.feature.drawer.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.daniil.shevtsov.idle.feature.drawer.presentation.DrawerTab
import com.daniil.shevtsov.idle.feature.drawer.presentation.DrawerTabId
import com.daniil.shevtsov.idle.feature.drawer.presentation.drawerTab

@Preview
@Composable
fun DrawerSelectorPreview() {
    DrawerTabSelector(
        tabs = listOf(
            drawerTab(title = "lol"),
            drawerTab(title = "kek"),
            drawerTab(title = "cheburek"),
        ),
        onTabSelected = {},
    )
}

@Composable
fun DrawerTabSelector(
    tabs: List<DrawerTab>,
    onTabSelected: (id: DrawerTabId) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow {
        items(items = tabs) { tab ->
            Text(
                text = tab.title,
                modifier = modifier
                    .clickable { onTabSelected(tab.id) }
            )
        }
    }
}