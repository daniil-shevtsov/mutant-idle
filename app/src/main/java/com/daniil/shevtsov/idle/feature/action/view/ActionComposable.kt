package com.daniil.shevtsov.idle.feature.action.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniil.shevtsov.idle.core.ui.*
import com.daniil.shevtsov.idle.core.ui.widgets.Collapsable
import com.daniil.shevtsov.idle.feature.action.presentation.ActionIcon
import com.daniil.shevtsov.idle.feature.action.presentation.ActionModel
import com.daniil.shevtsov.idle.feature.action.presentation.ActionPane
import com.daniil.shevtsov.idle.feature.action.presentation.ActionsState
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager

@Preview
@Composable
fun ActionPreview() {
    Action(action = actionPreviewStub())
}

@Preview(
    widthDp = 400,
    heightDp = 400,
)
@Composable
fun ActionPanesPreview() {
    ActionSection(
        state = actionStatePreviewStub(),
        isCollapsed = false,
        onToggleCollapse = {},
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ActionSection(
    state: ActionsState,
    isCollapsed: Boolean,
    modifier: Modifier = Modifier,
    onToggleCollapse: () -> Unit,
    onActionClicked: (actionId: Long) -> Unit = {},
) {
    val actionPanes = listOf(state.humanActionPane, state.mutantActionPane)

    Collapsable(
        title ="Actions",
        isCollapsed = isCollapsed,
        collapsedContent = {
            Text(
                text = "Actions",
                color = Color.White,
                fontSize = 24.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Pallete.Red)
                    .protrusive(
                        lightColor = Pallete.LightRed,
                        darkColor = Pallete.DarkRed
                    )
                    .background(Pallete.Red)
                    .padding(4.dp)
                    .cavitary(
                        lightColor = Pallete.LightRed,
                        darkColor = Pallete.DarkRed
                    )
                    .background(Pallete.DarkRed)
                    .padding(4.dp)
            )
        },
        expandedContent = {
            HorizontalPager(
                count = actionPanes.size,
                modifier = modifier,
            ) { paneIndex ->
                val actionPane = actionPanes[paneIndex]
                ActionPane(
                    pane = actionPane,
                    onActionClicked = onActionClicked,
                )
            }
        },
        onToggleCollapse = onToggleCollapse,
    )
}

@Composable
fun ActionPane(
    pane: ActionPane,
    onActionClicked: (actionId: Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        cells = GridCells.Fixed(count = 2),
        modifier = modifier,
        contentPadding = PaddingValues(4.dp)
    ) {
        items(pane.actions) { action ->
            Action(
                action = action,
                onClicked = { onActionClicked(action.id) },
                modifier = modifier,
            )
        }
    }
}

@Composable
fun Action(
    action: ActionModel,
    modifier: Modifier = Modifier,
    onClicked: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .background(Pallete.Red)
            .padding(4.dp)
            .background(Pallete.DarkRed)
            .padding(4.dp)
            .clickable { onClicked() },
        verticalArrangement = spacedBy(4.dp)
    ) {
        val actionIcon = when (action.icon) {
            ActionIcon.Human -> "\uD83D\uDE42"
            ActionIcon.Mutant -> "\uD83D\uDC79"
        }

        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = spacedBy(8.dp)
        ) {
            Text(text = actionIcon, fontSize = 24.sp)
            Text(
                text = action.title,
                color = Color.White,
                fontSize = 24.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Pallete.DarkRed)
            )
        }

        Text(
            text = action.subtitle,
            color = Color.Black,
            fontSize = 16.sp,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(4.dp)
                .padding(start = 4.dp)
                .padding(bottom = 4.dp)
        )
    }
}