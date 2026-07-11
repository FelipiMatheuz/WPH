package model.domain.prime

import kotlinx.serialization.Serializable

@Serializable
data class PrimeCollection(
    val id: String,
    val name: String,
    val promoImage: String,
    val released: Int,
    val primeSets: List<String>
)