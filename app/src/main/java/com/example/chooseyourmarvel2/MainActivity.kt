package com.example.chooseyourmarvel2

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONObject
import java.math.BigInteger
import java.security.MessageDigest

class MainActivity : Activity() {
    // Initialize petImageURL with an empty string
    private lateinit var marvelList: MutableList<Triple<String, String, String>>
    private lateinit var rvMarvels: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        rvMarvels = findViewById(R.id.marvel_list)
        marvelList = mutableListOf()
        getMarvel()
    }

    private fun getMarvel() {
        val client = AsyncHttpClient()
        val ts = System.currentTimeMillis().toString()
        val privateKey = "170c53d842569a5b5bd6a133c050590fb124793b"
        val apiKey = "f80c7f1776d714932e6e5f39e6d7dd49"
        val hash = generateHash(ts, privateKey, apiKey)
        val url = "https://gateway.marvel.com/v1/public/characters?ts=$ts&apikey=$apiKey&hash=$hash"
        client[url, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {
                val jsonData = json.jsonObject.getJSONObject("data")
                val resultsArray = jsonData.getJSONArray("results")
                for (i in 0 until resultsArray.length()) {
                    val characterObj = resultsArray.getJSONObject(i)
                    val name = characterObj.getString("name")
                    val description = if (characterObj.getString("description").isNotEmpty()) {
                        characterObj.getString("description")
                    } else {
                        "No description found."
                    }
                    val thumbnailPath = characterObj.getJSONObject("thumbnail").getString("path")
                    val thumbnailExtension = characterObj.getJSONObject("thumbnail").getString("extension")
                    val imageUrl = "$thumbnailPath.$thumbnailExtension"
                    marvelList.add(Triple(imageUrl, name, description))
                }
                val adapter = MarvelAdapter(marvelList)
                rvMarvels.adapter = adapter
                rvMarvels.layoutManager = LinearLayoutManager(this@MainActivity)
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                throwable: Throwable?
            ) {
                Log.d("Marvel Error", errorResponse)
            }
        }]
    }

    private fun generateHash(ts: String, privateKey: String, apiKey: String): String {
        val input = "$ts$privateKey$apiKey"
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }
}