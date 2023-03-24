package com.josycom.mayorjay.marsalbum.common.data.api.model.mappers

import com.josycom.mayorjay.marsalbum.common.data.api.model.PhotoResponse
import com.josycom.mayorjay.marsalbum.common.domain.model.PhotoAlbum

class PhotoResponseMapper(
    private val photoMapper: PhotoRemoteMapper
    ): ApiMapper<PhotoResponse, PhotoAlbum> {

    override fun mapToDomain(apiEntity: PhotoResponse): PhotoAlbum {
        return PhotoAlbum(
            photos = apiEntity.photos?.map { photoMapper.mapToDomain(it) }.orEmpty()
        )
    }
}