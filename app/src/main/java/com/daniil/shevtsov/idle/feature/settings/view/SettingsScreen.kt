package com.daniil.shevtsov.idle.feature.settings.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daniil.shevtsov.idle.core.ui.protrusive
import com.daniil.shevtsov.idle.core.ui.theme.AppTheme
import com.daniil.shevtsov.idle.core.ui.widgets.Cavity
import com.daniil.shevtsov.idle.core.ui.widgets.CavityButton
import com.daniil.shevtsov.idle.feature.main.presentation.MainViewAction
import com.daniil.shevtsov.idle.feature.settings.domain.SettingsKey
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Preview
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(
        state = settingsViewStateComposeStub(),
        onAction = {},
    )
}

fun settingsViewStateComposeStub(): SettingsViewState {
    return SettingsViewState(
        categories = persistentListOf(
            SettingsCategoryModel(id = 0L, title = "General", isSelected = true),
            SettingsCategoryModel(id = 0L, title = "Accessibility", isSelected = false),
            SettingsCategoryModel(id = 0L, title = "Misc", isSelected = false),
        ),
        settingsPanel = SettingsPanelModel(
            items = persistentListOf(
                settingsItemSwitchComposeStub(title = "lol", isSelected = true),
                settingsItemSwitchComposeStub(title = "kek", isSelected = false),
                settingsItemSwitchComposeStub(title = "cheburek", isSelected = true),
                settingsItemTextInputComposeStub(
                    title = "lol color",
                    text = "#",
                ),
                settingsItemSwitchComposeStub(title = "lol2", isSelected = true),
                settingsItemSwitchComposeStub(title = "kek2", isSelected = false),
                settingsItemTextInputComposeStub(
                    title = "cheburek color",
                    text = "#00FF00",
                ),
            )
        )
    )
}

fun settingsItemSwitchComposeStub(
    key: SettingsKey = SettingsKey.DebugEnabled,
    title: String = "",
    isSelected: Boolean = false,
) = SettingsItemModel.Switch(key = key, title = title, isSelected = isSelected)

fun settingsItemTextInputComposeStub(
    key: SettingsKey = SettingsKey.DebugEnabled,
    title: String = "",
    text: String = "",
) = SettingsItemModel.ColorSelector(
    key = key,
    title = title,
    currentColor = SettingsColor(text)
)

data class SettingsViewState(
    val categories: ImmutableList<SettingsCategoryModel>,
    val settingsPanel: SettingsPanelModel,
)

@Composable
fun SettingsScreen(
    state: SettingsViewState,
    onAction: (action: MainViewAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = spacedBy(AppTheme.dimensions.paddingM),
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.colors.background),
    ) {
        SettingsCategories(state.categories)
        SettingsPanel(
            model = state.settingsPanel,
            onSwitch = { key -> onAction(MainViewAction.SettingsSwitchUpdate(key)) },
            modifier = Modifier.padding(AppTheme.dimensions.paddingSmall)
        )
    }
}

@Composable
fun SettingsCategories(
    categories: ImmutableList<SettingsCategoryModel>,
) {
    Cavity(mainColor = AppTheme.colors.background) {
        Column(
            verticalArrangement = spacedBy(AppTheme.dimensions.paddingSmall),
            modifier = Modifier
                .padding(AppTheme.dimensions.paddingSmall)
                .width(IntrinsicSize.Min),
        ) {
            categories.forEach { category ->
                SettingsCategory(
                    category,
                    Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun SettingsCategory(
    model: SettingsCategoryModel,
    modifier: Modifier = Modifier,
) {
    Protrusive {
        Text(
            modifier = modifier
                .padding(AppTheme.dimensions.paddingSmall),
            text = model.title,
            style = AppTheme.typography.button,
            color = AppTheme.colors.backgroundText,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun Protrusive(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .background(AppTheme.colors.background)
            .protrusive(
                lightColor = AppTheme.colors.backgroundLight,
                darkColor = AppTheme.colors.backgroundDark,
            )
            .background(AppTheme.colors.background)
    ) {
        content()
    }
}

@Composable
fun SettingsPanel(
    model: SettingsPanelModel,
    onSwitch: (key: SettingsKey) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = spacedBy(AppTheme.dimensions.paddingSmall),
        modifier = modifier.width(IntrinsicSize.Max)
    ) {
        model.items.forEach { item ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = item.title,
                    style = AppTheme.typography.title,
                    color = AppTheme.colors.textLight
                )
                SettingsItemControl(
                    item,
                    onSwitch = { onSwitch(item.key) },
                    modifier = Modifier.padding(start = AppTheme.dimensions.paddingMedium)
                )
            }
        }
    }
}

@Composable
fun SettingsItemControl(
    item: SettingsItemModel,
    onSwitch: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (item) {
        is SettingsItemModel.ColorSelector -> ColorSelector(item, modifier)
        is SettingsItemModel.Switch -> SettingsSwitch(item, modifier, onSwitch)
    }
}

@Composable
fun ColorSelector(
    model: SettingsItemModel.ColorSelector,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = spacedBy(AppTheme.dimensions.paddingXS),
        modifier = modifier,
    ) {
        Cavity(mainColor = AppTheme.colors.background) {
            var currentInput by remember { mutableStateOf(TextFieldValue(model.currentColor.hex)) }
            BasicTextField(
                value = currentInput,
                onValueChange = { newFieldValue ->
                    if (newFieldValue.text.length <= 7) {
                        currentInput = newFieldValue
                    }
                },
                singleLine = true,
                modifier = Modifier
                    .background(AppTheme.colors.backgroundText)
                    .padding(AppTheme.dimensions.paddingSmall)
                    .width(IntrinsicSize.Min)
                    .widthIn(min = 60.dp) //TODO: Define this in characters (i.e. 7 chars)
            )
        }
        CavityButton(text = "", onClick = {}) {
            Text(
                text = "Save",
                style = AppTheme.typography.body,
                color = AppTheme.colors.backgroundText,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(AppTheme.dimensions.paddingXS)
            )
        }
    }
}

@Composable
fun SettingsSwitch(
    model: SettingsItemModel.Switch,
    modifier: Modifier = Modifier,
    onSwitch: () -> Unit,
) {
    Box(modifier = modifier.clickable {
        onSwitch()
    }) {
        Cavity(
            mainColor = AppTheme.colors.background,
            modifier = Modifier
                .width(45.dp)
                .align(Alignment.Center)
        ) {
            Protrusive(
                modifier = Modifier
                    .width(20.dp)
                    .height(20.dp)
                    .padding(1.dp)
                    .align(
                        when (model.isSelected) {
                            true -> Alignment.CenterEnd
                            false -> Alignment.CenterStart
                        }
                    )
            ) {
            }
        }
    }
}

data class SettingsCategoryModel(
    val id: Long,
    val title: String,
    val isSelected: Boolean,
)

data class SettingsPanelModel(
    val items: ImmutableList<SettingsItemModel>
)

sealed interface SettingsItemModel {
    val key: SettingsKey
    val title: String

    data class Switch(
        override val key: SettingsKey,
        override val title: String,
        val isSelected: Boolean,
    ) : SettingsItemModel

    data class ColorSelector(
        override val key: SettingsKey,
        override val title: String,
        val currentColor: SettingsColor,
    ) : SettingsItemModel
}

data class SettingsColor(
    val hex: String,
)
