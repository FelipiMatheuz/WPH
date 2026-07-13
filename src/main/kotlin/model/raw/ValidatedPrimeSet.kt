package model.raw

import model.domain.prime.PrimeComponent

data class ValidatedPrimeSet(val rawSet: RawPrimeSet, val components: List<PrimeComponent>)