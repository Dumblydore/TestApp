package com.sample.simpsonsviewer.di

import android.content.Context
import androidx.room.Room
import com.sample.simpsonsviewer.BuildConfig
import com.sample.simpsonsviewer.model.ViewerDatabase
import com.sample.simpsonsviewer.model.character.CharacterDao
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {
    @Provides
    @Singleton
    fun providesOkhttpClient(): OkHttpClient = OkHttpClient.Builder()
        .build()

    @Provides
    @Singleton
    fun providesMoshi(): Moshi = Moshi.Builder()
        .build()

    @Provides
    @Singleton
    fun providesViewerDatabase(@ApplicationContext context: Context): ViewerDatabase =
        Room.databaseBuilder(context, ViewerDatabase::class.java, "ViewerDatabase")
            .build()

    @Provides
    fun providesCharacterDao(viewerDatabase: ViewerDatabase): CharacterDao =
        viewerDatabase.characterDao()

    @Provides
    fun providesApiUrl(): String = BuildConfig.API_URL
}