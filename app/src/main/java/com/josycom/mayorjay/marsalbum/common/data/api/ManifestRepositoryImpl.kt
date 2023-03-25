package com.josycom.mayorjay.marsalbum.common.data.api

import com.josycom.mayorjay.marsalbum.common.data.api.datasource.ManifestDataSource
import com.josycom.mayorjay.marsalbum.common.data.api.model.mappers.PhotoManifestRemoteMapper
import com.josycom.mayorjay.marsalbum.common.domain.model.Manifest
import com.josycom.mayorjay.marsalbum.common.domain.repositories.ManifestRepository
import javax.inject.Inject

class ManifestRepositoryImpl @Inject constructor(
    private val dataSource: ManifestDataSource,
    private val manifestMapper: PhotoManifestRemoteMapper
    ): ManifestRepository {

    override suspend fun getRoverManifest(roverName: String): Manifest {
        val manifestResponse = dataSource.getRoverManifest(roverName)
        return Manifest(manifestMapper.mapToDomain(manifestResponse.photoManifest))
    }
}