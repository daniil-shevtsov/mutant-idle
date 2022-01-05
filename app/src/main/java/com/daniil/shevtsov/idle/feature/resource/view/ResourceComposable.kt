package com.daniil.shevtsov.idle.feature.resource.view

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniil.shevtsov.idle.core.ui.Pallete
import com.daniil.shevtsov.idle.core.ui.cavitary
import com.daniil.shevtsov.idle.core.ui.resourcePreviewStub
import com.daniil.shevtsov.idle.feature.resource.presentation.ResourceModel

@Preview
@Composable
fun ResourcePreview() {
    ResourcePanel(
        resource = resourcePreviewStub()
    )
}

@Preview
@Composable
fun ResourcePanePreview() {
    ResourcePane(
        resources = listOf(
            resourcePreviewStub(),
            resourcePreviewStub(),
            resourcePreviewStub(),
        )
    )
}

@Composable
fun ResourcePane(
    resources: List<ResourceModel>
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
        if (!isCollapsed) {
            Column {
                resources.forEach { resource ->
                    ResourcePanel(resource)
                }
            }
        } else {
            Text("Resources")
        }
    }

}

@Composable
fun ResourcePanel(
    resource: ResourceModel,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Pallete.Red)
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            modifier = Modifier.weight(0.35f),
            text = resource.name,
            fontSize = 24.sp,
            color = Color.White
        )
        Text(
            text = resource.value,
            fontSize = 16.sp,
            modifier = Modifier
                .weight(0.65f)
                .fillMaxWidth()
                .cavitary(
                    lightColor = Pallete.LightRed,
                    darkColor = Pallete.DarkRed
                )
                .background(Color.White)
                .padding(4.dp)
        )
    }
}