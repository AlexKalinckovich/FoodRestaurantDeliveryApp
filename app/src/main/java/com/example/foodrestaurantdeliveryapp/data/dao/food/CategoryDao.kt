package com.example.foodrestaurantdeliveryapp.data.dao.food

import com.example.foodrestaurantdeliveryapp.data.entity.food.Category
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
class CategoryDao @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private val collection = firestore.collection("categories")

    fun getAllCategories(): Flow<List<Category>> = callbackFlow {
        trySend(emptyList())

        val registration = collection
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    trySend(snapshot.toObjects(Category::class.java))
                }
            }
        awaitClose { registration.remove() }
    }

    suspend fun getAllCategoriesOnce(): List<Category> {
        return try {
            val snapshot = collection
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get(Source.SERVER)
                .await()
            snapshot.toObjects(Category::class.java)
        } catch (e: Exception) {
            collection
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get(Source.SERVER)
                .await()
                .toObjects(Category::class.java)
        }
    }

    suspend fun insert(category: Category): String {
        val docRef = if (category.categoryId.isEmpty()) {
            collection.document()
        } else {
            collection.document(category.categoryId)
        }
        val item = category.copy(categoryId = docRef.id)
        docRef.set(item).await()
        return docRef.id
    }

    suspend fun getCount(): Int {
        val snapshot = collection.count().get(AggregateSource.SERVER).await()
        return snapshot.count.toInt()
    }

    suspend fun deleteAll() {
        val snapshot = collection.get().await()
        val batch = firestore.batch()
        snapshot.documents.forEach { batch.delete(it.reference) }
        batch.commit().await()
    }

    suspend fun searchByTokens(tokens: List<String>): List<Category> {
        if (tokens.isEmpty()) return getAllCategoriesOnce()
        val results = mutableListOf<Category>()
        for (token in tokens) {
            val snapshot = collection
                .whereGreaterThanOrEqualTo("nameLowercase", token)
                .whereLessThanOrEqualTo("nameLowercase", token + "\uf8ff")
                .get(Source.SERVER)
                .await()
            results.addAll(snapshot.toObjects(Category::class.java))
        }
        return results.distinctBy { it.categoryId }
    }

    suspend fun getCategoryById(categoryId: String): Category? {
        return collection.document(categoryId)
            .get(Source.SERVER)
            .await()
            .toObject(Category::class.java)
    }
}