package pipeline

import manager.FileManager
import extractor.PrimeSetExtractor
import misc.PrimeSetSyncService
import normalizer.PrimeSetNormalizer
import remote.DataSources
import remote.HtmlDownloader

class PrimeSetPipeline(
    private val downloader: HtmlDownloader = HtmlDownloader(),
    private val extractor: PrimeSetExtractor = PrimeSetExtractor(),
    private val normalizer: PrimeSetNormalizer = PrimeSetNormalizer()
) : Pipeline {
    override fun run() {
        val primeDocument = downloader.download(DataSources.PRIME_SETS)
        val rawPrimeSets = extractor.extract(primeDocument)
        val syncResult = PrimeSetSyncService().sync(rawPrimeSets)

        if (syncResult.newPrimeSets.isEmpty()) {
            println("Prime Sets are already up to date.")
            return
        }

        val normalizedPrimeSets = normalizer.normalize(syncResult.newPrimeSets)

        FileManager.exportPrimeSets(syncResult.existing + normalizedPrimeSets)
    }
}