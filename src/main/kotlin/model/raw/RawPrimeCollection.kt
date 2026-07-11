package model.raw

data class RawPrimeCollection(
    val name: String,
    val released: Int,
    var promoUrl: String,
    val items: List<String>
)
