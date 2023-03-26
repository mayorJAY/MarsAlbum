package com.josycom.mayorjay.marsalbum.photodetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.josycom.mayorjay.marsalbum.R
import com.josycom.mayorjay.marsalbum.common.domain.model.Photo
import com.josycom.mayorjay.marsalbum.common.util.Constants
import com.josycom.mayorjay.marsalbum.common.util.displayImage
import com.josycom.mayorjay.marsalbum.common.util.getFormattedDate
import com.josycom.mayorjay.marsalbum.common.util.setSpannedText
import com.josycom.mayorjay.marsalbum.databinding.FragmentPhotoDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhotoDetailsFragment : Fragment() {

    private lateinit var binding: FragmentPhotoDetailsBinding
    private val viewModel: PhotoDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhotoDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setPhoto()
        observePhoto()
    }

    private fun setPhoto() {
        val photo = arguments?.getSerializable(Constants.PHOTO_KEY) as Photo?
        viewModel.setPhoto(photo)
    }

    private fun observePhoto() {
        viewModel.getPhoto().observe(viewLifecycleOwner) { photo ->
            val rover = photo.rover
            val camera = photo.camera
            binding.apply {
                ivPhoto.displayImage(photo.imgSrc)
                tvRover.setSpannedText(getString(R.string.rover, rover.name))
                tvRoverId.setSpannedText(getString(R.string.rover_id, rover.id))
                tvLaunchDate.setSpannedText(getString(R.string.launch_date, rover.launchDate.getFormattedDate()))
                tvLandDate.setSpannedText(getString(R.string.land_date, rover.launchDate.getFormattedDate()))
                tvStatus.setSpannedText(getString(R.string.status, rover.status.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }))

                tvPhotoId.setSpannedText(getString(R.string.photo_id, photo.id))
                tvPhotoDate.setSpannedText(getString(R.string.date_captured, photo.earthDate.getFormattedDate()))

                tvCamera.setSpannedText(getString(R.string.camera_used, camera.fullName, camera.name))
                tvCameraId.setSpannedText(getString(R.string.camera_id, camera.id))
            }
        }
    }
}