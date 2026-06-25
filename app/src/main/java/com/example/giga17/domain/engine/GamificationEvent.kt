package com.example.giga17.domain.engine

sealed class GamificationEvent {
    data class LevelUp(val newLevel: Int) : GamificationEvent()
    data class BadgeUnlocked(val badge: GamificationEngine.BadgeDef) : GamificationEvent()
}
