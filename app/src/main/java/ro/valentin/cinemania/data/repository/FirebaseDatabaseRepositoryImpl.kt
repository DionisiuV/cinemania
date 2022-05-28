package ro.valentin.cinemania.data.repository

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import ro.valentin.cinemania.core.Constants.LOG_TAG
import ro.valentin.cinemania.domain.model.Response
import ro.valentin.cinemania.domain.model.Seat
import ro.valentin.cinemania.domain.repository.FirebaseDatabaseRepository
import java.lang.Error
import javax.inject.Inject

class FirebaseDatabaseRepositoryImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) : FirebaseDatabaseRepository{
    override suspend fun getSeats(movieId: Int)  = flow {
        try {
            emit(Response.Loading)

            Log.d(LOG_TAG, "Loading from firebasedbrepositoryimpl")

            val seatsRef = firebaseDatabase.getReference("seats")

            val seats = seatsRef.child(movieId.toString()).get().await().children.map { snapshot ->
                    snapshot.getValue(Seat::class.java)
            }

            emit(Response.Success(seats))
        } catch (e: Error) {
            emit(Response.Error(e.message ?: e.toString()))
        }
    }
}