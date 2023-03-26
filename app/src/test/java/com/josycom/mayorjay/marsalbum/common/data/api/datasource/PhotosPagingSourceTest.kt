package com.josycom.mayorjay.marsalbum.common.data.api.datasource

import androidx.paging.PagingSource
import com.josycom.mayorjay.marsalbum.common.data.api.model.ManifestResponse
import com.josycom.mayorjay.marsalbum.common.data.api.model.PhotoRemote
import com.josycom.mayorjay.marsalbum.common.data.api.model.PhotoResponse
import com.josycom.mayorjay.marsalbum.common.data.api.model.mappers.CameraRemoteMapper
import com.josycom.mayorjay.marsalbum.common.data.api.model.mappers.PhotoRemoteMapper
import com.josycom.mayorjay.marsalbum.common.data.api.model.mappers.RoverRemoteMapper
import com.josycom.mayorjay.marsalbum.common.data.api.service.MarsAlbumApi
import com.josycom.mayorjay.marsalbum.common.data.api.util.ApiConstants
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import java.io.IOException

class PhotosPagingSourceTest : TestCase() {

    private lateinit var photoMapper: PhotoRemoteMapper
    private lateinit var cameraMapper: CameraRemoteMapper
    private lateinit var roverMapper: RoverRemoteMapper

    override fun setUp() {
        cameraMapper = CameraRemoteMapper()
        roverMapper = RoverRemoteMapper()
        photoMapper = PhotoRemoteMapper(cameraMapper, roverMapper)
    }

    fun `test PhotosPagingSource#load success_status_returned_with_null_photos actual_result_has_empty_list`() = runBlocking {
        val service = getApiService(null)
        val pagingSource = PhotosPagingSource(service, photoMapper, "perseverance", 10, ApiConstants.PAGE_SIZE, ApiConstants.FIRST_PAGE)
        val expected = PagingSource.LoadResult.Page(data = emptyList(), prevKey = null, nextKey = null)
        val actual = pagingSource.load(PagingSource.LoadParams.Refresh(null, 10, false))
        assertEquals(expected, actual)
    }

    fun `test PhotosPagingSource#load success_status_returned_with_empty_photos actual_result_has_empty_list`() = runBlocking {
        val service = getApiService(emptyList())
        val pagingSource = PhotosPagingSource(service, photoMapper, "perseverance", 10, ApiConstants.PAGE_SIZE, ApiConstants.FIRST_PAGE)
        val expected = PagingSource.LoadResult.Page(data = emptyList(), prevKey = null, nextKey = null)
        val actual = pagingSource.load(PagingSource.LoadParams.Refresh(null, 10, false))
        assertEquals(expected, actual)
    }

    fun `test PhotosPagingSource#load success_status_returned_with_valid_photos actual_result_has_valid_list`() = runBlocking {
        val photos = listOf(PhotoRemote())
        val service = getApiService(photos)
        val pagingSource = PhotosPagingSource(service, photoMapper, "perseverance", 10, ApiConstants.PAGE_SIZE, ApiConstants.FIRST_PAGE)
        val expected = PagingSource.LoadResult.Page(photos.map { photoMapper.mapToDomain(it) }, prevKey = null, nextKey = 1)
        val actual = pagingSource.load(PagingSource.LoadParams.Refresh(null, 10, false))
        assertEquals(expected, actual)
    }

    fun `test PhotosPagingSource#load success_status_returned_with_valid_photos actual_result_has_valid_item`() = runBlocking {
        val photos = listOf(PhotoRemote(id = 1220))
        val service = getApiService(photos)
        val pagingSource = PhotosPagingSource(service, photoMapper, "perseverance", 10, ApiConstants.PAGE_SIZE, ApiConstants.FIRST_PAGE)
        val result = pagingSource.load(PagingSource.LoadParams.Refresh(null, 10, false))
        assertEquals(1220, (result as PagingSource.LoadResult.Page).data[0].id)
    }

    fun `test PhotosPagingSource#load failure_status_returned error_load_result_propagated`() = runBlocking {
        val service = getApiService(null, false)
        val pagingSource = PhotosPagingSource(service, photoMapper, "perseverance", 10, ApiConstants.PAGE_SIZE, ApiConstants.FIRST_PAGE)
        val result = pagingSource.load(PagingSource.LoadParams.Refresh(null, 10, false))
        assertTrue(result is PagingSource.LoadResult.Error)
    }

    fun `test PhotosPagingSource#load failure_status_returned error_message_propagated`() = runBlocking {
        val service = getApiService(null, false)
        val pagingSource = PhotosPagingSource(service, photoMapper, "perseverance", 10, ApiConstants.PAGE_SIZE, ApiConstants.FIRST_PAGE)
        val result = pagingSource.load(PagingSource.LoadParams.Refresh(null, 10, false))
        assertEquals("Error reaching Remote server", (result as PagingSource.LoadResult.Error).throwable.message)
    }

    private fun getApiService(photos: List<PhotoRemote>?, isSuccess: Boolean = true): MarsAlbumApi {
        return object: MarsAlbumApi {
            override suspend fun getRoverManifest(
                roverName: String,
                apiKey: String
            ): ManifestResponse {
                return ManifestResponse(null)
            }

            override suspend fun getRoverPhotos(
                roverName: String,
                martianSol: Int,
                page: Int,
                apiKey: String
            ): PhotoResponse {
                if (!isSuccess) throw IOException("Error reaching Remote server")
                return PhotoResponse(photos)
            }

        }
    }
}