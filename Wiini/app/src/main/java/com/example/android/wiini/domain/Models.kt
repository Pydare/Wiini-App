package com.example.android.wiini.domain

import androidx.room.PrimaryKey
import com.example.android.wiini.util.smartTruncate
import com.squareup.moshi.Json

/**
 * Domain objects are plain Kotlin data classes that represent the things in the app.
 * These are the objects that should be displayed on screen, or manipulated by the app.
 *
 * @see database for objects that are mapped to the database
 * @see network for objects that parse or prepare network calls
 */

/**
 * Audios represent an audio that can be played
 */
data class Audio (
        val url: String,
        val title: String,
        val description: String,
        val kind: String,
        val id: String,
        val name: String,
        val location: String,
        val storageClass: String,
        val etag: String,
        val timeCreated: String,
        val updated: String,
        val iamConfiguration: String,
        val locationType: String){

    /**
     * Short description is used for displaying truncated descriptions in the UI
     */
    val shortDescription: String
        get() = description.smartTruncate(200)
}
