package com.example.android.wiini.oauth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.android.wiini.R
import com.example.android.wiini.databinding.ActivitySignInBinding
import com.example.android.wiini.ui.MainActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.*


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