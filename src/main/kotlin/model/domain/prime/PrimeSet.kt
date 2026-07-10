package model.domain.prime

import kotlinx.serialization.Serializable

@Serializable
data class PrimeSet(
    val id: String,
    val name: String,
    val type: PrimeType,
    val image: String,
    val components: List<PrimeComponent>
)
