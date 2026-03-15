package com.example.foodrestaurantdeliveryapp.data.dao.food

import com.example.foodrestaurantdeliveryapp.data.entity.food.FoodItem
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FoodDao @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private val collection = firestore.collection("foodItems")

    suspend fun getFoodItem(foodId: String): FoodItem? {
        return collection.document(foodId).get().await()
            .toObject(FoodItem::class.java)
    }

    suspend fun updateFoodItem(foodItem: FoodItem) {
        collection.document(foodItem.foodId).set(foodItem).await()
    }

    suspend fun insert(foodItem: FoodItem): String {
        val docRef = if (foodItem.foodId.isEmpty()) {
            collection.document()
        } else {
            collection.document(foodItem.foodId)
        }
        val item = foodItem.copy(foodId = docRef.id)
        docRef.set(item).await()
        return docRef.id
    }

    suspend fun deleteAll() {
        val snapshot = collection.get().await()
        val batch = firestore.batch()
        snapshot.documents.forEach { batch.delete(it.reference) }
        batch.commit().await()
    }

    suspend fun searchByTokens(tokens: List<String>): List<FoodItem> {
        if (tokens.isEmpty()) return getAll().first()
        val results = mutableListOf<FoodItem>()
        for (token in tokens) {
            val snapshot = collection
                .whereGreaterThanOrEqualTo("nameLowercase", token)
                .whereLessThanOrEqualTo("nameLowercase", token + "\uf8ff")
                .get()
                .await()
            results.addAll(snapshot.toObjects(FoodItem::class.java))
        }
        return results.distinctBy { it.foodId }
    }

    fun getAll(): Flow<List<FoodItem>> = callbackFlow {
        val registration = collection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                trySend(snapshot.toObjects(FoodItem::class.java))
            }
        }
        awaitClose { registration.remove() }
    }

    suspend fun getFoodByCategory(categoryId: String): List<FoodItem> {
        val snapshot = collection
            .whereEqualTo("categoryId", categoryId)
            .get()
            .await()
        return snapshot.toObjects(FoodItem::class.java)
    }

    suspend fun getCount(): Int {
        val snapshot = collection.count().get(AggregateSource.SERVER).await()
        return snapshot.count.toInt()
    }
}