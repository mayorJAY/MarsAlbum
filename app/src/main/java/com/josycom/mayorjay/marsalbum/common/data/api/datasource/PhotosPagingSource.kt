package com.josycom.mayorjay.marsalbum.common.data.api.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.josycom.mayorjay.marsalbum.common.data.api.service.MarsAlbumApi
import com.josycom.mayorjay.marsalbum.common.data.api.model.mappers.PhotoRemoteMapper
import com.josycom.mayorjay.marsalbum.common.domain.util.NetworkException
import com.josycom.mayorjay.marsalbum.common.domain.model.Photo
import com.josycom.mayorjay.marsalbum.common.util.isEmptyOrNull
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

class PhotosPagingSource (
    private val apiService: MarsAlbumApi,
    private val photoMapper: PhotoRemoteMapper,
    private val roverName: String,
    private val martianSol: Int,
    private val pageSize: Int,
    private val firstPage: Int
    ): PagingSource<Int, Photo>() {

    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        return try {

            val page = params.key ?: firstPage
            val photoResponse = apiService.getRoverPhotos(roverName, martianSol, page)
            val photos = photoResponse.photos?.map { photoMapper.mapToDomain(it) }.orEmpty()
            val nextKey = if (photos.isEmpty()) null else page + (params.loadSize / pageSize)
            LoadResult.Page(
                data = photos,
                prevKey = if (page == firstPage) null else page - 1,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            Timber.e(exception)
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            Timber.e(exception)
            if (exception.code() == 403)
                LoadResult.Error(NetworkException("Invalid API Key supplied. Get one at https://api.nasa.gov"))
            else
                LoadResult.Error(NetworkException( if (exception.message.isEmptyOrNull()) "Code ${exception.code()}" else exception.message()))
        }
    }
}