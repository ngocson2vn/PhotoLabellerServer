package com.mccorby.photolabeller.server.core.domain.repository

import com.mccorby.photolabeller.server.core.domain.model.ClientUpdate
import com.mccorby.photolabeller.server.core.domain.model.UpdatingRound
import java.io.File

interface ServerRepository {
    fun storeClientUpdate(gradientByteArray: ByteArray, samples: Int)
    fun listClientUpdates(): List<ClientUpdate>
    fun clearClientUpdates(): Boolean
    fun storeCurrentUpdatingRound(updatingRound: UpdatingRound)
    fun retrieveCurrentUpdatingRound(): UpdatingRound
    fun retrieveModel(): File
}