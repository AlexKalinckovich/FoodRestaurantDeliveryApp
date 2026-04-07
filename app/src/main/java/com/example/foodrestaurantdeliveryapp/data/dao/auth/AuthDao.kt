package com.example.foodrestaurantdeliveryapp.data.dao.auth

import com.example.foodrestaurantdeliveryapp.data.entity.user.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthDao @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private val collection = firestore.collection("users")

    fun listenToUser(uid: String): Flow<User?> = callbackFlow {
        if (uid.isEmpty()) {
            trySend(null)
            awaitClose { }
            return@callbackFlow
        }
        val registration = collection.document(uid)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                trySend(snapshot?.toObject(User::class.java))
            }
        awaitClose { registration.remove() }
    }

    suspend fun insertUser(user: User) {
        collection.document(user.uid).set(user).await()
    }

    suspend fun getUserById(uid: String): User? {
        return collection.document(uid).get(Source.SERVER)
            .await().toObject(User::class.java)
    }
}