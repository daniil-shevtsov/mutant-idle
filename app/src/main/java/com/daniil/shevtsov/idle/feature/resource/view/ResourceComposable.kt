package com.daniil.shevtsov.idle.feature.resource.view

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daniil.shevtsov.idle.core.ui.Icons
import com.daniil.shevtsov.idle.core.ui.cavitary
import com.daniil.shevtsov.idle.core.ui.theme.AppTheme
import com.daniil.shevtsov.idle.core.ui.widgets.Collapsable
import com.daniil.shevtsov.idle.feature.ratio.presentation.RatioModel
import com.daniil.shevtsov.idle.feature.ratio.presentation.ratioModel
import com.daniil.shevtsov.idle.feature.ratio.view.RatioPane
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
        ratios = listOf(
            ratioModel(name = "Suspicion", percent = 50.0, icon = Icons.Suspicion),
            ratioModel(name = "Mutanity", percent = 75.0, icon = Icons.Mutanity),
        ),
        isCollapsed = false,
        onToggleCollapse = {},
    )
}

@Preview
@Composable
fun ManyResourcePanePreview() {
    ResourcePane(
        resources = listOf(
            resourceModel(name = "Blood", value = "10 000", icon = Icons.Blood),
            resourceModel(name = "Money", value = "100", icon = Icons.Money),
            resourceModel(name = "Prisoners", value = "1", icon = Icons.Prisoner),
            resourceModel(name = "Information", value = "50", icon = Icons.Information),
            resourceModel(name = "Fresh Meat", value = "100", icon = Icons.FreshMeat),
            resourceModel(name = "Familiars", value = "150", icon = Icons.Familiar),
        ),
        ratios = listOf(
            ratioModel(name = "Suspicion", percent = 50.0, icon = Icons.Suspicion),
            ratioModel(name = "Mutanity", percent = 75.0, icon = Icons.Mutanity),
        ),
        isCollapsed = false,
        onToggleCollapse = {},
    )
}

@Composable
fun ResourcePane(
    ratios: List<RatioModel>,
    resources: List<ResourceModel>,
    isCollapsed: Boolean,
    modifier: Modifier = Modifier,
    onToggleCollapse: () -> Unit,
) {
    Collapsable(
        title = "Resources and Ratios",
        isCollapsed = isCollapsed,
        collapsedContent = {
            Row {
                ResourcePanel(resource = resources[0])
            }
        },
        expandedContent = {
            Column(
                modifier = modifier
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState())
                ) {
                    resources.chunked(3)
                        .forEach { columnResources ->
                            Column(modifier = Modifier.width(IntrinsicSize.Min)) {
                                columnResources.forEach { resource ->
                                    ResourcePanel(
                                        resource = resource,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }
                }
                RatioPane(ratios)
            }
        },
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
            .background(AppTheme.colors.background)
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(text = resource.icon, style = AppTheme.typography.icon)
        Text(
            modifier = Modifier,
            text = resource.name,
            style = AppTheme.typography.title,
            color = AppTheme.colors.textLight,
        )
        Text(
            text = resource.value,
            style = AppTheme.typography.body,
            color = AppTheme.colors.textDark,
            textAlign = TextAlign.Center,
            maxLines = 1,
            modifier = Modifier
                .cavitary(
                    lightColor = AppTheme.colors.backgroundLight,
                    darkColor = AppTheme.colors.backgroundDark
                )
                .background(AppTheme.colors.backgroundText)
                .padding(4.dp)
                .weight(1f)
        )
    }
}
