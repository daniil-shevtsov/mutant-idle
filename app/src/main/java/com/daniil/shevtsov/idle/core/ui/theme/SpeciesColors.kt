package com.daniil.shevtsov.idle.core.ui.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.AbsoluteCutCornerShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daniil.shevtsov.idle.core.ui.Icons
import com.daniil.shevtsov.idle.core.ui.upgradePreviewStub
import com.daniil.shevtsov.idle.core.ui.widgets.TitleWithProgress
import com.daniil.shevtsov.idle.feature.colors.presentation.SpeciesColors
import com.daniil.shevtsov.idle.feature.colors.presentation.toAppColors
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
        contentPadding = PaddingValues(AppTheme.dimensions.paddingSmall)
    ) {
        items(species) { trait ->
            val theme = chooseThemeForId(trait.id, speciesColors = trait.colors)
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

fun chooseThemeForId(
    speciesId: Long,
    speciesColors: SpeciesColors?,
): SpeciesTheme {
    val shapes: AppShapes = when (speciesId) {
        Species.Devourer.id -> AppShapes(
            progressBar = RectangleShape,
        )
        Species.Vampire.id -> AppShapes(
            progressBar = CutCornerShape(percent = 50),
        )
        Species.Shapeshifter.id -> AppShapes(
            progressBar = RectangleShape,
        )
        Species.Parasite.id -> AppShapes(
            progressBar = RectangleShape,
        )
        Species.Demon.id -> AppShapes(
            progressBar = RectangleShape,
        )
        Species.Alien.id -> AppShapes(
            progressBar = RectangleShape,
        )
        Species.Android.id -> AppShapes(
            progressBar = AbsoluteCutCornerShape(6.dp),
        )
        else -> defaultShapes()
    }

    return SpeciesTheme(
        colors = speciesColors?.toAppColors() ?: defaultColors(),
        shapes = shapes,
    )
}

data class SpeciesTheme(
    val colors: AppColors,
    val shapes: AppShapes,
)
