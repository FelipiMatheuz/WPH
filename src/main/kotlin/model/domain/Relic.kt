package model.domain

import kotlinx.serialization.Serializable

@Serializable
data class Relic(
    val id: String,
    val name: String,
    val era: String,
    val source: AcquisitionSource,
    val drops: List<Drop>
)
