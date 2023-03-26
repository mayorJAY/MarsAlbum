package com.josycom.mayorjay.marsalbum.photooverview

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.josycom.mayorjay.marsalbum.common.domain.model.Manifest
import com.josycom.mayorjay.marsalbum.common.domain.model.PhotoManifest
import com.josycom.mayorjay.marsalbum.common.domain.repositories.ManifestRepository
import com.josycom.mayorjay.marsalbum.common.domain.repositories.PhotosRepository
import com.josycom.mayorjay.marsalbum.common.util.isEmptyOrNull
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class PhotoOverviewViewModelTest : TestCase() {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    @Mock
    private lateinit var manifestRepository: ManifestRepository
    @Mock
    private lateinit var photosRepository: PhotosRepository
    @InjectMocks
    private lateinit var sut: PhotoOverviewViewModel
    private val dispatcher = UnconfinedTestDispatcher()

    @Before
    fun before() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun after() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test getManifestByRoverName non_null_manifest_returned`() {
        sut.spiritManifest = PhotoManifest(123)
        val manifest = sut.getManifestByRoverName("spirit")
        assertFalse(manifest.isEmptyOrNull())
    }

    @Test
    fun `test getManifestByRoverName valid_manifest_returned`() {
        sut.spiritManifest = PhotoManifest(123, name = "Spirit")
        val manifest = sut.getManifestByRoverName("spirit")
        assertEquals("Spirit", manifest.name)
    }

    @Test
    fun `test fetchManifest manifest_already_retrieved repository_is_not_called`(): Unit = runBlocking {
        sut.spiritManifest = PhotoManifest(123, name = "Spirit")
        sut.fetchManifest("spirit")
        Mockito.verify(manifestRepository, Mockito.never()).getRoverManifest(Mockito.anyString())
    }

    @Test
    fun `test fetchManifest manifest_not_yet_retrieved repository_is_called`(): Unit = runBlocking {
        sut.fetchManifest("spirit")
        Mockito.verify(manifestRepository, Mockito.times(1)).getRoverManifest(Mockito.anyString())
    }

    @Test
    fun `test saveManifest manifest_saved_successfully`(): Unit = runBlocking {
        sut.saveManifest(Manifest(PhotoManifest(543, name = "Spirit")))
        assertEquals(543, sut.spiritManifest.maxSol)
    }

    @Test
    fun `test getSolList valid_list_of_sol_retrieved`() {
        val list = sut.getSolList(200)
        assertEquals(201, list.size)
    }
}