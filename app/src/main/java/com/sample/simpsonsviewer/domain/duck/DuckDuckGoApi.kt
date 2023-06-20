package com.sample.simpsonsviewer.domain.duck

import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DuckDuckGoApi @Inject constructor(
    private val client: OkHttpClient,
    private val apiUrl: String,
    private val moshi: Moshi
) {
    suspend fun getCharacters(): Result<DuckDuckGoResponse> = runCatching {
        val call = client.newCall(
            Request.Builder()
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .get()
                .url(apiUrl)
                .build()
        ).execute()
        yield()
        call.body?.source()?.let {
            moshi.adapter(DuckDuckGoResponse::class.java).fromJson(it)
        } ?: throw IllegalStateException("No response")
    }
}