package model.raw

import model.domain.prime.PrimeSet

data class PrimeSetSyncResult(
    val existing: List<PrimeSet>,
    val newPrimeSets: List<RawPrimeSetWithComponents>
)
