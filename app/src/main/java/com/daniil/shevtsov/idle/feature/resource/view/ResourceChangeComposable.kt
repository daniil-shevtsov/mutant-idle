package com.daniil.shevtsov.idle.feature.resource.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniil.shevtsov.idle.feature.action.presentation.ResourceChangeModel
import com.daniil.shevtsov.idle.feature.action.view.resourceChangesComposeStub

@Preview
@Composable
fun ResourceChangesPreview() {
    ResourceChanges(resourceChanges = resourceChangesComposeStub())
}

@Composable
fun ResourceChanges(
    resourceChanges: List<ResourceChangeModel>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.width(IntrinsicSize.Max),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        resourceChanges.forEach { resourceChange ->
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = resourceChange.icon,
                    fontSize = 16.sp,
                )
                Text(
                    modifier = modifier.padding(start = 4.dp),
                    text = resourceChange.value,
                    color = Color.White,
                    fontSize = 16.sp,
                )
            }

        }
    }
}
