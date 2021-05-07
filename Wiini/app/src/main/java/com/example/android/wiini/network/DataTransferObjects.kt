package com.example.android.wiini.network

import com.example.android.wiini.database.DatabaseAudio
import com.example.android.wiini.domain.Audio
import com.squareup.moshi.Json
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
    @Json(name = "selfLink")
    val url: String,
    @Json(name = "projectNumber")
    val title: String,
    @Json(name = "metageneration")
    val description: String,
    @Json(name = "kind")
    val kind: String,
    @Json(name = "id")
    val id: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "location")
    val location: String,
    @Json(name = "storageClass")
    val storageClass: String,
    @Json(name = "etag")
    val etag: String,
    @Json(name = "timeCreated")
    val timeCreated: String,
    @Json(name = "updated")
    val updated: String,
    @Json(name = "iamConfiguration")
    val iamConfiguration: Object,
    @Json(name = "locationType")
    val locationType: String
)

/**
 * Convert Network results to database objects
 */
fun NetworkAudioContainer.asDomainModel(): List<Audio>{
    // TODO: get a better title and description
    return audios.map {
        Audio(
            url = it.url,
            title = it.title,
            description = it.description,
            kind = it.kind,
            id = it.id,
            name = it.name,
            location = it.location,
            storageClass = it.storageClass,
            etag = it.etag,
            timeCreated = it.timeCreated,
            updated = it.updated,
            iamConfiguration = it.iamConfiguration.toString(),
            locationType = it.locationType
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
                description = it.description,
                kind = it.kind,
                id = it.id,
                name = it.name,
                location = it.location,
                storageClass = it.storageClass,
                etag = it.etag,
                timeCreated = it.timeCreated,
                updated = it.updated,
                iamConfiguration = it.iamConfiguration.toString(),
                locationType = it.locationType
        )
    }.toTypedArray()
}

