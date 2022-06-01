package ro.valentin.cinemania.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.google.firebase.auth.AuthCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import ro.valentin.cinemania.domain.repository.AuthRepository
import ro.valentin.cinemania.domain.use_case.firebase_auth.FirebaseAuthUseCases
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCases: FirebaseAuthUseCases
): ViewModel() {
    fun signIn() = liveData(Dispatchers.IO) {
        authUseCases.oneTapSignInGoogle().collect {
            emit(it)
        }
    }

    fun signUp() = liveData(Dispatchers.IO) {
        authUseCases.oneTapSignUpGoogle().collect {
            emit(it)
        }
    }

    fun signInWithGoogle(authCredential: AuthCredential) = liveData(Dispatchers.IO) {
        authUseCases.firebaseSignInWithGoogle(authCredential).collect {
            emit(it)
        }
    }

    fun firebaseSignOut() = liveData(Dispatchers.IO) {
        authUseCases.firebaseSignOut().collect {
            emit(it)
        }
    }

    fun authStateListener() = liveData(Dispatchers.IO) {
        authUseCases.authStateListener().collect {
            emit(it)
        }
    }
}
