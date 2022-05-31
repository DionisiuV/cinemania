package ro.valentin.cinemania.presentation.splash

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ro.valentin.cinemania.domain.repository.AuthRepository
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    val isUserAuthenticated get() = authRepository.isUserAuthenticatedInFirebase()
}