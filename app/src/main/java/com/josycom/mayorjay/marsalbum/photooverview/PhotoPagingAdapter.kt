package com.josycom.mayorjay.marsalbum.photooverview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.josycom.mayorjay.marsalbum.common.domain.model.Photo
import com.josycom.mayorjay.marsalbum.databinding.PhotoItemViewBinding

class PhotoPagingAdapter(private val onPhotoSelected: (photo: Photo) -> Unit) : PagingDataAdapter<Photo, PhotoPagingAdapter.PhotoViewHolder>(DiffUtilCallBack()) {

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        getItem(position)?.let { photo ->
            holder.bind(photo)
            holder.itemView.setOnClickListener { onPhotoSelected.invoke(photo) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding = PhotoItemViewBinding.inflate(LayoutInflater.from(parent.context))
        return PhotoViewHolder(binding)
    }

    class DiffUtilCallBack : DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo) = oldItem == newItem
    }

    class PhotoViewHolder(private val binding: PhotoItemViewBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(photo: Photo) {
            binding.tvImage.text = photo.imgSrc
            binding.tvDate.text = photo.earthDate
            binding.tvSol.text = photo.sol.toString()
        }
    }
}