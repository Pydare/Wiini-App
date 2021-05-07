package com.example.android.wiini.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.android.wiini.database.AudiosDatabase
import com.example.android.wiini.database.asDomainModel
import com.example.android.wiini.domain.Audio
import com.example.android.wiini.network.Network
import com.example.android.wiini.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException


/**
 * Class that has methods to load data without specifying the
 * data source. Could be from cache or network
 */
class AudiosRepository(private val database: AudiosDatabase) {

    val audios: LiveData<List<Audio>> = Transformations.map(database.audioDao.getAudios()){
        it.asDomainModel()
    }

    suspend fun refreshAudios(){
        // this dispatchers.IO is used because its the io coroutine that's being carried out
        // io is usually used for read--write operations
        withContext(Dispatchers.IO){
            val playlist = Network.wiini.getAudioAsync().await() // await is a suspend function and doesn't block the thread
            try {
                database.audioDao.insertAll(*playlist.asDatabaseModel())
            }catch (e: IOException){
                Log.e("RefreshAudiosErrorTag", "Failed to set media data source", e)
            }
        }
    }

}