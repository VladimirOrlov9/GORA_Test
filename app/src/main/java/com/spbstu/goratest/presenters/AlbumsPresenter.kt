package com.spbstu.goratest.presenters

import com.spbstu.goratest.data.Album
import com.spbstu.goratest.models.AlbumsModel
import com.spbstu.goratest.views.AlbumsView

class AlbumsPresenter(private val model: AlbumsModel) {

    private lateinit var albumsView: AlbumsView

    private var albums: ArrayList<Album>? = null

    fun attachView(view: AlbumsView) {
        this.albumsView = view
    }

    fun loadAlbums() {
        model.loadUsers(object : AlbumsModel.LoadAlbumsCallback {

            override fun onComplete(data: ArrayList<Album>?) {
                if (data != null) {
                    albums = data
                    albumsView.updateAlbumsListView(data.map { it.title })
                }
            }
        })
    }

    fun goToAlbumsPictures(pos: Int) {
        val id = albums?.get(pos)?.id
        if (id != null) {
            albumsView.openPicturesPage(id)
        }
    }
}