package com.example.foodrestaurantdeliveryapp.data.dao.restaurant

import com.example.foodrestaurantdeliveryapp.data.entity.restaurant.Restaurant
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Source
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RestaurantDao @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private val collection = firestore.collection("restaurants")

    fun getAllRestaurants(): Flow<List<Restaurant>> = callbackFlow {
        trySend(emptyList())

        val registration = collection
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val restaurants = snapshot.toObjects(Restaurant::class.java)
                    trySend(restaurants)
                }
            }
        awaitClose { registration.remove() }
    }

    suspend fun getAllRestaurantsOnce(): List<Restaurant> {
        return try {
            val snapshot = collection
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get(Source.SERVER)
                .await()
            snapshot.toObjects(Restaurant::class.java)
        } catch (e: Exception) {
            collection
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get(Source.CACHE)
                .await()
                .toObjects(Restaurant::class.java)
        }
    }

    fun getRestaurantById(id: String): Flow<Restaurant?> = callbackFlow {
        trySend(null)

        val docRef = collection.document(id)
        val registration = docRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            trySend(snapshot?.toObject(Restaurant::class.java))
        }
        awaitClose { registration.remove() }
    }

    suspend fun getRestaurantByIdOnce(id: String): Restaurant? {
        return try {
            val snapshot = collection.document(id)
                .get(Source.SERVER)
                .await()
            snapshot.toObject(Restaurant::class.java)
        } catch (e: Exception) {
            collection.document(id)
                .get(Source.CACHE)
                .await()
                .toObject(Restaurant::class.java)
        }
    }

    suspend fun insert(restaurant: Restaurant): String {
        val docRef = if (restaurant.restaurantId.isEmpty()) {
            collection.document()
        } else {
            collection.document(restaurant.restaurantId)
        }
        val item = restaurant.copy(restaurantId = docRef.id)
        docRef.set(item).await()
        return docRef.id
    }

    suspend fun update(restaurant: Restaurant) {
        collection.document(restaurant.restaurantId).set(restaurant).await()
    }

    suspend fun delete(restaurant: Restaurant) {
        collection.document(restaurant.restaurantId).delete().await()
    }

    suspend fun deleteAll() {
        val snapshot = collection.get().await()
        val batch = firestore.batch()
        snapshot.documents.forEach { batch.delete(it.reference) }
        batch.commit().await()
    }

    suspend fun searchByTokens(tokens: List<String>): List<Restaurant> {
        if (tokens.isEmpty()) return getAllRestaurantsOnce()
        val results = mutableListOf<Restaurant>()
        for (token in tokens) {
            val nameSnapshot = collection
                .whereGreaterThanOrEqualTo("nameLowercase", token)
                .whereLessThanOrEqualTo("nameLowercase", token + "\uf8ff")
                .get(Source.SERVER)
                .await()
            results.addAll(nameSnapshot.toObjects(Restaurant::class.java))

            val addressSnapshot = collection
                .whereGreaterThanOrEqualTo("addressLowercase", token)
                .whereLessThanOrEqualTo("addressLowercase", token + "\uf8ff")
                .get(Source.SERVER)
                .await()
            results.addAll(addressSnapshot.toObjects(Restaurant::class.java))
        }
        return results.distinctBy { it.restaurantId }
    }

    suspend fun searchByDeliveryFee(maxFee: Double): List<Restaurant> {
        val snapshot = collection
            .whereLessThanOrEqualTo("deliveryFee", maxFee)
            .get(Source.SERVER)
            .await()
        return snapshot.toObjects(Restaurant::class.java)
    }

    suspend fun waitForData(timeout: Long = 5000L): Boolean {
        return try {
            val snapshot = collection.limit(1).get(Source.SERVER).await()
            snapshot.documents.isNotEmpty()
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getCount(): Int {
        val snapshot = collection.count().get(AggregateSource.SERVER).await()
        return snapshot.count.toInt()
    }
}