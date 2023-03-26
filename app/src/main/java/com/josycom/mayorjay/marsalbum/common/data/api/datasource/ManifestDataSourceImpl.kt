package com.josycom.mayorjay.marsalbum.common.data.api.datasource

import com.josycom.mayorjay.marsalbum.common.data.api.service.MarsAlbumApi
import com.josycom.mayorjay.marsalbum.common.data.api.model.ManifestResponse
import javax.inject.Inject

class ManifestDataSourceImpl @Inject constructor(private val apiService: MarsAlbumApi) : ManifestDataSource {

    override suspend fun getRoverManifest(roverName: String): ManifestResponse {
        return apiService.getRoverManifest(roverName)
    }
}