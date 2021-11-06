package com.daniil.shevtsov.idle.main.ui.resource

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.daniil.shevtsov.idle.main.ui.ResourceModel

@Preview
@Composable
fun ResourcePreview() {
    ResourceComposable(
        resource = ResourceModel(
            name = "Blood",
            value = "10 000",
        )
    )
}

@Composable
fun ResourceComposable(
    resource: ResourceModel,
) {
    Row {
        Text(text = resource.name)
        Text(text = resource.value)
    }
}