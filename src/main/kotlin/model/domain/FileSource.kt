package model.domain

enum class FileSource(val path: String) {
    RELICS("relics.json"),
    PRIME_SETS("prime_sets.json"),
    PRIME_COLLECTIONS("prime_collections.json"),
    MANIFEST("manifest.json")
}