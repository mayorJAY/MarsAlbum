package com.josycom.mayorjay.marsalbum.common.data.api.model.mappers

import com.josycom.mayorjay.marsalbum.common.data.api.model.RoverRemote
import com.josycom.mayorjay.marsalbum.common.domain.model.Rover
import javax.inject.Inject

class RoverRemoteMapper @Inject constructor() : ApiMapper<RoverRemote?, Rover> {

    override fun mapToDomain(apiEntity: RoverRemote?): Rover {
        return Rover(
            id = apiEntity?.id ?: -1,
            landingDate = apiEntity?.landingDate.orEmpty().trim(),
            launchDate = apiEntity?.launchDate.orEmpty().trim(),
            name = apiEntity?.name.orEmpty().trim(),
            status = apiEntity?.status.orEmpty().trim()
        )
    }
}