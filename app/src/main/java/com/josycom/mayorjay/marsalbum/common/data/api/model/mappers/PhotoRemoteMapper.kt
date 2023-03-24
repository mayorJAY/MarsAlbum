package com.josycom.mayorjay.marsalbum.common.data.api.model.mappers

import com.josycom.mayorjay.marsalbum.common.data.api.model.PhotoRemote
import com.josycom.mayorjay.marsalbum.common.domain.model.Photo
import javax.inject.Inject

class PhotoRemoteMapper @Inject constructor(
    private val cameraMapper: CameraRemoteMapper,
    private val roverMapper: RoverRemoteMapper
    ): ApiMapper<PhotoRemote, Photo> {

    override fun mapToDomain(apiEntity: PhotoRemote): Photo {
        return Photo(
            camera = cameraMapper.mapToDomain(apiEntity.camera),
            earthDate = apiEntity.earthDate.orEmpty(),
            id = apiEntity.id ?: -1,
            imgSrc = apiEntity.imgSrc.orEmpty(),
            rover = roverMapper.mapToDomain(apiEntity.rover),
            sol = apiEntity.sol ?: -1
        )
    }
}