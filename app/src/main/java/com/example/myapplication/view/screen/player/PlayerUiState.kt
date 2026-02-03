package com.example.myapplication.viewmodel

data class PlayerUiState(
    val selectedTab: PlayerTab = PlayerTab.ALL,
    val isToggleOn: Boolean = false,
    val players: List<String> = emptyList()
)

enum class PlayerTab {
    ALL, NEW, OLD, RESIGN
}
