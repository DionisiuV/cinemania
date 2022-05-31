package ro.valentin.cinemania.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.google.firebase.auth.AuthCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import ro.valentin.cinemania.domain.repository.AuthRepository
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {
    fun signIn() = liveData(Dispatchers.IO) {
        authRepository.oneTapSignInGoogle().collect {
            emit(it)
        }
    }

    fun signUp() = liveData(Dispatchers.IO) {
        authRepository.oneTapSignUpGoogle().collect {
            emit(it)
        }
    }

    fun signInWithGoogle(authCredential: AuthCredential) = liveData(Dispatchers.IO) {
        authRepository.firebaseSignInWithGoogle(authCredential).collect {
            emit(it)
        }
    }

    fun firebaseSignOut() = liveData(Dispatchers.IO) {
        authRepository.firebaseSignOut().collect {
            emit(it)
        }
    }

    fun authStateListener() = liveData(Dispatchers.IO) {
        authRepository.authStateListener().collect {
            emit(it)
        }
    }
}
