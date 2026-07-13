package model.raw

data class RawRelic(
    val name: String,
    val era: String,
    val drops: List<RawDrop>
)