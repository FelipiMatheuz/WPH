package model.raw

import kotlinx.serialization.Serializable

@Serializable
data class ManifestFile(
    val name: String,
    val size: Long,
    val sha256: String
)
