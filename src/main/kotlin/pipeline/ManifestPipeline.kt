package pipeline

import exporter.JsonExporter

class ManifestPipeline: Pipeline {
    override fun run() {
        JsonExporter().exportManifest()
    }
}