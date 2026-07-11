package consistency.model

import model.domain.prime.PrimeCollection
import model.domain.prime.PrimeSet
import model.domain.relic.Relic
import model.raw.Manifest

data class ConsistencyContext(

    val relics: List<Relic>,
    val primeSets: List<PrimeSet>,
    val primeCollections: List<PrimeCollection>,
    val manifest: Manifest

)