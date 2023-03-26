package com.josycom.mayorjay.marsalbum.photodetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.josycom.mayorjay.marsalbum.common.domain.model.Photo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PhotoDetailsViewModel @Inject constructor() : ViewModel() {

    private var photo: MutableLiveData<Photo> = MutableLiveData()
    fun getPhoto(): MutableLiveData<Photo> = photo
    fun setPhoto(photo: Photo?) { this.photo.value = photo }
}