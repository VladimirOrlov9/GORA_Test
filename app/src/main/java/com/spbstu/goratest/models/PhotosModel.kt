package com.spbstu.goratest.models

import android.os.Handler
import android.os.Looper
import android.os.StrictMode
import com.spbstu.goratest.data.Photo
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class PhotosModel(private val albumId: Int) {

    interface LoadPhotosCallback {
        fun onComplete(data: ArrayList<Photo>?)
    }

    fun loadPhotos(callback: LoadPhotosCallback) {
        val executor: ExecutorService = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())

        executor.execute {
            var urlConnection: HttpURLConnection? = null
            val list = ArrayList<Photo>()

            try {
                val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
                StrictMode.setThreadPolicy(policy)

                val url = URL("https://jsonplaceholder.typicode.com/photos?albumId=${this.albumId}")
                urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.connect()
                val jsonText = urlConnection.inputStream.use {
                    it.reader().use { reader ->
                        reader.readText()
                    }
                }

                val jsonArray = JSONArray(jsonText)

                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.get(i) as JSONObject

                    val title = jsonObject.getString("title")
                    val photoUrl = jsonObject.getString("url")

                    list.add(Photo(title = title, url = photoUrl))
                }

            } catch (ex: IOException) {
                urlConnection?.disconnect()
            }

            handler.post {
                callback.onComplete(list)
            }
        }

    }
}