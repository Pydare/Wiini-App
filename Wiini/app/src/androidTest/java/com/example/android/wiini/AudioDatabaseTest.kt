package com.example.android.wiini

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.android.wiini.database.AudioDao
import com.example.android.wiini.database.AudiosDatabase
import com.example.android.wiini.database.DatabaseAudio
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.lang.Exception

@RunWith(AndroidJUnit4::class)
class AudioDatabaseTest{

    private lateinit var audioDao: AudioDao
    private lateinit var db: AudiosDatabase

    @Before
    fun createDb(){
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory db because the information stored here disappears when
        // the process is killed.
        db = Room.inMemoryDatabaseBuilder(context, AudiosDatabase::class.java)
                // Allowing main thread queries, just for testing
                .allowMainThreadQueries()
                .build()
        audioDao = db.audioDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb(){
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetAudio(){
        val testAudio = DatabaseAudio("https://test.com", "Test Audio", "This is a test audio")
        audioDao.insert(testAudio)
        val retrievedTestAudio = audioDao.get("Test Audio")
        assertEquals(retrievedTestAudio?.url, "https://test.com")
    }

}