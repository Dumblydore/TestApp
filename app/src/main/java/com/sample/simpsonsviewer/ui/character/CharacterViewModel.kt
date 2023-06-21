package com.sample.simpsonsviewer.ui.character

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.simpsonsviewer.model.ValueResponse
import com.sample.simpsonsviewer.model.character.CharacterEntity
import com.sample.simpsonsviewer.model.character.CharacterRepository
import com.sample.simpsonsviewer.ui.UIState
import com.sample.simpsonsviewer.ui.asUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class CharacterState(
    val query: String = "",
    val characters: List<CharacterEntity> = emptyList(),
    val selectedCharacter: CharacterEntity? = null,
    val uiState: UIState = UIState.None
)

@HiltViewModel
class CharacterViewModel @Inject constructor(
    charaRepository: CharacterRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val selectedCharacter = MutableStateFlow<CharacterEntity?>(null)
    private val query = savedStateHandle.getStateFlow("query", "")

    val state: StateFlow<CharacterState> = combine(
        query,
        charaRepository.getCharacters(query.debounce(125L)),
        selectedCharacter,
        ::processResponse
    ).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), CharacterState())

    fun updateQuery(query: String) {
        savedStateHandle["query"] = query
    }

    fun selectCharacter(character: CharacterEntity) {
        selectedCharacter.value = character
    }

    fun clearSelectedCharacter() {
        selectedCharacter.value = null
    }

    private fun processResponse(
        query: String,
        response: ValueResponse<List<CharacterEntity>>,
        selectedCharacter: CharacterEntity?
    ): CharacterState = CharacterState(
        query = query,
        uiState = response.asUiState(),
        selectedCharacter = selectedCharacter,
        characters = response.data.orEmpty()
    )
}