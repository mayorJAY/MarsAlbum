package com.josycom.mayorjay.marsalbum.common.data.api.model

import com.squareup.moshi.Json

data class PhotoResponse(val photos: List<PhotoRemote>?)

data class PhotoRemote(
    val camera: CameraRemote? = null,
    @Json(name = "earth_date")
    val earthDate: String? = null,
    val id: Int? = null,
    @Json(name = "img_src")
    val imgSrc: String? = null,
    val rover: RoverRemote? = null,
    val sol: Int? = null
)

data class CameraRemote(
    @Json(name = "full_name")
    val fullName: String? = null,
    val id: Int? = null,
    val name: String? = null,
    @Json(name = "rover_id")
    val roverId: Int? = null
)

data class RoverRemote(
    val id: Int? = null,
    @Json(name = "landing_date")
    val landingDate: String? = null,
    @Json(name = "launch_date")
    val launchDate: String? = null,
    val name: String? = null,
    val status: String? = null
)