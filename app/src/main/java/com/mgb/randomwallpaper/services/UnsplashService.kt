package com.mgb.randomwallpaper.services

import android.content.Context
import com.mgb.randomwallpaper.clients.CollectionsClient
import com.mgb.randomwallpaper.clients.WallpaperClient
import com.mgb.randomwallpaper.database.CollectionsDAO
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.jetbrains.anko.AnkoLogger
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by mgradob on 8/8/17.
 */
class UnsplashService(var context: Context) : AnkoLogger {

    private val collectionsDao: CollectionsDAO by lazy { CollectionsDAO(context) }

    private fun getRetrofit(): Retrofit {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val httpClient = OkHttpClient.Builder().addInterceptor(logging).build()

        val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl("https://api.unsplash.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build()

        return retrofit
    }

    fun getRandomWallpaper(callback: Callback<Array<WallpaperModel>>) {
        val wallpaperClient: WallpaperClient = getRetrofit().create(WallpaperClient::class.java)

        collectionsDao.getSelectedChannels { channels ->
            val call: Call<Array<WallpaperModel>> = wallpaperClient.getNewRandomPhoto(channels)
            call.enqueue(callback)
        }
    }

    fun saveWallpaper(url: String, callback: Callback<ResponseBody>) {
        val wallpaperClient: WallpaperClient = getRetrofit().create(WallpaperClient::class.java)

        val call: Call<ResponseBody> = wallpaperClient.downloadPhoto(url)
        call.enqueue(callback)
    }

    fun getCuratedCollections(callback: Callback<Array<CollectionModel>>) {
        val collectionsClient: CollectionsClient = getRetrofit().create(CollectionsClient::class.java)

        val call: Call<Array<CollectionModel>> = collectionsClient.getCuratedCollections()
        call.enqueue(callback)
    }

    fun getFeaturedCollections(callback: Callback<Array<CollectionModel>>) {
        val collectionsClient: CollectionsClient = getRetrofit().create(CollectionsClient::class.java)

        val call: Call<Array<CollectionModel>> = collectionsClient.getFeaturedCollections()
        call.enqueue(callback)
    }

    fun getCollections(callback: Callback<Array<CollectionModel>>) {
        val collectionsClient: CollectionsClient = getRetrofit().create(CollectionsClient::class.java)

        val call: Call<Array<CollectionModel>> = collectionsClient.getCollections()
        call.enqueue(callback)
    }
}