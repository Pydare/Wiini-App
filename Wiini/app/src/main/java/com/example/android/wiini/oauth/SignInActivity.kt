package com.example.android.wiini.oauth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.android.wiini.R
import com.example.android.wiini.databinding.ActivitySignInBinding
import com.example.android.wiini.ui.MainActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.*
import okhttp3.*
import java.io.IOException


class SignInActivity: AppCompatActivity() {

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding : ActivitySignInBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in)

        val googleSignInOptions : GoogleSignInOptions = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(R.string.server_client_id.toString())
            .requestEmail()
            .build()

        val mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        binding.signInButton.setOnClickListener {
            val intent: Intent = mGoogleSignInClient.signInIntent
            startActivityForResult(intent, RC_SIGN_IN)
        }

        binding.mainAppButton.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == RC_SIGN_IN) {
            val result : GoogleSignInResult? = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result != null) {
                handleSignInResult(result)
            }
        }
    }

    private fun handleSignInResult(signInResult: GoogleSignInResult) {
        if(signInResult.isSuccess) {
            // TODO: Update UI
            val account : GoogleSignInAccount? = signInResult.signInAccount
            val idToken: String? = account?.idToken

            // okhttp recipes https://square.github.io/okhttp/recipes/
            val client = OkHttpClient()

            val formBody: RequestBody = FormBody.Builder()
                    .add("idToken", idToken)
                    .build()
            val request: Request = Request.Builder()
                    .url("https://storage.googleapis.com/storage/v1/b/my_tts_image_bucket/")
                    .post(formBody)
                    .build()

            try {
                val response: Response = client.newCall(request).execute()
                val statusCode: Int = response.code()
                val responseBody = response.body().toString()
                Log.i("SignedInTag", "signed in as $responseBody")

            } catch (e: IOException) {
                e.printStackTrace()
            }

            // TODO: verify the token info at the backend


        } else {
            // TODO: Update UI
        }
    }


    override fun onStart() {
        super.onStart()
        // check for existing google sign in account, if the user is already signed in
        // the GoogleSignInAccount will be non-null
        val account = GoogleSignIn.getLastSignedInAccount(this)
        // if account is non-null, update ui ie hide the sign-in button and launch the main activity
        if (account != null) {
            val text = "User already signed in!"
            val duration = Toast.LENGTH_SHORT

            val toast = Toast.makeText(applicationContext, text, duration)
            toast.show()
        }
    }


}