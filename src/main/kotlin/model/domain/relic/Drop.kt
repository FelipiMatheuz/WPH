package model.domain.relic

import kotlinx.serialization.Serializable

@Serializable
data class Drop(
    val id: String,
    val itemName: String,
    val component: String,
    val rarity: String
)
