package com.example.android.wiini.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET


interface WiiniService {
    //TODO: Figure out android audio operations in API format
    @GET("wiini.json") // i'd need a util to convert this to android audio
    fun getPlaylist(): Deferred<NetworkAudioContainer>
}

/**
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()


/**
 * Main entry point for network access. Call like `Network.wiini.getPlaylist()`
 */
object Network {
    // Configure retrofit to parse JSON and use coroutines: this part would definitely be
    // reviewed later: this is where the API call to the GCP would be made
    // TODO: Make API call to the GCP
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://devbytes.udacity.com/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val wiini = retrofit.create(WiiniService::class.java)

}