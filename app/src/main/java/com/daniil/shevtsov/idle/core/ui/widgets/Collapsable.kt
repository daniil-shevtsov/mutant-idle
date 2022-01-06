package com.daniil.shevtsov.idle.core.ui.widgets

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniil.shevtsov.idle.core.ui.Pallete

@Preview(heightDp = 400)
@Composable
fun CollapsablePreview() {
    Collapsable(
        title = "Preview",
        collapsedContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .background(Color.Red)
            )
        },
        expandedContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(Color.White)
            )
        }
    )
}

@Composable
fun <ElementType, ComposableType> CollapsableColumn(
    title: String,
    items: List<ElementType>,
    composable: @Composable (item: ElementType) -> ComposableType,
    modifier: Modifier = Modifier,
) {
    Collapsable(
        title = title,
        modifier = modifier,
        collapsedContent = {
            composable(items.first())
        },
        expandedContent = {
            Column {
                items.forEach { item ->
                    composable(item)
                }
            }
        }
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Collapsable(
    title: String,
    modifier: Modifier = Modifier,
    collapsedContent: @Composable () -> Unit,
    expandedContent: @Composable () -> Unit,
) {
    var isCollapsed by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Pallete.Red)
            .padding(4.dp)
        //.animateContentSize(),
    ) {
        Row(
            horizontalArrangement = spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier

        ) {
            CollapseButton(
                isCollapsed = isCollapsed,
                onClick = { isCollapsed = !isCollapsed }
            )
            Text(
                text = title,
                color = Color.White,
                fontSize = 24.sp,
                modifier = Modifier
            )
        }

        AnimatedContent(
            targetState = isCollapsed,
            transitionSpec = {
                if (targetState) {
                    fadeIn(initialAlpha = 1f) with fadeOut(targetAlpha = 1f)
                } else {
                    fadeIn(initialAlpha = 1f) with fadeOut(targetAlpha = 1f)
                }

            }
        ) { targetCollapsed ->
            if (targetCollapsed) {
                collapsedContent()
            } else {
                expandedContent()
            }
        }
    }
}

@Composable
fun CollapseButton(
    isCollapsed: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick, modifier = modifier
            .size(32.dp)
    ) {
        Icon(
            if (isCollapsed) Icons.Filled.ArrowDropDown else Icons.Filled.ArrowDropUp,
            contentDescription = "Collapse / Expand",
            modifier = modifier.fillMaxSize(),
            tint = Color.White,
        )
    }
}