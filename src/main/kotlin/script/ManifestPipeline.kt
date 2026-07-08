package script

import exporter.JsonExporter

class ManifestPipeline: Pipeline {
    override fun run() {
        JsonExporter().exportManifest()
    }
}