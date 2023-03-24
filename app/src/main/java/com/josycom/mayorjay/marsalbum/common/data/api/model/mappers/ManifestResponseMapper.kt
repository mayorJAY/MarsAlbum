package com.josycom.mayorjay.marsalbum.common.data.api.model.mappers

import com.josycom.mayorjay.marsalbum.common.data.api.model.ManifestResponse
import com.josycom.mayorjay.marsalbum.common.domain.model.Manifest

class ManifestResponseMapper(
    private val manifestMapper: PhotoManifestRemoteMapper
    ): ApiMapper<ManifestResponse, Manifest> {

    override fun mapToDomain(apiEntity: ManifestResponse): Manifest {
        return Manifest(
            photoManifest = manifestMapper.mapToDomain(apiEntity.photoManifest)
        )
    }
}