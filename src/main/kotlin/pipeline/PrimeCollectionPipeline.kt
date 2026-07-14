package pipeline

import extractor.PrimeCollectionExtractor
import extractor.PrimeCollectionImageExtractor
import logging.Logger
import manager.FileManager
import datasync.PrimeCollectionSyncService
import normalizer.PrimeCollectionNormalizer
import remote.DataSources
import remote.HtmlDownloader

class PrimeCollectionPipeline(
    private val downloader: HtmlDownloader = HtmlDownloader(),
    private val extractor: PrimeCollectionExtractor = PrimeCollectionExtractor(),
    private val imageExtractor: PrimeCollectionImageExtractor = PrimeCollectionImageExtractor(),
    private val syncer: PrimeCollectionSyncService = PrimeCollectionSyncService(),
    private val normalizer: PrimeCollectionNormalizer = PrimeCollectionNormalizer()
) : Pipeline {
    override fun run() {
        Logger.pipelineSection("Prime collections")
        val collectionDocument = downloader.download(DataSources.PRIME_COLLECTION)
        val currentCollection = extractor.extract(collectionDocument)

        if (syncer.isSynced(currentCollection)) {
            Logger.info("Prime Collections are already up to date.")
            Logger.pipelineSuccess()
            return
        }

        val promoImageDocument = downloader.download(DataSources.PRIME_ACCESS)
        val promoImage = imageExtractor.extract(promoImageDocument)

        val primeCollections = normalizer.normalize(currentCollection, promoImage)

        FileManager.exportPrimeCollections(primeCollections)
        Logger.pipelineSuccess()
    }
}