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
import com.daniil.shevtsov.idle.core.ui.widgets.TitleWithProgress
import com.daniil.shevtsov.idle.feature.ratio.presentation.RatioModel
import com.daniil.shevtsov.idle.feature.ratio.presentation.ratioModel
import com.daniil.shevtsov.idle.feature.resource.domain.ResourceKey
import com.daniil.shevtsov.idle.feature.resource.domain.resourceModel
import com.daniil.shevtsov.idle.feature.resource.presentation.ResourceModel

@Preview
@Composable
fun ResourcePreview() {
    ResourceRow(
        resource = resourceModel(name = "Blood", value = "10 000", icon = Icons.Blood),
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
            resourceModel(name = "Organs", value = "10", icon = Icons.Organs),
        ),
        ratios = listOf(
            ratioModel(title = "Suspicion", name = "Wanted", percent = 0.5, icon = Icons.Suspicion),
            ratioModel(title = "Mutanity", name = "Honest", percent = 0.25, icon = Icons.Mutanity),
        ),
        isCollapsed = false,
        onToggleCollapse = {},
    )
}

@Preview
@Composable
fun Debug() {
    ResourcePane(
        resources = listOf(
            resourceModel(key= ResourceKey.Blood, name="Blood", value="4", icon="🩸"),
            resourceModel(key=ResourceKey.Money, name="Money", value="100", icon="💰"),
            resourceModel(key=ResourceKey.FreshMeat, name="Fresh Meat", value="13", icon="🥩"),
            resourceModel(key=ResourceKey.ControlledMind, name="Controlled Mind", value="3", icon="🙁"),
            resourceModel(key=ResourceKey.Information, name="Information", value="1", icon="📝")
        ),
        ratios = listOf(
            ratioModel(title = "Suspicion", name = "Wanted", percent = 0.5, icon = Icons.Suspicion),
            ratioModel(title = "Mutanity", name = "Honest", percent = 0.25, icon = Icons.Mutanity),
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
                ResourceRow(resource = resources[0])
            }
        },
        expandedContent = {
            Column(
                modifier = modifier
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.paddingSmall)
            ) {
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState())
                ) {
                    resources.chunked(3)
                        .forEach { columnResources ->
                            Column(modifier = Modifier.width(IntrinsicSize.Min)) {
                                columnResources.forEach { resource ->
                                    ResourceRow(
                                        resource = resource,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }
                }
                Column {
                    Row {
                        ratios.chunked(2)
                            .map { it.first() to it.getOrNull(1) }
                            .forEach { (leftRatio, rightRatio) ->
                                val ratioModifier = Modifier.weight(1f)
                                leftRatio.let { model ->
                                    TitleWithProgress(
                                        title = "",
                                        name = model.percentLabel + " " + model.name,
                                        progress = model.percent.toFloat(),
                                        icon = model.icon,
                                        modifier = ratioModifier
                                    )
                                }
                                rightRatio?.let { model ->
                                    TitleWithProgress(
                                        title = "",
                                        name = model.percentLabel + " " + model.name,
                                        progress = model.percent.toFloat(),
                                        icon = model.icon,
                                        modifier = ratioModifier
                                    )
                                }
                            }
                    }
                }
            }
        },
        onToggleCollapse = onToggleCollapse,
    )
}

@Composable
fun ResourceRow(
    resource: ResourceModel,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .background(AppTheme.colors.background),
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
                .padding(AppTheme.dimensions.paddingSmall)
                .width(IntrinsicSize.Max)
                .weight(1f)
        )
    }
}
