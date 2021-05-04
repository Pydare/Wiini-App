package com.example.android.wiini.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.android.wiini.domain.Audio


@Entity(tableName = "databaseaudio")
data class DatabaseAudio constructor(
    @PrimaryKey
    val url: String,
    val title: String,
    val description: String
)

fun List<DatabaseAudio>.asDomainModel(): List<Audio>{
    return map{
        Audio(
            url = it.url,
            title = it.title,
            description = it.description
        )
    }
}