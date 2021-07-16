package com.spbstu.goratest.presenters

import com.spbstu.goratest.adapters.PhotosRecyclerAdapter
import com.spbstu.goratest.data.Photo
import com.spbstu.goratest.models.PhotosModel
import com.spbstu.goratest.views.PhotosView

class PhotosPresenter(private val model: PhotosModel) {

    private lateinit var photosView: PhotosView

    private var photos: ArrayList<Photo>? = null
    private lateinit var adapter: PhotosRecyclerAdapter

    fun attachView(view: PhotosView) {
        this.photosView = view
    }

    fun loadPhotos() {
        model.loadPhotos(object : PhotosModel.LoadPhotosCallback {

            override fun onComplete(data: ArrayList<Photo>?) {
                if (data != null) {
                    photos = data

                    adapter = PhotosRecyclerAdapter(photos = photos!!)
                    photosView.updatePhotosRecycler(adapter = adapter)
                }
            }
        })
    }
}