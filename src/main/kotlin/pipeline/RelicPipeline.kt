package pipeline

import json.JsonManager
import extractor.RelicExtractor
import extractor.RelicSourceExtractor
import normalizer.RelicNormalizer
import remote.DataSources
import remote.HtmlDownloader

class RelicPipeline(
    private val downloader: HtmlDownloader = HtmlDownloader(),
    private val extractor: RelicExtractor = RelicExtractor(),
    private val sourceExtractor: RelicSourceExtractor = RelicSourceExtractor(),
    private val normalizer: RelicNormalizer = RelicNormalizer()
) : Pipeline {
    override fun run() {
        val dropTableDocument = downloader.download(DataSources.DROP_TABLE)
        val rawRelics = extractor.extract(dropTableDocument)

        val voidDocument = downloader.download(DataSources.VOID_RELIC)
        val resurgenceDocument = downloader.download(DataSources.PRIME_RESURGENCE)
        val rawRelicSource = sourceExtractor.extract(voidDocument, resurgenceDocument)

        val relics = normalizer.normalize(rawRelics, rawRelicSource)

        JsonManager.exportRelics(relics)
    }
}