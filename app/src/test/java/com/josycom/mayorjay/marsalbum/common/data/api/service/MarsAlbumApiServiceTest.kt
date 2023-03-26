package com.josycom.mayorjay.marsalbum.common.data.api.service

import com.josycom.mayorjay.marsalbum.common.util.isEmptyOrNull
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MarsAlbumApiServiceTest : TestCase() {

    private lateinit var sut: MarsAlbumApi
    private lateinit var server: MockWebServer

    override fun setUp() {
        server = MockWebServer()
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        sut = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(MarsAlbumApi::class.java)
    }

    fun `test getRoverManifest request_sent valid_path_called`() {
        runBlocking {
            enqueueMockResponse("ManifestResponse.json")
            sut.getRoverManifest(roverName = "curiosity")
            val request = server.takeRequest()
            assertTrue((request.path ?: "").contains("manifests/curiosity"))
        }
    }

    fun `test getRoverManifest request_sent valid_response_body_received`() {
        runBlocking {
            enqueueMockResponse("ManifestResponse.json")
            val responseBody = sut.getRoverManifest(roverName = "curiosity")
            assertNotNull(responseBody)
        }
    }

    fun `test getRoverManifest request_sent valid_rover_name_received_from_response`() {
        runBlocking {
            enqueueMockResponse("ManifestResponse.json")
            val responseBody = sut.getRoverManifest(roverName = "curiosity")
            val name = responseBody.photoManifest?.name
            assertEquals("Curiosity", name)
        }
    }

    fun `test getRoverManifest request_sent valid_max_sol_received_from_response`() {
        runBlocking {
            enqueueMockResponse("ManifestResponse.json")
            val responseBody = sut.getRoverManifest(roverName = "curiosity")
            val maxSol = responseBody.photoManifest?.maxSol
            assertNotNull(maxSol)
        }
    }

    fun `test getRoverPhotos request_sent valid_path_called`() {
        runBlocking {
            enqueueMockResponse("PhotoResponse.json")
            sut.getRoverPhotos(roverName = "curiosity", martianSol = 20, page = 1)
            val request = server.takeRequest()
            assertTrue((request.path ?: "").contains("rovers/curiosity/photos"))
        }
    }

    fun `test getRoverPhotos request_sent valid_response_body_received`() {
        runBlocking {
            enqueueMockResponse("PhotoResponse.json")
            val responseBody = sut.getRoverPhotos(roverName = "curiosity", martianSol = 20, page = 1)
            assertNotNull(responseBody)
        }
    }

    fun `test getRoverPhotos request_sent valid_photo_list_received_from_response`() {
        runBlocking {
            enqueueMockResponse("PhotoResponse.json")
            val responseBody = sut.getRoverPhotos(roverName = "curiosity", martianSol = 20, page = 1)
            assertFalse(responseBody.photos.isEmptyOrNull())
        }
    }

    fun `test getRoverPhotos request_sent valid_image_src_received_from_response`() {
        runBlocking {
            enqueueMockResponse("PhotoResponse.json")
            val responseBody = sut.getRoverPhotos(roverName = "curiosity", martianSol = 20, page = 1)
            val firstItem = responseBody.photos?.get(0)
            assertFalse(firstItem?.imgSrc.isEmptyOrNull())
        }
    }

    fun `test getRoverPhotos request_sent valid_camera_object_received_from_response`() {
        runBlocking {
            enqueueMockResponse("PhotoResponse.json")
            val responseBody = sut.getRoverPhotos(roverName = "curiosity", martianSol = 20, page = 1)
            val firstItem = responseBody.photos?.get(0)
            assertFalse(firstItem?.camera.isEmptyOrNull())
        }
    }

    fun `test getRoverPhotos request_sent valid_rover_object_received_from_response`() {
        runBlocking {
            enqueueMockResponse("PhotoResponse.json")
            val responseBody = sut.getRoverPhotos(roverName = "curiosity", martianSol = 20, page = 1)
            val firstItem = responseBody.photos?.get(0)
            assertFalse(firstItem?.rover.isEmptyOrNull())
        }
    }

    fun `test getRoverPhotos request_sent_with_invalid_api_key error_response_received`() {
        runBlocking {
            enqueueMockResponse("ApiErrorResponse.json")
            val responseBody = sut.getRoverPhotos(roverName = "curiosity", martianSol = 20, page = 1)
            assertNull(responseBody.photos)
        }
    }

    private fun enqueueMockResponse(fileName: String) {
        javaClass.classLoader?.let {
            val inputStream = it.getResourceAsStream(fileName)
            val source = inputStream.source().buffer()
            val mockResponse = MockResponse()
            mockResponse.setBody(source.readString(Charsets.UTF_8))
            server.enqueue(mockResponse)
        }
    }

    override fun tearDown() {
        server.shutdown()
    }
}