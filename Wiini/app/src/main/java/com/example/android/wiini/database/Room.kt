package com.example.android.wiini.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AudioDao{
    @Query("select * from databaseaudio")
    fun getAudios(): LiveData<List<DatabaseAudio>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg audios: DatabaseAudio)
}

@Database(entities = [DatabaseAudio::class], version = 1)
abstract class AudiosDatabase: RoomDatabase(){
    abstract val audioDao: AudioDao
}

private lateinit var INSTANCE: AudiosDatabase

fun getDatabase(context: Context): AudiosDatabase{
    synchronized(AudiosDatabase::class.java){
        if(!::INSTANCE.isInitialized){
            INSTANCE = Room.databaseBuilder(context.applicationContext,
            AudiosDatabase::class.java,
            "audios").build()
        }
    }
    return INSTANCE
}