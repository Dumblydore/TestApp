package com.sample.simpsonsviewer.domain.duck

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DuckDuckGoResponse(
    @Json(name = "RelatedTopics") val relatedTopics: List<RelatedTopic>
) {
    @JsonClass(generateAdapter = true)
    data class RelatedTopic(
        @Json(name = "FirstURL") val firstURL: String,
        @Json(name = "Icon") val icon: Icon,
        @Json(name = "Result") val result: String,
        @Json(name = "Text") val text: String
    ) {
        @JsonClass(generateAdapter = true)
        data class Icon(
            @Json(name = "Height") val height: String,
            @Json(name = "URL") val url: String?,
            @Json(name = "Width") val width: String
        )
    }
}