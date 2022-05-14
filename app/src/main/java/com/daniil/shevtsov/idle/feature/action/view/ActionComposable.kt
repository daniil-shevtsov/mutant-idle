package com.daniil.shevtsov.idle.feature.action.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
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
import com.daniil.shevtsov.idle.core.ui.Icons
import com.daniil.shevtsov.idle.core.ui.Pallete
import com.daniil.shevtsov.idle.core.ui.widgets.Collapsable
import com.daniil.shevtsov.idle.feature.action.domain.actionModel
import com.daniil.shevtsov.idle.feature.action.presentation.ActionModel
import com.daniil.shevtsov.idle.feature.action.presentation.ActionsState
import com.daniil.shevtsov.idle.feature.action.presentation.ratioChangeModel
import com.daniil.shevtsov.idle.feature.action.presentation.resourceChangeModel
import com.daniil.shevtsov.idle.feature.main.presentation.actionPane
import com.daniil.shevtsov.idle.feature.main.presentation.actionsState
import com.daniil.shevtsov.idle.feature.ratio.view.RatioChanges
import com.daniil.shevtsov.idle.feature.resource.view.ResourceChanges
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager

@Preview
@Composable
fun ActionPreview() {
    Column {
        Action(action = actionComposeStub(isEnabled = true))
        Action(action = actionComposeStub(isEnabled = false))
    }
}

@Preview(
    widthDp = 400,
    heightDp = 400,
)
@Composable
fun MutantActionPanePreview() {
    ActionSection(
        state = actionsState(
            actionPanes = listOf(
                actionPane(
                    actions = listOf(
                        actionComposeStub(),
                        actionComposeStub(),
                    )
                )
            )
        ),
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
    val actionPanes = state.actionPanes

    Collapsable(
        title = "Actions",
        isCollapsed = isCollapsed,
        modifier = modifier,
        collapsedContent = { },
        expandedContent = {
            HorizontalPager(
                count = actionPanes.size,
                verticalAlignment = Alignment.Top,
                modifier = modifier
                    .fillMaxSize(),
            ) { paneIndex ->
                val actionPane = actionPanes[paneIndex]
                Column(
                    verticalArrangement = Arrangement.Top,
                    modifier = modifier.fillMaxSize(),
                ) {
                    LazyVerticalGrid(
                        cells = GridCells.Fixed(count = 2),
                        modifier = modifier,
                        contentPadding = PaddingValues(
                            start = 2.dp,
                            top = 2.dp,
                            end = 2.dp,
                            bottom = 30.dp
                        )
                    ) {
                        items(actionPane.actions) { action ->
                            Action(
                                action = action,
                                onClicked = { onActionClicked(action.id) },
                                modifier = modifier.padding(2.dp), //TODO: Add item spacing after comopse update
                            )
                        }
                    }
                }
            }
        },
        onToggleCollapse = onToggleCollapse,
    )
}

//TODO: Make items the same height
@Composable
fun Action(
    action: ActionModel,
    modifier: Modifier = Modifier,
    onClicked: () -> Unit = {},
) {
    val colorAlpha = when (action.isEnabled) {
        true -> 1f
        false -> 0.5f
    }
    val lightColor = Pallete.Background.copy(alpha = colorAlpha)
    val darkColor = Pallete.BackgroundDark.copy(alpha = colorAlpha)

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
        val actionIcon = action.icon.value

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
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = SpaceBetween
        ) {
            ResourceChanges(
                resourceChanges = action.resourceChanges,
                modifier = modifier,
            )
            RatioChanges(
                ratioChanges = action.ratioChanges,
                modifier = modifier,
            )
        }
    }

}

fun actionComposeStub(
    isEnabled: Boolean = true,
) = actionModel(
    title = "Eat human food",
    subtitle = "It's not enough",
    resourceChanges = resourceChangesComposeStub(),
    ratioChanges = ratioChangesComposeStub(),
    isEnabled = isEnabled,
)

fun resourceChangesComposeStub() = listOf(
    resourceChangeModel(icon = Icons.Blood, value = "2.0"),
    resourceChangeModel(icon = Icons.HumanFood, value = "-1.0"),
)

fun ratioChangesComposeStub() = listOf(
    ratioChangeModel(icon = Icons.Mutanity, value = "10.0"),
    ratioChangeModel(icon = Icons.Suspicion, value = "-5.0"),
)
