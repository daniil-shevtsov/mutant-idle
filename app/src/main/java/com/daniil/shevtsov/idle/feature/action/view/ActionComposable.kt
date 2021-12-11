package com.daniil.shevtsov.idle.feature.action.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniil.shevtsov.idle.core.ui.Pallete
import com.daniil.shevtsov.idle.core.ui.actionPreviewStub
import com.daniil.shevtsov.idle.core.ui.actionStatePreviewStub
import com.daniil.shevtsov.idle.core.ui.innerBorder
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
    ActionSection(state = actionStatePreviewStub())
}

@Preview(
    widthDp = 400,
    heightDp = 400,
)
@Composable
fun ActionPanesPreviewWithOneItem() {
    ActionSection(state = ActionsState(listOf(ActionPane(listOf(actionPreviewStub())))))
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ActionSection(
    state: ActionsState,
    modifier: Modifier = Modifier,
    onActionClicked: (actionId: Long) -> Unit = {},
) {
    HorizontalPager(
        count = state.actionPanes.size,
        modifier = modifier
            .background(Pallete.Red)
            .padding(4.dp)
            .innerBorder(
                lightColor = Pallete.LightRed,
                darkColor = Pallete.DarkRed
            )
            .background(Pallete.DarkGray),
    ) { paneIndex ->
        val actionPane = state.actionPanes[paneIndex]
        ActionPane(
            pane = actionPane,
            onActionClicked = onActionClicked,
        )
    }
}

@Composable
fun ActionPane(
    pane: ActionPane,
    onActionClicked: (actionId: Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        cells = GridCells.Fixed(count = 2),
        modifier = modifier
    ) {
        items(pane.actions) { action ->
            Action(
                action = action,
                onClicked = {onActionClicked(action.id)},
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
            .padding(4.dp)
            .background(Pallete.Red)
            .padding(4.dp)
            .background(Pallete.DarkRed)
            .padding(4.dp)
            .clickable { onClicked() }
    ) {
        Text(
            text = action.title,
            color = Color.White,
            fontSize = 24.sp,
            modifier = Modifier
                .fillMaxWidth()
                .background(Pallete.DarkRed)
                .padding(4.dp)
                .padding(start = 4.dp)
        )
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