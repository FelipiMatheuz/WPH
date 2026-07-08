package model.raw

import kotlinx.serialization.Serializable

@Serializable
data class Manifest(
    val generatedAt: String,
    val generatorVersion: String,
    val files: List<ManifestFile>
)
