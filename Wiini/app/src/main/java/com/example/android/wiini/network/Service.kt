package com.example.android.wiini.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET


/**
 *https://cloud.google.com/storage/docs/json_api/v1/buckets/get?apix_params=%7B%22bucket%22%3A%22my_tts_image_bucket%22%7D
 * https://cloud.google.com/storage/docs/authentication
 * https://developers.google.com/oauthplayground/?code=4/0AY0e-g44vUFX31PYPcmIaPGpcYTo7GrBixAsz06OttYzUP8h6wYHnllqo_X0iz64UwKNbA&scope=https://www.googleapis.com/auth/cloud-platform
 */
private const val BASE_URL = "https://storage.googleapis.com/"

interface WiiniService {
    @GET("/storage/v1/b/my_tts_image_bucket")
    fun getAudioAsync(): Deferred<NetworkAudioContainer>
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
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val wiini: WiiniService = retrofit.create(WiiniService::class.java)

}