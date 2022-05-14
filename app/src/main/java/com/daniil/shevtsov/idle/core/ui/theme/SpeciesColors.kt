package com.daniil.shevtsov.idle.core.ui.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
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
