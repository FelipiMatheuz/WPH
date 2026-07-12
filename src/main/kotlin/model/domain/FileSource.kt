package model.domain

enum class FileSource(val path: String) {
    RELICS("relics.json"),
    PRIME_SETS("prime-sets.json"),
    PRIME_COLLECTIONS("prime-collections.json"),
    MANIFEST("manifest.json"),

    IGNORED("ignored-prime-sets.txt")
}