package model.domain.prime

enum class PrimeType(
    val wikiTitle: String
) {

    WARFRAME("Warframe"),

    PRIMARY("Primary"),

    SECONDARY("Secondary"),

    MELEE("Melee"),

    ARCH_GUN("Archgun"),

    COMPANION("Companion"),

    ARCHWING("Archwing");

    companion object {

        fun fromWiki(title: String): PrimeType? =
            entries.firstOrNull {
                it.wikiTitle.equals(title, true)
            }
    }
}