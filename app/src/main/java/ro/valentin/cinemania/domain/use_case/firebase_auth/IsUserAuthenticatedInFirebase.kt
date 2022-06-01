package ro.valentin.cinemania.domain.use_case.firebase_auth

import ro.valentin.cinemania.domain.repository.AuthRepository
import ro.valentin.cinemania.domain.repository.FirebaseDatabaseRepository
import javax.inject.Inject

class IsUserAuthenticatedInFirebase @Inject constructor(
    private val firebaseAuthRepository: AuthRepository
){

    operator fun invoke() = firebaseAuthRepository.isUserAuthenticatedInFirebase()
}