package com.josycom.mayorjay.marsalbum.common.data.api.impl

import com.josycom.mayorjay.marsalbum.common.data.api.datasource.ManifestDataSource
import com.josycom.mayorjay.marsalbum.common.data.api.model.mappers.PhotoManifestRemoteMapper
import com.josycom.mayorjay.marsalbum.common.domain.model.Manifest
import com.josycom.mayorjay.marsalbum.common.domain.NetworkException
import com.josycom.mayorjay.marsalbum.common.domain.repositories.ManifestRepository
import com.josycom.mayorjay.marsalbum.common.util.Resource
import com.josycom.mayorjay.marsalbum.common.util.isEmptyOrNull
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class ManifestRepositoryImpl @Inject constructor(
    private val dataSource: ManifestDataSource,
    private val manifestMapper: PhotoManifestRemoteMapper
    ): ManifestRepository {

    override suspend fun getRoverManifest(roverName: String): Resource<Manifest> {
        return try {
            val manifestResponse = dataSource.getRoverManifest(roverName)
            val photoManifest = manifestMapper.mapToDomain(manifestResponse.photoManifest)
            if (photoManifest.maxSol != -1) {
                Resource.Success(Manifest(photoManifest))
            } else {
                Resource.Error(NetworkException("Manifest not found"))
            }
        } catch (exception: IOException) {
            Timber.e(exception)
            Resource.Error(exception)
        } catch (exception: HttpException) {
            Timber.e(exception)
            if (exception.code() == 403)
                Resource.Error(NetworkException("Invalid API Key supplied. Get one at https://api.nasa.gov"))
            else
            Resource.Error(NetworkException( if (exception.message.isEmptyOrNull()) "Code ${exception.code()}" else exception.message()))
        }
    }
}