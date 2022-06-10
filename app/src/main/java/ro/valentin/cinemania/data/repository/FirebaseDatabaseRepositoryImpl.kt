package ro.valentin.cinemania.data.repository

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import ro.valentin.cinemania.core.Constants
import ro.valentin.cinemania.domain.model.Response
import ro.valentin.cinemania.domain.model.Seat
import ro.valentin.cinemania.domain.repository.FirebaseDatabaseRepository
import java.lang.Error
import javax.inject.Inject

class FirebaseDatabaseRepositoryImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) : FirebaseDatabaseRepository{

    override suspend fun getSeats(movieId: Int,
                                  selectedLocation: String,
                                  selectedDate: String,
                                  selectedTime: String
    )  = flow {
        try {
            emit(Response.Loading)
            val seatsRef = firebaseDatabase.getReference("movies").child(movieId.toString()).child(selectedLocation).child(selectedDate).child(selectedTime).child("seats")
            val seats = seatsRef.get().await().children.map { snapshot ->
                    snapshot.getValue(Seat::class.java)
            }
            emit(Response.Success(seats))
        } catch (e: Error) {
            emit(Response.Error(e.message ?: e.toString()))
        }
    }

    override suspend fun getSelectedSeatsByUser(
        movieId: Int,
        selectedLocation: String,
        selectedDate: String,
        selectedTime: String,
        userId: String
    ) = flow {
        val seatsSelectedByUser = mutableListOf<String>()
        try {
            emit(Response.Loading)

            val movieRef = firebaseDatabase.getReference("movies").child(movieId.toString()).child(selectedLocation).child(selectedDate).child(selectedTime).child("seats")
            movieRef.get().await().children.forEach { child ->
                    val lastUpdateIdFromDb = child.child("lastUpdate").value.toString()
                     val isAvailable = child.child("available").value.toString()
                    if(userId == lastUpdateIdFromDb && isAvailable == "false") {
                        Log.d(Constants.LOG_TAG, child.child("number").value.toString())
                        seatsSelectedByUser.add(child.child("number").value.toString())
                }
            }
            emit(Response.Success(seatsSelectedByUser))
        } catch (e: Error) {
            emit(Response.Error(e.message ?: e.toString()))
        }
    }
}