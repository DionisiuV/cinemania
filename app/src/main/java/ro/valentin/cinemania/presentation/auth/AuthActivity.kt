package ro.valentin.cinemania.presentation.auth

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.SignInButton
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import ro.valentin.cinemania.R
import ro.valentin.cinemania.core.Constants.LOG_TAG
import ro.valentin.cinemania.core.Constants.SIGN_IN_ERROR_MSG
import ro.valentin.cinemania.core.Utils.Companion.hide
import ro.valentin.cinemania.core.Utils.Companion.show
import ro.valentin.cinemania.domain.model.Response
import javax.inject.Inject

@AndroidEntryPoint
class AuthActivity : AppCompatActivity(R.layout.activity_auth) {
    private val viewModel by viewModels<AuthViewModel>()
    private lateinit var resultActivityLauncher: ActivityResultLauncher<IntentSenderRequest>
    private lateinit var loaderProgressBar: ProgressBar
    @Inject

    lateinit var oneTapClient: SignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initProgressBar()
        initResultActivityLauncher()
        initSignInButton()
    }

    private fun initSignInButton() {
        findViewById<SignInButton>(R.id.sign_in_button).setOnClickListener {
            oneTapSignInWithGoogle()
        }
    }

    private fun initProgressBar() {
        loaderProgressBar = findViewById(R.id.loadingProgressBar)
    }

    private fun initResultActivityLauncher() {
        val activityResult = ActivityResultContracts.StartIntentSenderForResult()
        resultActivityLauncher = registerForActivityResult(activityResult) { result ->
            try {
                val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
                val idToken = credential.googleIdToken
                val googleCredential = GoogleAuthProvider.getCredential(idToken, null)

                signInWithGoogle(googleCredential)
            } catch (e: Exception) {
                e.message?.let { Log.d(LOG_TAG, it) }
            }
        }
    }

    private fun oneTapSignInWithGoogle() {
        viewModel.signIn().observe(this) {  response ->
            when(response) {
                is Response.Loading -> {
                    findViewById<SignInButton>(R.id.sign_in_button).visibility = View.GONE
                    loaderProgressBar.show()
                }
                is Response.Success -> {
                    val intent = response.data
                    val intentSender = intent.pendingIntent.intentSender
                    val request = IntentSenderRequest.Builder(intentSender).build()

                    //send request to launcher
                    resultActivityLauncher.launch(request)

                    loaderProgressBar.hide()
                }
                is Response.Error -> {
                    loaderProgressBar.hide()
                    if(response.error == SIGN_IN_ERROR_MSG) {
                        oneTapSignUpWithGoogle()
                    }

                    Log.d(LOG_TAG, "Signup error ${response.error}")
                }
            }

        }
    }

    private fun oneTapSignUpWithGoogle() {
        viewModel.signUp().observe(this) {  response ->
            when(response) {
                is Response.Loading -> {
                    findViewById<SignInButton>(R.id.sign_in_button).visibility = View.GONE
                    loaderProgressBar.show()
                }
                is Response.Success -> {
                    Log.d(LOG_TAG, "oneTapSignUp() success")
                    val intent = response.data
                    val intentSender = intent.pendingIntent.intentSender
                    val request = IntentSenderRequest.Builder(intentSender).build()

                    //send request to launcher
                    resultActivityLauncher.launch(request)

                    loaderProgressBar.hide()
                }
                is Response.Error -> {
                    loaderProgressBar.hide()

                    Log.d(LOG_TAG, response.error)
                }
            }

        }
    }

    private fun signInWithGoogle(authCredential: AuthCredential) {
        viewModel.signInWithGoogle(authCredential).observe(this) {  response ->
            when(response) {
                is Response.Loading -> {
                    findViewById<SignInButton>(R.id.sign_in_button).visibility = View.GONE
                    loaderProgressBar.show()
                }
                is Response.Success -> {
                    loaderProgressBar.hide()
                    if(response.data) {
                        intent.extras?.let { goToMovieFragment(it.getInt("movieId")) }
                    }
                }
                is Response.Error -> {
                    loaderProgressBar.hide()
                    Log.d(LOG_TAG, response.error)
                }
            }
        }
    }

    private fun goToMovieFragment(movieId: Int) {
        val movieIdBundle = Bundle()
        movieIdBundle.putInt("movieId", movieId)

        Log.d(LOG_TAG, "movieId = $movieId")

    }
}