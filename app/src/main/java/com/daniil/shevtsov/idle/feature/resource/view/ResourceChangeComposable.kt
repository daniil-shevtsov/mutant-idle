package com.daniil.shevtsov.idle.feature.resource.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daniil.shevtsov.idle.core.ui.theme.AppTheme
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
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.paddingSmall),
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
                    style = AppTheme.typography.icon,
                )
                Text(
                    modifier = modifier.padding(start = 4.dp),
                    text = resourceChange.value,
                    style = AppTheme.typography.body,
                    color = AppTheme.colors.textLight,
                )
            }
        }
    }
}
