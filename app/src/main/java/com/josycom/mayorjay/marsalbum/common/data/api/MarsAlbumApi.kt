package com.josycom.mayorjay.marsalbum.common.data.api

import com.josycom.mayorjay.marsalbum.common.data.api.model.ManifestResponse
import com.josycom.mayorjay.marsalbum.common.data.api.model.PhotoResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MarsAlbumApi {

    @GET(ApiConstants.MANIFEST_ENDPOINT)
    suspend fun getRoverManifest(
        @Path(ApiConstants.ROVER_NAME) roverName: String,
        @Query(ApiConstants.API_KEY) apiKey: String
    ): ManifestResponse

    @GET(ApiConstants.PHOTOS_ENDPOINT)
    suspend fun getRoverPhotos(
        @Path(ApiConstants.ROVER_NAME) roverName: String,
        @Query(ApiConstants.SOL) martianSol: Int,
        @Query(ApiConstants.PAGE) page: Int,
        @Query(ApiConstants.API_KEY) apiKey: String
    ): PhotoResponse
}