package model.domain.prime

import kotlinx.serialization.Serializable

@Serializable
data class PrimeComponent(
    val id: String,
    val part: PrimePart,
    val quantity: Int
)