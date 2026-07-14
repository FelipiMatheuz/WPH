package pipeline

import manager.FileManager
import extractor.PrimeSetExtractor
import logging.Logger
import datasync.PrimeSetSyncService
import normalizer.PrimeSetNormalizer
import remote.DataSources
import remote.HtmlDownloader

class PrimeSetPipeline(
    private val downloader: HtmlDownloader = HtmlDownloader(),
    private val extractor: PrimeSetExtractor = PrimeSetExtractor(),
    private val syncer: PrimeSetSyncService = PrimeSetSyncService(),
    private val normalizer: PrimeSetNormalizer = PrimeSetNormalizer()
) : Pipeline {
    override fun run() {
        Logger.pipelineSection("Prime sets")
        val primeDocument = downloader.download(DataSources.PRIME_SETS)
        val rawPrimeSets = extractor.extract(primeDocument)
        val syncResult = syncer.sync(rawPrimeSets)

        if (syncResult.newPrimeSets.isEmpty()) {
            Logger.info("Prime Sets are already up to date.")
            Logger.pipelineSuccess()
            return
        }

        val normalizedPrimeSets = normalizer.normalize(syncResult.newPrimeSets)

        FileManager.exportPrimeSets(syncResult.existing + normalizedPrimeSets)
        Logger.pipelineSuccess()
    }
}