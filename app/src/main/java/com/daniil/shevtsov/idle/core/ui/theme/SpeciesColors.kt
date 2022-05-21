package com.daniil.shevtsov.idle.core.ui.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.AbsoluteCutCornerShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daniil.shevtsov.idle.core.ui.Icons
import com.daniil.shevtsov.idle.core.ui.shapes.WavyShape
import com.daniil.shevtsov.idle.core.ui.shapes.standardBezierPoints
import com.daniil.shevtsov.idle.core.ui.upgradePreviewStub
import com.daniil.shevtsov.idle.core.ui.widgets.TitleWithProgress
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
            val theme = chooseThemeForId(trait.id)
            AppTheme(
                colors = theme.colors,
                shapes = theme.shapes,
            ) {
                Column {
                    Upgrade(
                        upgrade = upgradePreviewStub(
                            title = trait.title + "'s  upgrade",
                            status = UpgradeStatusModel.Affordable,
                        ),
                    )
                    TitleWithProgress(
                        title = "Kek",
                        name = "Investigation",
                        progress = 0.85f,
                        icon = Icons.Suspicion,
                    )
                }

            }
        }
    }
}

fun chooseThemeForId(speciesId: Long): SpeciesTheme {
    val colors: AppColors
    val shapes: AppShapes

    when (speciesId) {
        Species.Devourer.id -> {
            colors = AppColors(
                backgroundLight = Color(0xFFD64747),
                background = Color(0xFFAF0A0A),
                backgroundDark = Color(0xFF750404),
                backgroundDarkest = Color(0xFF2F1B1B),
                backgroundText = Color.White,
                textDark = Color.Black,
                textLight = Color.White,
                iconLight = Color.White,
            )
            shapes = AppShapes(
                progressBar = RectangleShape,
            )
        }
        Species.Vampire.id -> {
            colors = AppColors(
                backgroundLight = Color(0xFFAF3550),
                background = Color(0xFF911A34),
                backgroundDark = Color(0xFF600B1E),
                backgroundDarkest = Color(0xFF2F000F),
                backgroundText = Color(0xFFFACBE2),
                textDark = Color(0xFF3D0D1C),
                textLight = Color(0xFFfffeff),
                iconLight = Color(0xFFfffeff),
            )
            shapes = AppShapes(
                progressBar = CutCornerShape(percent = 50),
            )
        }
        Species.Shapeshifter.id -> {
            colors = AppColors(
                backgroundLight = Color(0xFFFFD8AB),
                background = Color(0xFFD9A05F),
                backgroundDark = Color(0xFF815D33),
                backgroundDarkest = Color(0xFF4B2A1A),
                backgroundText = Color(0xFFFAE0D0),
                textDark = Color(0xFF1E0C0C),
                textLight = Color(0xFFFFDEDE),
                iconLight = Color(0xFFFFDEDE),
            )
            shapes = AppShapes(
                progressBar = RectangleShape,
            )
        }
        Species.Parasite.id -> {
            colors = AppColors(
                backgroundLight = Color(0xFFAB84FF),
                background = Color(0xFF5F39B0),
                backgroundDark = Color(0xFF41257D),
                backgroundDarkest = Color(0xFF1A112D),
                backgroundText = Color(0xFFE2D4FF),
                textDark = Color(0xFF1E182A),
                textLight = Color(0xFFDBCDFA),
                iconLight = Color(0xFFDBCDFA),
            )
            shapes = AppShapes(
                progressBar = RectangleShape,
            )
        }
        Species.Demon.id -> {
            colors = AppColors(
                backgroundLight = Color(0xFFD63421),
                background = Color(0xFF841508),
                backgroundDark = Color(0xFF53150E),
                backgroundDarkest = Color(0xFF2F0C08),
                backgroundText = Color(0xFFFFCBC2),
                textDark = Color(0xFF360C08),
                textLight = Color(0xFFFF1A00),
                iconLight = Color(0xFFFF1A00),
            )
            shapes = AppShapes(
                progressBar = WavyShape(::standardBezierPoints),
            )
        }
        Species.Alien.id -> {
            colors = AppColors(
                backgroundLight = Color(0xFFa0d29e),
                background = Color(0xFF4F9766),
                backgroundDark = Color(0xFF487463),
                backgroundDarkest = Color(0xFF225149),
                backgroundText = Color(0xFFE0FFF1),
                textDark = Color(0xFF212B27),
                textLight = Color(0xFFDFFCF1),
                iconLight = Color(0xFFDFFCF1),
            )
            shapes = AppShapes(
                progressBar = WavyShape(::standardBezierPoints),
            )
        }
        Species.Android.id -> {
            colors = AppColors(
                backgroundLight = Color(0xFFCDDEE5),
                background = Color(0xFF94ACB6),
                backgroundDark = Color(0xFF5A6E76),
                backgroundDarkest = Color(0xFF36454B),
                backgroundText = Color.White,
                textDark = Color(0xFF26235A),
                textLight = Color(0xFFE0DEFB),
                iconLight = Color(0xFFE0DEFB),
            )
            shapes = AppShapes(
                progressBar = AbsoluteCutCornerShape(6.dp),
            )
        }
        else -> {
            colors = defaultColors()
            shapes = defaultShapes()
        }
    }

    return SpeciesTheme(
        colors = colors,
        shapes = shapes,
    )
}

data class SpeciesTheme(
    val colors: AppColors,
    val shapes: AppShapes,
)
