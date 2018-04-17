package com.mccorby.photolabeller.server.core.datasource

import com.mccorby.photolabeller.server.core.domain.model.ClientUpdate
import com.mccorby.photolabeller.server.core.domain.model.UpdatingRound
import com.mccorby.photolabeller.server.core.domain.repository.ServerRepository
import java.io.File

class ServerRepositoryImpl(private val fileDataSource: FileDataSource, private val memoryDataSource: MemoryDataSource): ServerRepository {
    override fun listClientUpdates(): List<ClientUpdate> = memoryDataSource.getUpdates()

    override fun storeClientUpdate(gradientByteArray: ByteArray, samples: Int) {
        val file = fileDataSource.storeUpdate(gradientByteArray)
        memoryDataSource.addUpdate(ClientUpdate(file, samples))
    }

    override fun clearClientUpdates(): Boolean {
        memoryDataSource.clear()
        fileDataSource.clearUpdates()
        return true
    }

    override fun storeCurrentUpdatingRound(updatingRound: UpdatingRound) {
        fileDataSource.saveUpdatingRound(updatingRound)
    }

    override fun retrieveCurrentUpdatingRound(): UpdatingRound {
        return fileDataSource.retrieveCurrentUpdatingRound()
    }

    override fun retrieveModel(): File {
        return fileDataSource.retrieveModel()
    }
}