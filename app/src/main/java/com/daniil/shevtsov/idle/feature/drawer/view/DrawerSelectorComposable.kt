package com.daniil.shevtsov.idle.feature.drawer.view

import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
) {
    LazyRow {
        items(items = tabs) { tab ->
            Text(
                text = tab.title
            )
        }
    }
}