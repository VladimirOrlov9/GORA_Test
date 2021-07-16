package com.spbstu.goratest.models

import android.os.Handler
import android.os.Looper
import android.os.StrictMode
import com.spbstu.goratest.data.User
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class UsersModel {

    interface LoadUsersCallback {
        fun onComplete(data: ArrayList<User>?)
    }

    fun loadUsers(callback: LoadUsersCallback) {
        val executor: ExecutorService = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())

        executor.execute {
            var urlConnection: HttpURLConnection? = null
            val list = ArrayList<User>()

            try {
                val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
                StrictMode.setThreadPolicy(policy)

                val url = URL("https://jsonplaceholder.typicode.com/users")
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
                    val name = jsonObject.getString("name")

                    list.add(User(id = id, name = name))
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