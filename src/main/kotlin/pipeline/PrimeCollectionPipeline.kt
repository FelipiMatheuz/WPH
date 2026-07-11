package pipeline

import exporter.JsonExporter
import extractor.PrimeCollectionExtractor
import extractor.PrimeCollectionImageExtractor
import misc.PrimeCollectionSyncService
import normalizer.PrimeCollectionNormalizer
import remote.DataSources
import remote.HtmlDownloader
import validator.PrimeCollectionValidator

class PrimeCollectionPipeline: Pipeline {
    override fun run() {
        val collectionDocument = HtmlDownloader().download(DataSources.PRIME_COLLECTION)
        val currentCollection = PrimeCollectionExtractor().extract(collectionDocument)

        if (PrimeCollectionSyncService().collectionExists(currentCollection)) {
            println("Prime Collections are already up to date.")
            return
        }

        val promoImageDocument = HtmlDownloader().download(DataSources.PRIME_ACCESS)
        val promoImage = PrimeCollectionImageExtractor().extract(promoImageDocument)
        currentCollection.promoUrl = promoImage

        val primeCollections = PrimeCollectionNormalizer().normalize(currentCollection)

        PrimeCollectionValidator().validate(primeCollections)
        JsonExporter().exportPrimeCollections(primeCollections)
    }
}