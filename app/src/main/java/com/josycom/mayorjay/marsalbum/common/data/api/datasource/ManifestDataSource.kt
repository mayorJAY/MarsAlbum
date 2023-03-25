package com.josycom.mayorjay.marsalbum.common.data.api.datasource

import com.josycom.mayorjay.marsalbum.common.data.api.model.ManifestResponse

interface ManifestDataSource {

    suspend fun getRoverManifest(roverName: String): ManifestResponse
}