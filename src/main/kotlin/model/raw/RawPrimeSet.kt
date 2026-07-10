package model.raw

import model.domain.prime.PrimeComponent
import model.domain.prime.PrimeType

data class RawPrimeSet(
    val name: String,
    val type: PrimeType,
    val imageUrl: String,
    val pageUrl: String,
    var components: List<PrimeComponent>?
)
