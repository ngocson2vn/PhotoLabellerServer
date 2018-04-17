package com.mccorby.photolabeller.server.core.datasource

import com.mccorby.photolabeller.server.core.domain.model.ClientUpdate
import com.mccorby.photolabeller.server.core.domain.model.UpdatingRound
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.io.File
import java.util.*
import kotlin.test.assertEquals

internal class ServerRepositoryImplTest {

    @Mock
    private lateinit var fileDataSource: FileDataSource
    @Mock
    private lateinit var memoryDataSource: MemoryDataSource

    private lateinit var cut: ServerRepositoryImpl

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        cut = ServerRepositoryImpl(fileDataSource, memoryDataSource)
    }

    @Test
    fun `Given a new update when the repository stores it then the list of updates includes it`() {
        // Given
        val gradientByteArray = byteArrayOf()
        val gradientFile = mock<File>()
        val samples = 10
        whenever(fileDataSource.storeUpdate(gradientByteArray)).thenReturn(gradientFile)

        // When
        cut.storeClientUpdate(gradientByteArray, samples)

        // Then
        verify(fileDataSource).storeUpdate(gradientByteArray)
        verify(memoryDataSource).addUpdate(ClientUpdate(gradientFile, samples))
    }

    @Test
    fun `Given memory data source has values when a list of updates is requested then a list of updates is returned`() {
        // Given
        val clientUpdates = listOf(
                ClientUpdate(File("any"), 10),
                ClientUpdate(File("someOther"), 4))
        whenever(memoryDataSource.getUpdates()).thenReturn(clientUpdates)

        // When
        val listClientUpdates = cut.listClientUpdates()

        // Then
        verify(memoryDataSource).getUpdates()
        assertEquals(clientUpdates, listClientUpdates)
    }

    @Test
    fun `Given a updating round the repository stores it using the file data source`() {
        // Given
        val updatingRound = createUpdatingRound()

        // When
        cut.storeCurrentUpdatingRound(updatingRound)

        // Then
        verify(fileDataSource).saveUpdatingRound(updatingRound)
    }

    @Test
    fun `When the stored round is requested and it exists the repository returns the current round`() {
        // Given
        val updatingRound = createUpdatingRound()
        whenever(fileDataSource.retrieveCurrentUpdatingRound()).thenReturn(updatingRound)

        // When
        val result = cut.retrieveCurrentUpdatingRound()

        // Then
        verify(fileDataSource).retrieveCurrentUpdatingRound()
        assertEquals(updatingRound, result)
    }

    private fun createUpdatingRound() = UpdatingRound("model.zip", Date().time, 100_000, 3)

    @Test
    fun `When model file is requested the repository returns the last stored model`() {
        // Given
        val fileModel = mock<File>()
        whenever(fileDataSource.retrieveModel()).thenReturn(fileModel)

        // When
        val result = cut.retrieveModel()

        // Then
        verify(fileDataSource).retrieveModel()
        assertEquals(fileModel, result)
    }
}