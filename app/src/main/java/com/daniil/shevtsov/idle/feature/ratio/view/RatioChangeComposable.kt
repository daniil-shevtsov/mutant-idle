package com.daniil.shevtsov.idle.feature.ratio.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.End,
    ) {
        ratioChanges.forEach { ratioChange ->
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = ratioChange.icon,
                    fontSize = 16.sp,
                )
                Text(
                    modifier = modifier.padding(start = 4.dp),
                    text = ratioChange.value,
                    color = AppTheme.colors.textLight,
                    fontSize = 16.sp,
                )
            }

        }
    }
}
