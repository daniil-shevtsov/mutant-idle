package com.daniil.shevtsov.idle.feature.action.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daniil.shevtsov.idle.core.ui.Icons
import com.daniil.shevtsov.idle.core.ui.theme.AppTheme
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

@Preview(heightDp = 400)
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

@Preview
@Composable
fun UnevenActionsPanePreview() {
    ActionSection(
        state = actionsState(
            actionPanes = listOf(
                actionPane(
                    actions = unevenIconsActionComposeStub() + unevenTitleActionComposeStub() + unevenDescriptionActionComposeStub()
                )
            )
        ),
        isCollapsed = false,
        onToggleCollapse = {},
    )
}

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
            val actionPane = actionPanes.firstOrNull()
            if (actionPane != null) {
                LazyColumn(
                    verticalArrangement = Arrangement.Top,
                    modifier = Modifier.padding(2.dp)
                ) {
                    items(
                        actionPane.actions.chunked(2)
                            .map { it[0] to it.getOrNull(1) }
                    ) { (leftAction, rightAction) ->
                        Row(
                            horizontalArrangement = spacedBy(4.dp),
                            modifier = Modifier.height(IntrinsicSize.Min)
                        ) {
                            Action(
                                action = leftAction,
                                onClicked = { onActionClicked(leftAction.id) },
                                modifier = Modifier.fillMaxHeight().weight(1f),
                            )
                            if (rightAction != null) {
                                Action(
                                    action = rightAction,
                                    onClicked = { onActionClicked(rightAction.id) },
                                    modifier = Modifier.fillMaxHeight().weight(1f),
                                )
                            }
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
    val lightColor = AppTheme.colors.background.copy(alpha = colorAlpha)
    val darkColor = AppTheme.colors.backgroundDark.copy(alpha = colorAlpha)
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
            Text(text = actionIcon, style = AppTheme.typography.icon)
            Text(
                text = action.title,
                style = AppTheme.typography.title,
                color = AppTheme.colors.textLight,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }

        Text(
            text = action.subtitle,
            style = AppTheme.typography.subtitle,
            color = AppTheme.colors.textDark,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(AppTheme.colors.backgroundText)
                .padding(4.dp)
                .padding(start = 4.dp)
                .padding(bottom = 4.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = SpaceBetween
        ) {
            ResourceChanges(
                resourceChanges = action.resourceChanges,
            )
            RatioChanges(
                ratioChanges = action.ratioChanges,
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

fun unevenIconsActionComposeStub(
) = listOf(
    actionModel(
        title = "Action Title",
        subtitle = "Action description",
        resourceChanges = listOf(
            resourceChangeModel(icon = Icons.Blood, value = "2.0"),
        ),
        ratioChanges = emptyList(),
        isEnabled = true,
    ),
    actionModel(
        title = "Action Title",
        subtitle = "Action description",
        resourceChanges = listOf(
            resourceChangeModel(icon = Icons.Blood, value = "2.0"),
            resourceChangeModel(icon = Icons.HumanFood, value = "-1.0"),
        ),
        ratioChanges = emptyList(),
        isEnabled = true,
    )
)

fun unevenTitleActionComposeStub(
) = listOf(
    actionModel(
        title = "Action Title\nAction Title\nAction Title",
        subtitle = "Action description",
        resourceChanges = emptyList(),
        ratioChanges = emptyList(),
        isEnabled = true,
    ),
    actionModel(
        title = "Action Title",
        subtitle = "Action description",
        resourceChanges = emptyList(),
        ratioChanges = emptyList(),
        isEnabled = true,
    )
)

fun unevenDescriptionActionComposeStub(
) = listOf(
    actionModel(
        title = "Action Title",
        subtitle = "Action description",
        resourceChanges = emptyList(),
        ratioChanges = emptyList(),
        isEnabled = true,
    ),
    actionModel(
        title = "Action Title",
        subtitle = "Action description\nAction description\nAction description",
        resourceChanges = emptyList(),
        ratioChanges = emptyList(),
        isEnabled = true,
    )
)

fun resourceChangesComposeStub() = listOf(
    resourceChangeModel(icon = Icons.Blood, value = "2.0"),
    resourceChangeModel(icon = Icons.HumanFood, value = "-1.0"),
)

fun ratioChangesComposeStub() = listOf(
    ratioChangeModel(icon = Icons.Mutanity, value = "10.0"),
    ratioChangeModel(icon = Icons.Suspicion, value = "-5.0"),
)
