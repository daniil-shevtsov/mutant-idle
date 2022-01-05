package com.daniil.shevtsov.idle.core.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Preview(heightDp = 400)
@Composable
fun CollapsablePreview() {
    Collapsable(
        collapsedContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Red)
            )
        },
        expandedContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            )
        }
    )
}

@Composable
fun Collapsable(
    modifier: Modifier = Modifier,
    collapsedContent: @Composable () -> Unit,
    expandedContent: @Composable () -> Unit,
) {
    var isCollapsed by remember { mutableStateOf(false) }

    Row {
        IconButton(onClick = { isCollapsed = !isCollapsed }) {
            Icon(
                if (isCollapsed) Icons.Filled.ArrowDropDown else Icons.Filled.ArrowDropUp,
                contentDescription = "Collapse",
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
        }
        if (isCollapsed) {
            collapsedContent()
        } else {
            expandedContent()
        }
    }
}