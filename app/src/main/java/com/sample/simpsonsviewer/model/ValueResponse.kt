package com.sample.simpsonsviewer.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlin.coroutines.cancellation.CancellationException

internal object UnknownException : Exception("Unknown Exception Occurred")

sealed class ValueResponse<out T> {
    open val data: T? = null

    object Loading : ValueResponse<Nothing>()
    object Empty : ValueResponse<Nothing>()
    data class Data<T>(override val data: T) : ValueResponse<T>()
    data class Error(val error: Throwable) : ValueResponse<Nothing>()
}

fun <T> Flow<ValueResponse<T>>.catchAsValueResponse() = catch { error ->
    if (error is CancellationException) {
        throw error
    } else {
        emit(ValueResponse.Error(error))
    }
}