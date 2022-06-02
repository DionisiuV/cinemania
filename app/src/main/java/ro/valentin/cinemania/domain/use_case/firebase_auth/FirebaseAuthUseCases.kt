package ro.valentin.cinemania.domain.use_case.firebase_auth

data class FirebaseAuthUseCases(
    val isUserAuthenticatedInFirebase: IsUserAuthenticatedInFirebase,
    val oneTapSignInGoogle: OneTapSignInGoogle,
    val oneTapSignUpGoogle: OneTapSignUpGoogle,
    val firebaseSignInWithGoogle: FirebaseSignInWithGoogle,
    val firebaseSignOut: FirebaseSignOut,
    val authStateListener: AuthStateListener,
    val getCurrentUser: GetCurrentUser
)