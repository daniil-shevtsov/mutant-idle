package com.daniil.shevtsov.idle.feature.resource.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.daniil.shevtsov.idle.core.ui.cavitary
import com.daniil.shevtsov.idle.core.ui.widgets.CollapsableColumn
import com.daniil.shevtsov.idle.feature.resource.domain.resourceModel
import com.daniil.shevtsov.idle.feature.resource.presentation.ResourceModel

@Preview
@Composable
fun ResourcePreview() {
    ResourcePanel(
        resource = resourceModel(name = "Blood", value = "10 000", icon = Icons.Blood),
    )
}

@Preview
@Composable
fun ResourcePanePreview() {
    ResourcePane(
        resources = listOf(
            resourceModel(name = "Blood", value = "10 000", icon = Icons.Blood),
            resourceModel(name = "Money", value = "100", icon = Icons.Money),
            resourceModel(name = "Prisoners", value = "1", icon = Icons.Prisoner),
        ),
        isCollapsed = false,
        onToggleCollapse = {},
    )
}

@Composable
fun ResourcePane(
    resources: List<ResourceModel>,
    isCollapsed: Boolean,
    modifier: Modifier = Modifier,
    onToggleCollapse: () -> Unit,
) {
    CollapsableColumn(
        title = "Resources",
        isCollapsed = isCollapsed,
        items = resources,
        modifier = modifier,
        composable = { ResourcePanel(resource = it, modifier = modifier) },
        onToggleCollapse = onToggleCollapse,
    )
}

@Composable
fun ResourcePanel(
    resource: ResourceModel,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Pallete.Background)
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(text = resource.icon, fontSize = 24.sp)
        Text(
            modifier = modifier.weight(0.35f),
            text = resource.name,
            fontSize = 24.sp,
            color = Color.White
        )
        Text(
            text = resource.value,
            fontSize = 16.sp,
            modifier = modifier
                .weight(0.65f)
                .fillMaxWidth()
                .cavitary(
                    lightColor = Pallete.BackgroundLight,
                    darkColor = Pallete.BackgroundDark
                )
                .background(Color.White)
                .padding(4.dp)
        )
    }
}
