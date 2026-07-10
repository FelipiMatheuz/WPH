package model.domain

enum class FileSource(val path: String) {
    RELICS("relics.json"),

    PRIME_SETS("prime_sets.json"),
    MANIFEST("manifest.json")
}