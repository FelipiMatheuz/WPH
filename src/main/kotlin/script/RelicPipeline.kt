package script

import exporter.JsonExporter
import extractor.RelicExtractor
import extractor.RelicSourceExtractor
import normalizer.RelicNormalizer
import remote.DataSources
import remote.HtmlDownloader
import validator.RelicValidator

class RelicPipeline : Pipeline {
    override fun run() {
        val dropTableDocument = HtmlDownloader().download(DataSources.DROP_TABLE)
        val rawRelics = RelicExtractor().extract(dropTableDocument)

        val voidDocument = HtmlDownloader().download(DataSources.VOID_RELIC)
        val resurgenceDocument = HtmlDownloader().download(DataSources.PRIME_RESURGENCE)
        val rawRelicSource = RelicSourceExtractor().extract(voidDocument, resurgenceDocument)

        val relics = RelicNormalizer().normalize(rawRelics, rawRelicSource)

        RelicValidator().validate(relics)

        JsonExporter().exportRelics(relics)
    }
}