package com.daniil.shevtsov.idle.core.ui.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daniil.shevtsov.idle.core.ui.upgradePreviewStub
import com.daniil.shevtsov.idle.core.ui.widgets.MyProgressBar
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
            AppTheme(
                colors = chooseColorsForId(speciesId = trait.id),
                shapes = chooseShapesForId(speciesId = trait.id),
            ) {
                Column {
                    Upgrade(
                        upgrade = upgradePreviewStub(
                            title = trait.title + "'s  upgrade",
                            status = UpgradeStatusModel.Affordable,
                        ),
                    )
                    MyProgressBar(progressPercentage = 0.75f)
                }

            }
        }
    }
}

fun chooseColorsForId(speciesId: Long): AppColors {
    return when (speciesId) {
        Species.Devourer.id -> devourerColors()
        Species.Vampire.id -> vampireColors()
        Species.Shapeshifter.id -> shapeshifterColors()
        Species.Parasite.id -> parasiteColors()
        Species.Demon.id -> demonColors()
        Species.Alien.id -> alienColors()
        Species.Android.id -> androidColors()
        else -> devourerColors()
    }
}

fun chooseShapesForId(speciesId: Long): AppShapes {
    return when (speciesId) {
        Species.Devourer.id -> devourerShapes()
        Species.Vampire.id -> vampireShapes()
        Species.Shapeshifter.id -> devourerShapes()
        Species.Parasite.id -> devourerShapes()
        Species.Demon.id -> devourerShapes()
        Species.Alien.id -> devourerShapes()
        Species.Android.id -> devourerShapes()
        else -> devourerShapes()
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

fun vampireColors(): AppColors = AppColors(
    backgroundLight = Color(0xFFAF3550),
    background = Color(0xFF911A34),
    backgroundDark = Color(0xFF600B1E),
    backgroundDarkest = Color(0xFF2F000F),
    backgroundText = Color(0xFFFACBE2),
    textDark = Color(0xFF3D0D1C),
    textLight = Color(0xFFfffeff),
    iconLight = Color(0xFFfffeff),
)

fun shapeshifterColors() = AppColors(
    backgroundLight = Color(0xFFFFD8AB),
    background = Color(0xFFD9A05F),
    backgroundDark = Color(0xFF815D33),
    backgroundDarkest = Color(0xFF4B2A1A),
    backgroundText = Color(0xFFFAE0D0),
    textDark = Color(0xFF1E0C0C),
    textLight = Color(0xFFFFDEDE),
    iconLight = Color(0xFFFFDEDE),
)

fun parasiteColors() = AppColors(
    backgroundLight = Color(0xFFAB84FF),
    background = Color(0xFF5F39B0),
    backgroundDark = Color(0xFF41257D),
    backgroundDarkest = Color(0xFF1A112D),
    backgroundText = Color(0xFFE2D4FF),
    textDark = Color(0xFF1E182A),
    textLight = Color(0xFFDBCDFA),
    iconLight = Color(0xFFDBCDFA),
)

fun demonColors() = AppColors(
    backgroundLight = Color(0xFFD63421),
    background = Color(0xFF841508),
    backgroundDark = Color(0xFF53150E),
    backgroundDarkest = Color(0xFF2F0C08),
    backgroundText = Color(0xFFFFCBC2),
    textDark = Color(0xFF360C08),
    textLight = Color(0xFFFF1A00),
    iconLight = Color(0xFFFF1A00),
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

fun devourerShapes() = AppShapes(
    progressBar = RectangleShape,
)

fun vampireShapes() = AppShapes(
    progressBar = CutCornerShape(percent = 50),
)
