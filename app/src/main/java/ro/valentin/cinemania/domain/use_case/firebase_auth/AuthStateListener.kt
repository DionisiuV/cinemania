package ro.valentin.cinemania.domain.use_case.firebase_auth

import ro.valentin.cinemania.domain.repository.AuthRepository
import javax.inject.Inject

class AuthStateListener @Inject constructor(
    private val firebaseAuthRepository: AuthRepository
){
    operator fun invoke() = firebaseAuthRepository.authStateListener()
}