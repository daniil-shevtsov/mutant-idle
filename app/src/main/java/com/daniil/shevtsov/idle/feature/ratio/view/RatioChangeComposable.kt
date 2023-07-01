package com.daniil.shevtsov.idle.feature.ratio.view

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
import com.daniil.shevtsov.idle.feature.action.presentation.RatioChangeModel
import com.daniil.shevtsov.idle.feature.action.view.ratioChangesComposeStub

@Preview
@Composable
fun RatioChangesPreview() {
    RatioChanges(ratioChanges = ratioChangesComposeStub())
}

@Composable
fun RatioChanges(
    ratioChanges: List<RatioChangeModel>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.width(IntrinsicSize.Max),
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.paddingSmall),
        horizontalAlignment = Alignment.End,
    ) {
        ratioChanges.forEach { ratioChange ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = ratioChange.icon,
                    style = AppTheme.typography.icon,
                )
                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    text = ratioChange.value,
                    style = AppTheme.typography.body,
                    color = AppTheme.colors.textLight,
                )
            }

        }
    }
}
