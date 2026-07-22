package model.raw

import model.domain.relic.Rarity

data class RawDrop(
    val itemName: String,
    val rarity: Rarity
)