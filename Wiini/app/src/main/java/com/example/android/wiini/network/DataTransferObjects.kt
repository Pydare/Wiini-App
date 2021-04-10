package com.example.android.wiini.network

import com.example.android.wiini.database.DatabaseAudio
import com.example.android.wiini.domain.Audio
import com.squareup.moshi.JsonClass

/**
 * DataTransferObjects go in this file. These are responsible for parsing responses from the server
 * or formatting objects to send to the server. You should convert these to domain objects before
 * using them.
 */

/**
 * AudioHolder holds a list of Audios.
 *
 * This is to parse first level of our network result which looks like
 *
 * {
 *   "audios": []
 * }
 */
@JsonClass(generateAdapter = true)
data class NetworkAudioContainer(val audios: List<NetworkAudio>)

/**
 * Audios represent an audio that can be played
 */
@JsonClass(generateAdapter = true)
data class NetworkAudio(
    val url: String,
    val title: String,
    val description: String
)

/**
 * Convert Network results to database objects
 */
fun NetworkAudioContainer.asDomainModel(): List<Audio>{
    return audios.map {
        Audio(
            url = it.url,
            title = it.title,
            description = it.description
        )
    }
}

/**
 * Convert data transfer objects to database objects
 */
fun NetworkAudioContainer.asDatabaseModel(): Array<DatabaseAudio>{
    return audios.map {
        DatabaseAudio(
            url = it.url,
            title = it.title,
            description = it.description
        )
    }.toTypedArray()
}

