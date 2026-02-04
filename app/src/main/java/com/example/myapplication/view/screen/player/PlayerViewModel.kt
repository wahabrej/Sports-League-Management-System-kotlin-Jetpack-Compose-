package com.example.myapplication.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
class PlayerViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(PlayerUiState())
    val uiState = _uiState.asStateFlow()

    //  first time auto load
    init {
        loadPlayers(PlayerTab.ALL)
    }

    fun selectTab(tab: PlayerTab) {
        _uiState.update { it.copy(selectedTab = tab) }
        loadPlayers(tab)
    }

    fun toggleChange(value: Boolean) {
        _uiState.update { it.copy(isToggleOn = value) }
    }

    private fun loadPlayers(tab: PlayerTab) {
        viewModelScope.launch {

            val list = when (tab) {
                PlayerTab.ALL -> listOf("Rahim", "Karim", "Hasan")
                PlayerTab.NEW -> listOf("New Player 1", "New Player 2")
                PlayerTab.OLD -> listOf("Old Player 1")
                PlayerTab.RESIGN -> listOf("Resigned Player")
            }

            _uiState.update { it.copy(players = list) }
        }
    }
}
