package com.example.android.wiini.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AudioDao{
    /**
     * retrieve the particular audio with key value
     * @param key is the unique url for retrieving
     */
    @Query("SELECT * FROM databaseaudio WHERE title = :key")
    fun get(key: String): DatabaseAudio

    /**
     * delete the particular audio with key value
     * @param key is the unique url for retrieving
     */
    @Query("DELETE FROM databaseaudio WHERE title = :key")
    fun delete(key: String)

    /**
     * delete all audio files from the db
     */
    @Query("DELETE FROM databaseaudio")
    fun clear()

    /**
     * get all audios from the db
     */
    @Query("select * from databaseaudio")
    fun getAudios(): LiveData<List<DatabaseAudio>>

    /**
     * insert one audio into the db
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(input: DatabaseAudio)

    /**
     * insert all audios into the db
     */
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