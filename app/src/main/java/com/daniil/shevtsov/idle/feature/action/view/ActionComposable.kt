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
import com.daniil.shevtsov.idle.core.ui.Pallete
import com.daniil.shevtsov.idle.core.ui.actionPreviewStub
import com.daniil.shevtsov.idle.core.ui.actionStatePreviewStub
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
    Column {
        Action(action = actionPreviewStub(isActive = true))
        Action(action = actionPreviewStub(isActive = false))
    }
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
        title = "Actions",
        isCollapsed = isCollapsed,
        modifier = modifier,
        collapsedContent = { },
        expandedContent = {
            HorizontalPager(
                count = actionPanes.size,
                modifier = modifier,
            ) { paneIndex ->
                val actionPane = actionPanes[paneIndex]
                ActionPane(
                    pane = actionPane,
                    modifier = modifier,
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
        contentPadding = PaddingValues(2.dp)
    ) {
        items(pane.actions) { action ->
            Action(
                action = action,
                onClicked = { onActionClicked(action.id) },
                modifier = modifier.padding(2.dp), //TODO: Add item spacing after comopse update
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
    val colorAlpha = when (action.isActive) {
        true -> 1f
        false -> 0.5f
    }
    val lightColor = Pallete.Red.copy(alpha = colorAlpha)
    val darkColor = Pallete.DarkRed.copy(alpha = colorAlpha)

    Column(
        modifier = modifier
            .background(Color.LightGray)
            .background(lightColor)
            .padding(4.dp)
            .background(darkColor)
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