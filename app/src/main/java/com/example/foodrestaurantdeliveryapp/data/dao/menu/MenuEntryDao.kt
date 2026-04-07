package com.example.foodrestaurantdeliveryapp.data.dao.menu

import com.example.foodrestaurantdeliveryapp.data.entity.food.FoodItem
import com.example.foodrestaurantdeliveryapp.data.entity.menu.MenuEntry
import com.example.foodrestaurantdeliveryapp.data.repository.model.menu.model.MenuEntryWithFood
import com.example.foodrestaurantdeliveryapp.data.repository.model.menu.model.MenuWithDetails
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.WriteBatch
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MenuEntryDao @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private val collection = firestore.collection("menuEntries")

    fun getMenuForRestaurant(restaurantId: String): Flow<List<MenuWithDetails>> = callbackFlow {
        trySend(emptyList())

        val registration = collection
            .whereEqualTo("restaurantId", restaurantId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val menuEntries = snapshot.toObjects(MenuEntry::class.java)
                    trySend(menuEntries.map { it.toMenuWithDetails() })
                }
            }
        awaitClose { registration.remove() }
    }

    suspend fun getMenuForRestaurantOnce(restaurantId: String): List<MenuWithDetails> {
        return try {
            val snapshot = collection
                .whereEqualTo("restaurantId", restaurantId)
                .get(Source.SERVER)
                .await()
            val menuEntries = snapshot.toObjects(MenuEntry::class.java)
            menuEntries.map { it.toMenuWithDetails() }
        } catch (e: Exception) {
            val snapshot = collection
                .whereEqualTo("restaurantId", restaurantId)
                .get(Source.SERVER)
                .await()
            val menuEntries = snapshot.toObjects(MenuEntry::class.java)
            menuEntries.map { it.toMenuWithDetails() }
        }
    }

    fun getAll(): Flow<List<MenuEntry>> = callbackFlow {
        trySend(emptyList())

        val registration = collection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                trySend(snapshot.toObjects(MenuEntry::class.java))
            }
        }
        awaitClose { registration.remove() }
    }

    suspend fun insertAll(vararg menuEntries: MenuEntry) {
        val batch: WriteBatch = firestore.batch()
        menuEntries.forEach { entry ->
            val docRef = collection.document()
            val item = entry.copy(menuEntryId = docRef.id)
            batch.set(docRef, item)
        }
        batch.commit().await()
    }

    suspend fun getMenuEntryWithFood(menuId: String): MenuEntryWithFood? {
        val document = collection.document(menuId).get(Source.SERVER).await()
        val entry = document.toObject(MenuEntry::class.java) ?: return null
        val foodItem = FoodItem(
            foodId = entry.foodId,
            categoryId = entry.categoryId ?: "",
            categoryName = entry.category ?: "",
            name = entry.foodName,
            nameLowercase = entry.foodName.lowercase(),
            description = entry.foodDescription,
            descriptionLowercase = entry.foodDescription.lowercase(),
            imageUrl = entry.foodImageUrl,
            searchTokens = emptyList(),
            createdAt = entry.createdAt,
            updatedAt = entry.updatedAt
        )
        return MenuEntryWithFood(
            menuEntry = entry,
            foodItem = foodItem
        )
    }

    suspend fun getMenuEntry(menuId: String): MenuEntry? {
        return collection.document(menuId)
            .get(Source.SERVER)
            .await()
            .toObject(MenuEntry::class.java)
    }

    suspend fun updateMenuEntry(menuEntry: MenuEntry) {
        collection.document(menuEntry.menuEntryId).set(menuEntry).await()
    }

    suspend fun insertMenuEntry(menuEntry: MenuEntry): String {
        val docRef = collection.document()
        val item = menuEntry.copy(menuEntryId = docRef.id)
        docRef.set(item).await()
        return docRef.id
    }

    suspend fun deleteAll() {
        val snapshot = collection.get().await()
        val batch = firestore.batch()
        snapshot.documents.forEach { batch.delete(it.reference) }
        batch.commit().await()
    }

    suspend fun searchByTokens(tokens: List<String>): List<MenuEntry> {
        if (tokens.isEmpty()) return getAll().first()
        val results = mutableListOf<MenuEntry>()
        for (token in tokens) {
            val snapshot = collection
                .whereGreaterThanOrEqualTo("searchTokens", token)
                .whereLessThanOrEqualTo("searchTokens", token + "\uf8ff")
                .get(Source.SERVER)
                .await()
            results.addAll(snapshot.toObjects(MenuEntry::class.java))
        }
        return results.distinctBy { it.menuEntryId }
    }

    suspend fun getByRestaurantAndFood(restaurantId: String, foodId: String): MenuEntry? {
        val snapshot = collection
            .whereEqualTo("restaurantId", restaurantId)
            .whereEqualTo("foodId", foodId)
            .get(Source.SERVER)
            .await()
        return snapshot.documents.firstOrNull()?.toObject(MenuEntry::class.java)
    }

    suspend fun getCount(): Int {
        val snapshot = collection.count().get(AggregateSource.SERVER).await()
        return snapshot.count.toInt()
    }
}

private fun MenuEntry.toMenuWithDetails(): MenuWithDetails {
    return MenuWithDetails(
        menuId = menuEntryId,
        name = foodName,
        description = foodDescription,
        imageUrl = foodImageUrl,
        price = price,
        isAvailable = isAvailable
    )
}