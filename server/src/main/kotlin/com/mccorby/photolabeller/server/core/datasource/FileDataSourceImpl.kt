package com.mccorby.photolabeller.server.core.datasource

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.mccorby.photolabeller.server.core.domain.model.UpdatingRound
import org.apache.commons.io.FileUtils
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

class FileDataSourceImpl(private val rootDir: Path): FileDataSource {

    companion object {
        const val defaultRoundDir = "currentRound"
        const val currentRoundFileName = "currentRound.json"
        const val defaultModelFile = "model.zip"
    }

    override fun storeUpdate(gradientByteArray: ByteArray): File {
        File(rootDir.toString(), "client_updates").apply { mkdir() }
        val file = generateFileName()
        FileUtils.writeByteArrayToFile(file, gradientByteArray)
        return file
    }

    override fun clearUpdates() {
        FileUtils.deleteDirectory(Paths.get(rootDir.toString(), defaultRoundDir).toFile())
    }

    override fun saveUpdatingRound(updatingRound: UpdatingRound) {
        jacksonObjectMapper().writeValue(getCurrentRoundJsonFile(), updatingRound)
    }

    override fun retrieveCurrentUpdatingRound(): UpdatingRound =
            jacksonObjectMapper().readValue(getCurrentRoundJsonFile())

    private fun getCurrentRoundJsonFile(): File {
        return Paths.get(rootDir.toString(), currentRoundFileName).toFile()
    }

    override fun retrieveModel(): File {
        return Paths.get(rootDir.toString(), defaultModelFile).toFile()
    }

    // TODO We could have a file name generator and pass it as a dependence to this class
    private fun generateFileName(): File  {
        val timeStamp = Date().time
        val fileName = "_" + timeStamp + "_"
        val path = Paths.get(rootDir.toString(), defaultRoundDir, fileName)
        return File(path.toString())
    }

}