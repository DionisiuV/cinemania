package ro.valentin.cinemania.domain.repository

import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import ro.valentin.cinemania.domain.model.Response

interface AuthRepository {
    fun isUserAuthenticatedInFirebase(): Boolean
    suspend fun oneTapSignInGoogle(): Flow<Response<BeginSignInResult>>
    suspend fun oneTapSignUpGoogle(): Flow<Response<BeginSignInResult>>
    suspend fun firebaseSignInWithGoogle(authCredential: AuthCredential): Flow<Response<Boolean>>
    suspend fun firebaseSignOut(): Flow<Response<Boolean>>
    fun authStateListener(): Flow<Boolean>
    fun getCurrentUser(): FirebaseUser?
}