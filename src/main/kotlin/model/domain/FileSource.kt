package model.domain

enum class FileSource(val path: String) {
    RELICS("relics.json"),
    MANIFEST("manifest.json")
}