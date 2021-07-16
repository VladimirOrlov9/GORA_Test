package com.spbstu.goratest.models

import android.os.Handler
import android.os.Looper
import android.os.StrictMode
import com.spbstu.goratest.data.Album
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class AlbumsModel(private val id: Int) {
    interface LoadAlbumsCallback {
        fun onComplete(data: ArrayList<Album>?)
    }

    fun loadUsers(callback: LoadAlbumsCallback) {
        val executor: ExecutorService = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())

        executor.execute {
            var urlConnection: HttpURLConnection? = null
            val list = ArrayList<Album>()

            try {
                val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
                StrictMode.setThreadPolicy(policy)

                val url = URL("https://jsonplaceholder.typicode.com/albums?userId=${this.id}")
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
                    val id = jsonObject.getInt("id")
                    val title = jsonObject.getString("title")

                    list.add(Album(id = id, title = title))
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