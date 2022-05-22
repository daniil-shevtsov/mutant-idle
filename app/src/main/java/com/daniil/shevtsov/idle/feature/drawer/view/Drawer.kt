package com.daniil.shevtsov.idle.feature.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daniil.shevtsov.idle.core.ui.theme.AppTheme
import com.daniil.shevtsov.idle.feature.debug.presentation.DebugComposable
import com.daniil.shevtsov.idle.feature.drawer.presentation.*
import com.daniil.shevtsov.idle.feature.drawer.view.DrawerTabSelector
import com.daniil.shevtsov.idle.feature.player.info.view.PlayerInfoComposable
import com.daniil.shevtsov.idle.feature.player.job.domain.playerJobModel
import com.daniil.shevtsov.idle.feature.tagsystem.domain.tag

@Preview(
    widthDp = 320,
    heightDp = 534,
)
@Composable
fun MainDrawerDebugPreview() {
    MainDrawer(
        drawerState = drawerViewState(
            tabSelectorState = listOf(
                drawerTab(
                    id = DrawerTabId.Debug,
                    title = "Debug",
                    isSelected = true
                ),
                drawerTab(
                    id = DrawerTabId.PlayerInfo,
                    title = "Player Info",
                    isSelected = false
                ),
            ),
            drawerContent = drawerDebugContent(
                jobSelection = listOf(
                    playerJobModel(
                        title = "Lol",
                        tags = listOf(
                            tag(name = "lol1"),
                            tag(name = "lol2"),
                            tag(name = "lol3"),
                        )
                    ),
                    playerJobModel(
                        title = "Kek",
                        tags = listOf(
                            tag(name = "kek1"),
                            tag(name = "kek2"),
                            tag(name = "kek3"),
                        )
                    ),
                )
            )
        ),
        onViewAction = {},
    )
}

@Composable
fun MainDrawer(
    drawerState: DrawerViewState,
    onViewAction: (DrawerViewAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    DrawerTabSelector(
        tabs = drawerState.tabSelectorState,
        onTabSelected = { id -> onViewAction(DrawerViewAction.TabSwitched(id = id)) },
    )
    when (val drawerContentState = drawerState.drawerContent) {
        is DrawerContentViewState.Debug -> {
            DebugComposable(
                state = drawerContentState.state,
                modifier = modifier
                    .background(AppTheme.colors.background)
                    .padding(8.dp),
                onAction = { debugAction ->
                    onViewAction(DrawerViewAction.Debug(action = debugAction))
                })
        }
        is DrawerContentViewState.PlayerInfo -> {
            PlayerInfoComposable(
                state = drawerContentState.playerInfo,
                modifier = modifier
                    .background(AppTheme.colors.background)
                    .padding(8.dp)
            )
        }
    }
}
