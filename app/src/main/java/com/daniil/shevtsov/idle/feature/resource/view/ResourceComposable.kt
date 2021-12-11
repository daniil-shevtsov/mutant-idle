package com.daniil.shevtsov.idle.main.ui.resource

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniil.shevtsov.idle.core.ui.Pallete
import com.daniil.shevtsov.idle.feature.resource.presentation.ResourceModel
import com.daniil.shevtsov.idle.main.ui.innerBorder
import com.daniil.shevtsov.idle.main.ui.resourcePreviewStub

@Preview
@Composable
fun ResourcePreview() {
    ResourcePanel(
        resource = resourcePreviewStub()
    )
    ResourcePane(resources = listOf(
        resourcePreviewStub(),
        resourcePreviewStub(),
        resourcePreviewStub(),
    ))
}

@Composable
fun ResourcePane(
    resources: List<ResourceModel>
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        resources.forEach { resource ->
            ResourcePanel(resource)
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
        Text(text = resource.name, fontSize = 24.sp, color = Color.White)
        Text(
            text = resource.value, fontSize = 16.sp, modifier =
            Modifier
                .fillMaxWidth()
                .innerBorder(
                    lightColor = Pallete.LightRed,
                    darkColor = Pallete.DarkRed
                )
                .background(Color.White)
                .padding(4.dp)
        )
    }
}