package com.sample.simpsonsviewer.model

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sample.simpsonsviewer.model.character.CharacterDao
import com.sample.simpsonsviewer.model.character.CharacterEntity

@Database(
    entities = [CharacterEntity::class],
    version = 1
)
abstract class ViewerDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
}