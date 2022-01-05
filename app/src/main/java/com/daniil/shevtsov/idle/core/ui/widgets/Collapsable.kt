package com.daniil.shevtsov.idle.core.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniil.shevtsov.idle.core.ui.Pallete
import com.daniil.shevtsov.idle.core.ui.cavitary
import com.daniil.shevtsov.idle.core.ui.protrusive

@Preview(heightDp = 400)
@Composable
fun CollapsablePreview() {
    Collapsable(
        title = "Preview",
        collapsedContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Red)
            )
        },
        expandedContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
            )
        }
    )
}

@Composable
fun Collapsable(
    title: String,
    modifier: Modifier = Modifier,
    collapsedContent: @Composable () -> Unit,
    expandedContent: @Composable () -> Unit,
) {
    var isCollapsed by remember { mutableStateOf(true) }

    Row {
        CollapseButton(
            isCollapsed = isCollapsed,
            onClick = { isCollapsed = !isCollapsed }
        )
        if (isCollapsed) {
            Row(
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
            ) {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 24.sp,
                    modifier = Modifier
                )
                collapsedContent()
            }
        } else {
            expandedContent()
        }
    }
}

@Composable
fun CollapseButton(
    isCollapsed: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(
            if (isCollapsed) Icons.Filled.ArrowDropDown else Icons.Filled.ArrowDropUp,
            contentDescription = "Collapse / Expand",
            modifier = modifier.size(ButtonDefaults.IconSize),
        )
    }
}