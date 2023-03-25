package com.josycom.mayorjay.marsalbum.photodetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.josycom.mayorjay.marsalbum.databinding.FragmentPhotoDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

//@AndroidEntryPoint
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
}