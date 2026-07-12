package pipeline

import manager.FileManager

class ManifestPipeline: Pipeline {
    override fun run() {
        FileManager.exportManifest()
    }
}