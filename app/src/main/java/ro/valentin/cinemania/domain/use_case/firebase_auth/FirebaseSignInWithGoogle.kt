package ro.valentin.cinemania.domain.use_case.firebase_auth

import com.google.firebase.auth.AuthCredential
import ro.valentin.cinemania.domain.repository.AuthRepository
import javax.inject.Inject

class FirebaseSignInWithGoogle @Inject constructor(
    private val firebaseAuthRepository: AuthRepository
){

    suspend operator fun invoke(authCredential: AuthCredential) = firebaseAuthRepository.firebaseSignInWithGoogle(authCredential)
}