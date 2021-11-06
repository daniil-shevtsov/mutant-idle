package com.daniil.shevtsov.idle.main.ui.resource

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniil.shevtsov.idle.main.ui.Pallete
import com.daniil.shevtsov.idle.main.ui.resourcePreview

@Preview
@Composable
fun ResourcePreview() {
    ResourceComposable(
        resource = resourcePreview()
    )
}

@Composable
fun ResourceComposable(
    resource: ResourceModel,
) {
    Row(
        modifier = Modifier
            .background(Pallete.Red)
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(text = resource.name,fontSize = 24.sp, color = Color.White)
        Text(text = resource.value,fontSize = 16.sp, modifier = Modifier.background(Color.White).padding(4.dp))
    }
}