package com.daniil.shevtsov.idle.core.ui.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daniil.shevtsov.idle.core.ui.upgradePreviewStub
import com.daniil.shevtsov.idle.feature.initial.domain.createInitialTraits
import com.daniil.shevtsov.idle.feature.player.species.domain.Species
import com.daniil.shevtsov.idle.feature.player.trait.domain.TraitId
import com.daniil.shevtsov.idle.feature.upgrade.presentation.UpgradeStatusModel
import com.daniil.shevtsov.idle.feature.upgrade.view.Upgrade

@Preview(widthDp = 600)
@Composable
fun ThemeColorsPreview() {
    val species = createInitialTraits()
        .filter { it.traitId == TraitId.Species }
    LazyVerticalGrid(
        cells = GridCells.Fixed(2),
        contentPadding = PaddingValues(4.dp)
    ) {
        items(species) { trait ->
            AppTheme(colors = chooseColorsForId(speciesId = trait.id)) {
                Upgrade(
                    upgrade = upgradePreviewStub(
                        title = trait.title + "'s  upgrade",
                        status = UpgradeStatusModel.Affordable,
                    ),
                )
            }
        }
    }
}

fun chooseColorsForId(speciesId: Long): AppColors {
    return when (speciesId) {
        Species.Devourer.id -> devourerColors()
        Species.Alien.id -> alienColors()
        Species.Android.id -> androidColors()
        Species.Vampire.id -> vampireColors()
        else -> devourerColors()
    }
}

fun devourerColors(): AppColors = AppColors(
    backgroundLight = Color(0xFFD64747),
    background = Color(0xFFAF0A0A),
    backgroundDark = Color(0xFF750404),
    backgroundDarkest = Color(0xFF2F1B1B),
    backgroundText = Color.White,
    textDark = Color.Black,
    textLight = Color.White,
    iconLight = Color.White,
)

fun alienColors(): AppColors = AppColors(
    backgroundLight = Color(0xFFa0d29e),
    background = Color(0xFF4F9766),
    backgroundDark = Color(0xFF487463),
    backgroundDarkest = Color(0xFF225149),
    backgroundText = Color(0xFFE0FFF1),
    textDark = Color(0xFF212B27),
    textLight = Color(0xFFDFFCF1),
    iconLight = Color(0xFFDFFCF1),
)

fun androidColors(): AppColors = AppColors(
    backgroundLight = Color(0xFFCDDEE5),
    background = Color(0xFF94ACB6),
    backgroundDark = Color(0xFF5A6E76),
    backgroundDarkest = Color(0xFF36454B),
    backgroundText = Color.White,
    textDark = Color(0xFF26235A),
    textLight = Color(0xFFE0DEFB),
    iconLight = Color(0xFFE0DEFB),
)

fun vampireColors(): AppColors = AppColors(
    backgroundLight = Color(0xFFAF3550),
    background = Color(0xFF911A34),
    backgroundDark = Color(0xFF600B1E),
    backgroundDarkest = Color(0xFF2F000F),
    backgroundText = Color(0xFFFACBE2),
    textDark = Color(0xFFc79ab0),
    textLight = Color(0xFFfffeff),
    iconLight = Color(0xFFfffeff),
)
