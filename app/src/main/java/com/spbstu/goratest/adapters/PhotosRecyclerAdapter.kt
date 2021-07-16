package com.spbstu.goratest.adapters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.spbstu.goratest.R
import com.spbstu.goratest.data.Photo
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class PhotosRecyclerAdapter(photos: ArrayList<Photo>):
    RecyclerView.Adapter<PhotosRecyclerAdapter.MyViewHolder>() {

    private var mPhotos: ArrayList<Photo> = photos

    inner class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.image)
        val label: TextView = view.findViewById(R.id.label)
        val progressBar: ProgressBar = view.findViewById(R.id.loading_progressBar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosRecyclerAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.photo_cradview, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PhotosRecyclerAdapter.MyViewHolder, position: Int) {
        val element = mPhotos[position]

        holder.image.contentDescription = element.title
        holder.label.text = element.title

        val executor: ExecutorService = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())

        var bitmap: Bitmap? = null
        executor.execute {
            try {
                val url = URL(element.url)
                val urlConnection = url.openConnection()

                urlConnection.setRequestProperty("User-Agent", "Test")
                urlConnection.defaultUseCaches = true

                urlConnection.connect()

                val inputStream = BufferedInputStream(urlConnection.content as InputStream)
                bitmap = BitmapFactory.decodeStream(inputStream)

            } catch (ex: IOException) {
                ex.printStackTrace()
            }

            handler.post {
                holder.image.setImageBitmap(bitmap)
                holder.progressBar.visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int {
        return mPhotos.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}