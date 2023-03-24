package com.josycom.mayorjay.marsalbum.common.data.api.model

import com.squareup.moshi.Json

data class PhotoResponse(val photos: List<PhotoRemote>?)

data class PhotoRemote(
    val camera: CameraRemote?,
    @Json(name = "earth_date")
    val earthDate: String?,
    val id: Int?,
    @Json(name = "img_src")
    val imgSrc: String?,
    val rover: RoverRemote?,
    val sol: Int?
)

data class CameraRemote(
    @Json(name = "full_name")
    val fullName: String?,
    val id: Int?,
    val name: String?,
    @Json(name = "rover_id")
    val roverId: Int?
)

data class RoverRemote(
    val id: Int?,
    @Json(name = "landing_date")
    val landingDate: String?,
    @Json(name = "launch_date")
    val launchDate: String?,
    val name: String?,
    val status: String?
)