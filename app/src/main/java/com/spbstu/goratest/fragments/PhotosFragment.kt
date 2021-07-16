package com.spbstu.goratest.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.spbstu.goratest.R
import com.spbstu.goratest.adapters.PhotosRecyclerAdapter
import com.spbstu.goratest.models.PhotosModel
import com.spbstu.goratest.presenters.PhotosPresenter
import com.spbstu.goratest.views.PhotosView

class PhotosFragment : Fragment(), PhotosView {

    private lateinit var photosRecyclerView: RecyclerView

    private var presenter: PhotosPresenter? = null
    private var albumId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle = arguments
        val id = bundle?.getInt("id")
        if (id != null) {
            this.albumId = id
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_photos, container, false)
        init(view)
        return view
    }

    override fun onStart() {
        super.onStart()

        presenter?.loadPhotos()
    }

    private fun init(view: View) {
        photosRecyclerView = view.findViewById(R.id.recycler)
        photosRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        photosRecyclerView.hasFixedSize()

        val model = PhotosModel(albumId)
        presenter = PhotosPresenter(model)
        presenter?.attachView(this)
    }

    override fun updatePhotosRecycler(adapter: PhotosRecyclerAdapter) {
        photosRecyclerView.adapter = adapter
    }
}