package com.sample.simpsonsviewer.model.character

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Upsert
import com.sample.simpsonsviewer.domain.duck.DuckDuckGoResponse
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "icon")
    val icon: String?
)

@Dao
interface CharacterDao {

    @Upsert
    suspend fun upsert(characters: CharacterEntity)

    @Upsert
    suspend fun upsert(characters: List<CharacterEntity>)

    @Query("SELECT * FROM characters")
    fun getAll(): Flow<List<CharacterEntity>>

    @Query("SELECT * FROM characters WHERE name LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%'")
    fun search(query: String): Flow<List<CharacterEntity>>

    @Query("SELECT * FROM characters where id=:id")
    fun get(id: String): Flow<CharacterEntity>

    @Query("DELETE FROM characters WHERE characters.id=:id")
    suspend fun delete(id: String)

    @Query("DELETE FROM characters ")
    suspend fun deleteAll()
}

fun DuckDuckGoResponse.RelatedTopic.toCharacterEntity(): CharacterEntity {
    val splitText = text.split(" - ")
    val name = splitText.first()
    val description = splitText.getOrNull(1).orEmpty()

    return CharacterEntity(
        id = firstURL,
        name = name,
        description = description,
        icon = if (icon.url.isNullOrEmpty()) null else "https://duckduckgo.com${icon.url}"
    )
}