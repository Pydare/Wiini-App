package com.example.android.wiini.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.android.wiini.domain.Audio


@Entity(tableName = "databaseaudio")
data class DatabaseAudio constructor(
    @PrimaryKey
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
    val locationType: String
)

fun List<DatabaseAudio>.asDomainModel(): List<Audio>{
    return map{
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
                iamConfiguration = it.iamConfiguration,
                locationType = it.locationType
        )
    }
}