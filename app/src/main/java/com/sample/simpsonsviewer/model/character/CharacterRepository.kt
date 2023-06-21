package com.sample.simpsonsviewer.model.character

import com.sample.simpsonsviewer.domain.duck.DuckDuckGoApi
import com.sample.simpsonsviewer.model.UnknownException
import com.sample.simpsonsviewer.model.ValueResponse
import com.sample.simpsonsviewer.model.catchAsValueResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.runningReduce
import javax.inject.Inject


class CharacterRepository @Inject constructor(
    private val duckDuckGoApi: DuckDuckGoApi,
    private val characterDao: CharacterDao
) {
    fun getCharacters(query: Flow<String>): Flow<ValueResponse<List<CharacterEntity>>> =
        merge(getCharactersFromDatabase(query), fetchCharacters())
            .onStart { emit(ValueResponse.Loading) }
            .catchAsValueResponse()
            .runningReduce { accumulator, value ->
                if (value is ValueResponse.Error && accumulator is ValueResponse.Data) {
                    accumulator
                } else {
                    value
                }
            }.flowOn(Dispatchers.IO)


    private fun getCharactersFromDatabase(queryFlow: Flow<String>): Flow<ValueResponse<List<CharacterEntity>>> =
        queryFlow.flatMapLatest { query ->
            val flow = if (query.isEmpty()) characterDao.getAll() else characterDao.search(query)
            flow.map { drivers ->
                if (drivers.isEmpty() && query.isNotEmpty()) {
                    ValueResponse.Empty
                } else {
                    ValueResponse.Data(drivers)
                }
            }
        }


    private fun fetchCharacters() = flow {
        val result = duckDuckGoApi.getCharacters()
        if (result.isFailure) {
            val ex = result.exceptionOrNull() ?: UnknownException
            emit(ValueResponse.Error(ex))
        } else {
            val characters = result.getOrThrow().relatedTopics.map { it.toCharacterEntity() }
            characterDao.upsert(characters)
        }
    }
}