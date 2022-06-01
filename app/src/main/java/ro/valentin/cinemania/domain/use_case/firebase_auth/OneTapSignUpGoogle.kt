package ro.valentin.cinemania.domain.use_case.firebase_auth

import ro.valentin.cinemania.domain.repository.AuthRepository
import javax.inject.Inject

class OneTapSignUpGoogle @Inject constructor(
    private val firebaseAuthRepository: AuthRepository
){

    suspend operator fun invoke() = firebaseAuthRepository.oneTapSignUpGoogle()
}