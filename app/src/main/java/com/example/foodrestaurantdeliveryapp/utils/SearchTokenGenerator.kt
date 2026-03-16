package com.example.foodrestaurantdeliveryapp.utils

object SearchTokenGenerator {
    fun generateTokens(text: String, maxLength: Int = 10): List<String> {
        val normalized = text.lowercase().trim()
        val tokens = mutableListOf<String>()
        for (i in 1..minOf(normalized.length, maxLength)) {
            tokens.add(normalized.take(i))
        }
        return tokens
    }

    fun generateTokensForSearch(query: String): List<String> {
        return query.lowercase().trim()
            .split(" ")
            .filter { it.isNotEmpty() }
            .flatMap { generateTokens(it, maxLength = 5) }
            .distinct()
    }

    fun generateTokensForFuzzy(text: String, maxLength: Int = 5): List<String> {
        val normalized = text.lowercase().trim()
        val tokens = mutableListOf<String>()
        for (i in 2..minOf(normalized.length, maxLength)) {
            tokens.add(normalized.take(i))
            if (i >= 3) {
                tokens.add(normalized.drop(normalized.length - i))
            }
        }
        return tokens.distinct()
    }
}