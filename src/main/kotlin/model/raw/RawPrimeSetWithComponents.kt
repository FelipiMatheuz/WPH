package model.raw

import model.domain.prime.PrimeComponent

data class RawPrimeSetWithComponents(
    val rawSet: RawPrimeSet,
    val components: List<PrimeComponent>?
)