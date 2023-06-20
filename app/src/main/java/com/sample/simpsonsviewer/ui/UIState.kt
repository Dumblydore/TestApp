package com.sample.simpsonsviewer.ui

import com.sample.simpsonsviewer.model.ValueResponse


enum class UIState {
    None,
    Loading,
    Success,
    Error
}
fun ValueResponse<*>.asUiState(): UIState = when (this) {
    ValueResponse.Loading -> UIState.Loading
    is ValueResponse.Data -> UIState.Success
    ValueResponse.Empty,
    is ValueResponse.Error -> UIState.Error
}