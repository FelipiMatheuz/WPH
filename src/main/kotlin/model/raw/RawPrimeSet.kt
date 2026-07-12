package model.raw

import model.domain.prime.PrimeType

data class RawPrimeSet(
    val name: String,
    val type: PrimeType,
    val pageUrl: String,
    val imageUrl: String
)
