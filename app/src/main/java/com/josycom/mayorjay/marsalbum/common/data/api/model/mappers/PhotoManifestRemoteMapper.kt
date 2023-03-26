package com.josycom.mayorjay.marsalbum.common.data.api.model.mappers

import com.josycom.mayorjay.marsalbum.common.data.api.model.PhotoManifestRemote
import com.josycom.mayorjay.marsalbum.common.domain.model.PhotoManifest
import javax.inject.Inject

class PhotoManifestRemoteMapper @Inject constructor() : ApiMapper<PhotoManifestRemote?, PhotoManifest> {

    override fun mapToDomain(apiEntity: PhotoManifestRemote?): PhotoManifest {
        return PhotoManifest(
            maxSol = apiEntity?.maxSol ?: -1,
            name = apiEntity?.name.orEmpty().trim(),
            status = apiEntity?.status.orEmpty().trim()
        )
    }
}