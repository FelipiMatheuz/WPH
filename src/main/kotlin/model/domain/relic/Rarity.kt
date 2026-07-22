package model.domain.relic

enum class Rarity(val rarity: String, val percentage: String) {
    COMMON("Common", "25.33%"),
    UNCOMMON("Uncommon", "11.00%"),
    RARE("Rare", "2.00%");

    companion object {

        fun fromTable(named: String, percentage: String): Rarity? =
            Rarity.entries.firstOrNull {
                it.percentage == percentage ||
                        it.rarity.equals(named, true)
            }
    }
}