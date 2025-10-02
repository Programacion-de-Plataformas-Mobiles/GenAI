package com.example.genai.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Esquema para la respuesta estructurada de la API de OpenAI.
 */
@JsonClass(generateAdapter = true)
data class RealEstateResponse(
    @Json(name = "agent_summary") val agentSummary: String,
    @Json(name = "property_listings") val propertyListings: List<PropertyListing>?
)

@JsonClass(generateAdapter = true)
data class PropertyListing(
    @Json(name = "address") val address: String,
    @Json(name = "price_usd") val priceUsd: Double,
    @Json(name = "bedrooms") val bedrooms: Int,
    @Json(name = "bathrooms") val bathrooms: Double,
    @Json(name = "description") val description: String,
    @Json(name = "publication_url") val publicationUrl: String, // URL de la publicación
    @Json(name = "image_url") val imageUrl: String // URL de la imagen
)
