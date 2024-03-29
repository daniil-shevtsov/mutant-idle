package com.daniil.shevtsov.idle.feature.player.core.data

import com.daniil.shevtsov.idle.core.di.AppScope
import com.daniil.shevtsov.idle.feature.player.core.domain.Player
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@AppScope
class PlayerStorage @Inject constructor(
    initialPlayer: Player
) {
    private val player: MutableStateFlow<Player> =
        MutableStateFlow(initialPlayer)

    fun observe(): Flow<Player> {
        return player.asStateFlow()
    }

    fun get() = player.value

    fun update(newPlayer: Player) {
        player.value = newPlayer
    }

}
