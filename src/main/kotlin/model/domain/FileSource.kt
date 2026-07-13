package model.domain

enum class FileSource(val path: String, val logName: String) {
    RELICS("relics.json", "Relics"),
    PRIME_SETS("prime-sets.json", "Prime Sets"),
    PRIME_COLLECTIONS("prime-collections.json", "Prime Collections"),
    MANIFEST("manifest.json", "Manifest"),

    IGNORED("ignored-prime-sets.txt", "Ignored Prime Sets")
}