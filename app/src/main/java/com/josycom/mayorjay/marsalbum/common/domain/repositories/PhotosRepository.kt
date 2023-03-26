package com.josycom.mayorjay.marsalbum.common.domain.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.josycom.mayorjay.marsalbum.common.data.api.util.ApiConstants
import com.josycom.mayorjay.marsalbum.common.data.api.service.MarsAlbumApi
import com.josycom.mayorjay.marsalbum.common.data.api.datasource.PhotosPagingSource
import com.josycom.mayorjay.marsalbum.common.data.api.model.mappers.PhotoRemoteMapper
import com.josycom.mayorjay.marsalbum.common.domain.model.Photo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhotosRepository @Inject constructor(
    private val apiService: MarsAlbumApi,
    private val photoMapper: PhotoRemoteMapper
) {

    fun fetchRoverPhotosFlow(roverName: String, martianSol: Int): Flow<PagingData<Photo>> {
        return Pager(config = PagingConfig(
            pageSize = ApiConstants.PAGE_SIZE,
            enablePlaceholders = false
        ),
            pagingSourceFactory = {
                PhotosPagingSource(
                    apiService,
                    photoMapper,
                    roverName,
                    martianSol,
                    ApiConstants.PAGE_SIZE,
                    ApiConstants.FIRST_PAGE,
                )
            }).flow
    }
}