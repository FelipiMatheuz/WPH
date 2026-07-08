import script.ManifestPipeline
import script.RelicPipeline

fun main(args: Array<String>) {

    when (args.firstOrNull()) {

        "relics" -> RelicPipeline().run()

        "manifest" -> ManifestPipeline().run()

        "all" -> {
            RelicPipeline().run()
            ManifestPipeline().run()
        }

        else -> error("Unknown pipeline")
    }
}
