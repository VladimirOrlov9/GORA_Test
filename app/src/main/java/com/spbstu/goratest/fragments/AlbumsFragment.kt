package com.spbstu.goratest.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.navigation.Navigation
import com.spbstu.goratest.R
import com.spbstu.goratest.models.AlbumsModel
import com.spbstu.goratest.presenters.AlbumsPresenter
import com.spbstu.goratest.views.AlbumsView

class AlbumsFragment : Fragment(), AlbumsView {

    private lateinit var listView: ListView

    private var presenter: AlbumsPresenter? = null
    private var userId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle = arguments
        val id = bundle?.getInt("id")
        if (id != null) {
            this.userId = id
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_albums, container, false)
        init(view)
        return view
    }

    override fun onStart() {
        super.onStart()

        presenter?.loadAlbums()
    }

    private fun init(view: View) {
        listView = view.findViewById(R.id.categories_list)
        listView.setOnItemClickListener { _, _, position, _ ->
            presenter?.goToAlbumsPictures(position)
        }

        val model = AlbumsModel(userId)
        presenter = AlbumsPresenter(model)
        presenter?.attachView(this)
    }

    override fun updateAlbumsListView(list: List<String>) {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            list
        )
        listView.adapter = adapter
    }

    override fun openPicturesPage(id: Int) {
        val bundle = Bundle()
        bundle.putInt("id", id)
        Navigation.findNavController(requireView()).navigate(R.id.imagesFragment, bundle)
    }
}