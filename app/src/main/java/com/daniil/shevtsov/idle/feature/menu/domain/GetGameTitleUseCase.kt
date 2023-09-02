package com.daniil.shevtsov.idle.feature.menu.domain

import kotlinx.coroutines.delay

class GetGameTitleUseCase(
    private val repository: GameTitleRepository
) {
    suspend operator fun invoke(): String {
        return repository.get()
    }
}

interface GameTitleRepository {
    suspend fun get(): String
}

class GameTitleRepositoryImpl() : GameTitleRepository {
    override suspend fun get(): String {
        delay(2000L)
        return "Mutant Idle"
    }
}
