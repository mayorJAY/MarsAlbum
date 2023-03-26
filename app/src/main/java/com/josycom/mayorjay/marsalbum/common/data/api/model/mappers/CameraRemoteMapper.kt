package com.josycom.mayorjay.marsalbum.common.data.api.model.mappers

import com.josycom.mayorjay.marsalbum.common.data.api.model.CameraRemote
import com.josycom.mayorjay.marsalbum.common.domain.model.Camera
import javax.inject.Inject

class CameraRemoteMapper @Inject constructor() : ApiMapper<CameraRemote?, Camera> {

    override fun mapToDomain(apiEntity: CameraRemote?): Camera {
        return Camera(
            fullName = apiEntity?.fullName.orEmpty().trim(),
            id = apiEntity?.id ?: -1,
            name = apiEntity?.name.orEmpty().trim(),
            roverId = apiEntity?.roverId ?: -1
        )
    }
}