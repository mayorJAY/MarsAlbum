package com.josycom.mayorjay.marsalbum.common.data.api.impl

import com.josycom.mayorjay.marsalbum.common.data.api.datasource.ManifestDataSource
import com.josycom.mayorjay.marsalbum.common.data.api.model.ManifestResponse
import com.josycom.mayorjay.marsalbum.common.data.api.model.PhotoManifestRemote
import com.josycom.mayorjay.marsalbum.common.data.api.model.mappers.PhotoManifestRemoteMapper
import com.josycom.mayorjay.marsalbum.common.domain.repositories.ManifestRepository
import com.josycom.mayorjay.marsalbum.common.util.Resource
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.mockito.Mockito

class ManifestRepositoryImplTest : TestCase() {

    private lateinit var sut: ManifestRepository
    private lateinit var dataSource: ManifestDataSource
    private lateinit var manifestMapper: PhotoManifestRemoteMapper

    override fun setUp() {
        dataSource = Mockito.spy(ManifestDataSource::class.java)
        manifestMapper = PhotoManifestRemoteMapper()
        sut = ManifestRepositoryImpl(dataSource, manifestMapper)
    }

    fun `test getRoverManifest dataSource_returns_null_manifest Error_Resource_propagated`() = runBlocking {
        Mockito.doReturn(ManifestResponse(null)).`when`(dataSource).getRoverManifest(Mockito.anyString())

        val responseResource = sut.getRoverManifest("opportunity")
        assertTrue(responseResource is Resource.Error)
    }

    fun `test getRoverManifest dataSource_returns_null_manifest error_message_propagated`() = runBlocking {
        Mockito.doReturn(ManifestResponse(null)).`when`(dataSource).getRoverManifest(Mockito.anyString())

        val responseResource = sut.getRoverManifest("opportunity")
        assertEquals("Manifest not found", (responseResource as Resource.Error).error?.message)
    }

    fun `test getRoverManifest dataSource_returns_valid_manifest Success_Resource_propagated`() = runBlocking {
        Mockito.doReturn(ManifestResponse(PhotoManifestRemote(1234))).`when`(dataSource).getRoverManifest(Mockito.anyString())

        val responseResource = sut.getRoverManifest("opportunity")
        assertTrue(responseResource is Resource.Success)
    }

    fun `test getRoverManifest dataSource_returns_valid_manifest valid_data_propagated`() = runBlocking {
        Mockito.doReturn(ManifestResponse(PhotoManifestRemote(1234))).`when`(dataSource).getRoverManifest(Mockito.anyString())

        val responseResource = sut.getRoverManifest("opportunity")
        assertEquals(1234, (responseResource as Resource.Success).data?.photoManifest?.maxSol)
    }
}