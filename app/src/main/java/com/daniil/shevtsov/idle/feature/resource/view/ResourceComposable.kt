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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daniil.shevtsov.idle.core.ui.Icons
import com.daniil.shevtsov.idle.core.ui.cavitary
import com.daniil.shevtsov.idle.core.ui.theme.AppTheme
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
            .background(AppTheme.colors.background)
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(text = resource.icon, style = AppTheme.typography.icon)
        Text(
            modifier = modifier.weight(0.35f),
            text = resource.name,
            style = AppTheme.typography.title,
            color = AppTheme.colors.textLight,
        )
        Text(
            text = resource.value,
            style = AppTheme.typography.body,
            color = AppTheme.colors.textDark,
            modifier = modifier
                .weight(0.65f)
                .fillMaxWidth()
                .cavitary(
                    lightColor = AppTheme.colors.backgroundLight,
                    darkColor = AppTheme.colors.backgroundDark
                )
                .background(AppTheme.colors.backgroundText)
                .padding(4.dp)
        )
    }
}
