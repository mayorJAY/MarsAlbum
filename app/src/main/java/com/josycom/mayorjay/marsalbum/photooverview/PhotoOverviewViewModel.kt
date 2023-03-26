package com.josycom.mayorjay.marsalbum.photooverview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.josycom.mayorjay.marsalbum.common.domain.model.Manifest
import com.josycom.mayorjay.marsalbum.common.domain.model.Photo
import com.josycom.mayorjay.marsalbum.common.domain.model.PhotoManifest
import com.josycom.mayorjay.marsalbum.common.domain.repositories.ManifestRepository
import com.josycom.mayorjay.marsalbum.common.domain.repositories.PhotosRepository
import com.josycom.mayorjay.marsalbum.common.util.Resource
import com.josycom.mayorjay.marsalbum.common.presentation.Rover
import com.josycom.mayorjay.marsalbum.common.util.isEmptyOrNull
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PhotoOverviewViewModel @Inject constructor(
    private val manifestRepository: ManifestRepository,
    private val photosRepository: PhotosRepository
    ): ViewModel() {

    private var manifestLiveData: MutableLiveData<Resource<Manifest>> = MutableLiveData()
    fun getManifestLiveData(): MutableLiveData<Resource<Manifest>> = manifestLiveData
    private fun setManifestLiveData(manifest: Resource<Manifest>) { this.manifestLiveData.value = manifest }

    private var _curiosityManifest: PhotoManifest = PhotoManifest()
    private var curiosityManifest: PhotoManifest
        get() = _curiosityManifest
        set(value) { _curiosityManifest = value }

    private var _perseveranceManifest: PhotoManifest = PhotoManifest()
    private var perseveranceManifest: PhotoManifest
        get() = _perseveranceManifest
        set(value) { _perseveranceManifest = value }

    private var _opportunityManifest: PhotoManifest = PhotoManifest()
    private var opportunityManifest: PhotoManifest
        get() = _opportunityManifest
        set(value) { _opportunityManifest = value }

    private var _spiritManifest: PhotoManifest = PhotoManifest()
    private var spiritManifest: PhotoManifest
        get() = _spiritManifest
        set(value) { _spiritManifest = value }

    private var _roverSelected: String? = null
    var roverSelected: String?
        get() = _roverSelected
        set(value) { _roverSelected = value }

    private var _solSelected: String? = null
    var solSelected: String?
        get() = _solSelected
        set(value) { _solSelected = value }

    private var photosPagingFlow: Flow<PagingData<Photo>> = flowOf()
    fun getPhotosPagingFlow(): Flow<PagingData<Photo>> = photosPagingFlow
    private fun setPhotosPagingFlow(photosPagingFlow: Flow<PagingData<Photo>>) { this.photosPagingFlow = photosPagingFlow }

    init {
        initialLoad()
    }

    private fun initialLoad() {
        viewModelScope.launch {
            try {
                launch {
                    val curiosityManifestResource = manifestRepository.getRoverManifest(Rover.CURIOSITY.name.lowercase())
                    if (curiosityManifestResource is Resource.Success) {
                        saveManifest(curiosityManifestResource.data ?: Manifest())
                    }
                }

                launch {
                    val perseveranceManifestResource = manifestRepository.getRoverManifest(Rover.PERSEVERANCE.name.lowercase())
                    if (perseveranceManifestResource is Resource.Success) {
                        saveManifest(perseveranceManifestResource.data ?: Manifest())
                    }
                }

                launch {
                    val opportunityManifestResource = manifestRepository.getRoverManifest(Rover.OPPORTUNITY.name.lowercase())
                    if (opportunityManifestResource is Resource.Success) {
                        saveManifest(opportunityManifestResource.data ?: Manifest())
                    }
                }

                launch {
                    val spiritManifestResource = manifestRepository.getRoverManifest(Rover.SPIRIT.name.lowercase())
                    if (spiritManifestResource is Resource.Success) {
                        saveManifest(spiritManifestResource.data ?: Manifest())
                    }
                }
            } catch (ex: Exception) {
                Timber.e(ex)
            }
        }
    }

    fun fetchManifest(roverName: String) {
        setManifestLiveData(Resource.Loading())

        viewModelScope.launch {
            val manifest = getManifestByRoverName(roverName)

            val manifestResource = if (manifest.name.isEmptyOrNull())
                manifestRepository.getRoverManifest(roverName)
            else Resource.Success(Manifest(manifest))
            setManifestLiveData(manifestResource)
        }
    }

    fun getManifestByRoverName(roverName: String): PhotoManifest {
        roverSelected = roverName
        return when (roverName.uppercase()) {
            Rover.PERSEVERANCE.name -> { perseveranceManifest }
            Rover.CURIOSITY.name -> { curiosityManifest }
            Rover.OPPORTUNITY.name -> { opportunityManifest }
            else -> { spiritManifest }
        }
    }

    fun saveManifest(manifest: Manifest) {
        val photoManifest = manifest.photoManifest
        when (photoManifest.name.uppercase()) {
            Rover.PERSEVERANCE.name -> { perseveranceManifest = photoManifest }
            Rover.CURIOSITY.name -> { curiosityManifest = photoManifest }
            Rover.OPPORTUNITY.name -> { opportunityManifest = photoManifest }
            Rover.SPIRIT.name -> { spiritManifest = photoManifest }
        }
    }

    fun fetchPhotos(roverName: String, sol: Int) {
        val photosPagingFlow = photosRepository.fetchRoverPhotosFlow(roverName, sol).cachedIn(viewModelScope)
        setPhotosPagingFlow(photosPagingFlow)
    }

    fun getSolList(maxSol: Int): List<String> {
        val list = mutableListOf<String>()
        for (sol in 0..maxSol) {
            list.add(sol.toString())
        }
        return list
    }
}