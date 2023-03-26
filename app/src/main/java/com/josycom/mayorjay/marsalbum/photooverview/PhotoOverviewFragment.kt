package com.josycom.mayorjay.marsalbum.photooverview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.josycom.mayorjay.marsalbum.R
import com.josycom.mayorjay.marsalbum.common.domain.model.Manifest
import com.josycom.mayorjay.marsalbum.common.domain.model.Photo
import com.josycom.mayorjay.marsalbum.common.util.Resource
import com.josycom.mayorjay.marsalbum.common.presentation.Rover
import com.josycom.mayorjay.marsalbum.common.util.Constants
import com.josycom.mayorjay.marsalbum.common.util.isEmptyOrNull
import com.josycom.mayorjay.marsalbum.common.util.switchFragment
import com.josycom.mayorjay.marsalbum.databinding.FragmentPhotoOverviewBinding
import com.josycom.mayorjay.marsalbum.databinding.SolListViewBinding
import com.josycom.mayorjay.marsalbum.photodetails.PhotoDetailsFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PhotoOverviewFragment : Fragment(), MenuProvider {

    private lateinit var binding: FragmentPhotoOverviewBinding
    private val viewModel: PhotoOverviewViewModel by viewModels()
    private val photoAdapter: PhotoPagingAdapter by lazy { PhotoPagingAdapter { onPhotoSelected(it) } }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhotoOverviewBinding.inflate(layoutInflater)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        setupRv()
        fetchManifest()
        observeManifestLiveData()
        setupListener()
    }

    private fun init() = viewModel.init()

    private fun setupRv() {
        binding.rvPhotos.apply {
            layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = DefaultItemAnimator()
            adapter = photoAdapter.withLoadStateFooter(PagingLoadStateAdapter { photoAdapter.retry() })
        }
    }

    private fun setupListener() {
        binding.ivStatus.setOnClickListener {
            photoAdapter.retry()
        }
    }

    private fun fetchManifest() {
        val roverName = viewModel.roverSelected ?: Rover.CURIOSITY.name.lowercase()
        viewModel.fetchManifest(roverName)
    }

    private fun observeManifestLiveData() {
        viewModel.getManifestLiveData().observe(viewLifecycleOwner) { data ->

            when (data) {
                is Resource.Loading -> {
                    binding.tvStatus.isVisible = false
                    binding.ivStatus.isVisible = data.data.isEmptyOrNull()
                    binding.rvPhotos.isVisible = false
                    binding.ivStatus.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.loading_animation, null))
                }

                is Resource.Success -> {
                    binding.tvStatus.isVisible = false
                    binding.ivStatus.isVisible = false
                    val manifest = data.data ?: Manifest()
                    viewModel.saveManifest(manifest)
                    fetchAndObservePhotos(manifest.photoManifest.name, viewModel.solSelected?.toInt() ?: manifest.photoManifest.maxSol)
                }

                is Resource.Error -> {
                    binding.tvStatus.isVisible = data.data.isEmptyOrNull()
                    binding.tvStatus.text = getString(R.string.network_error_message, data.error?.message)
                    binding.ivStatus.isVisible = data.data.isEmptyOrNull()
                    binding.ivStatus.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_connection_error, null))
                }
            }
        }
    }

    private fun fetchAndObservePhotos(roverName: String, sol: Int) {
        viewModel.fetchPhotos(roverName, sol)
        observePhotoLoadState()
        observePhotosPagingFlow()
    }

    private fun observePhotoLoadState() {
        viewLifecycleOwner.lifecycleScope.launch {
            photoAdapter.loadStateFlow.collect {
                val loadState = it.source.refresh
                if (loadState is LoadState.Loading) {
                    binding.tvStatus.isVisible = false
                    binding.rvPhotos.isVisible = false
                    binding.ivStatus.isVisible = true
                    binding.ivStatus.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.loading_animation,
                            null
                        )
                    )
                }

                if (loadState is LoadState.Error) {
                    binding.tvStatus.isVisible = true
                    binding.tvStatus.text = getString(R.string.network_error_message, loadState.error.message)
                    binding.ivStatus.isVisible = true
                    binding.ivStatus.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.ic_connection_error,
                            null
                        )
                    )
                }

                if (loadState !is LoadState.Loading && loadState !is LoadState.Error) {
                    binding.ivStatus.isVisible = false
                    binding.tvStatus.isVisible = false
                    binding.rvPhotos.isVisible = true
                    if (photoAdapter.itemCount <= 0) {
                        binding.tvStatus.isVisible = true
                        binding.tvStatus.text = getString(R.string.no_photo_message)
                    }
                }
            }
        }
    }

    private fun observePhotosPagingFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getPhotosPagingFlow().collectLatest { data ->
                    photoAdapter.submitData(data)
                }
            }
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_main, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.action_filter_perseverance -> {
                filterByRover(Rover.PERSEVERANCE.name.lowercase())
                return true
            }
            R.id.action_filter_curiosity -> {
                filterByRover(Rover.CURIOSITY.name.lowercase())
                return true
            }
            R.id.action_filter_opportunity -> {
                filterByRover(Rover.OPPORTUNITY.name.lowercase())
                return true
            }
            R.id.action_filter_spirit -> {
                filterByRover(Rover.SPIRIT.name.lowercase())
                return true
            }
        }
        return false
    }

    private fun filterByRover(roverName: String) {
        val manifest = viewModel.getManifestByRoverName(roverName)
        if (manifest.name.isEmptyOrNull()) {
            viewModel.fetchManifest(roverName)
        } else {
            popUpSolDialog(roverName, manifest.maxSol)
        }
    }

    private fun popUpSolDialog(roverName: String, maxSol: Int) {
        val binding = SolListViewBinding.inflate(layoutInflater)
        AlertDialog.Builder(requireContext()).create().apply {
            setView(binding.root)
            setCancelable(false)

            binding.spSol.adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                viewModel.getSolList(maxSol)
            )
                .also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
            binding.spSol.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    viewModel.solSelected = p0?.getItemAtPosition(p2) as String?
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }

            binding.btProceed.setOnClickListener {
                fetchAndObservePhotos(roverName, viewModel.solSelected?.toInt() ?: 0)
                dismiss()
            }
            show()
        }
    }

    private fun onPhotoSelected(photo: Photo) {
        val arg = Bundle().apply { putSerializable(Constants.PHOTO_KEY, photo) }
        switchFragment(PhotoDetailsFragment(), arg, true)
    }
}
