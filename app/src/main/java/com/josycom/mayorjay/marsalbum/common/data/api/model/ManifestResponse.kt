package com.josycom.mayorjay.marsalbum.common.data.api.model

import com.squareup.moshi.Json

data class ManifestResponse(@Json(name = "photo_manifest")
                            val photoManifest: PhotoManifestRemote?)

data class PhotoManifestRemote(
    @Json(name = "max_sol")
    val maxSol: Int? = null,
    val name: String? = null,
    val status: String? = null
)