package com.mgb.randomwallpaper.clients

import com.mgb.randomwallpaper.services.CollectionModel
import com.mgb.randomwallpaper.services.CollectionPhoto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

/**
 * Created by mgradob on 8/8/17.
 */
interface CollectionsClient {

    @GET("/collections/featured")
    @Headers("Authorization: Client-ID 92658c2466664a2e3f307fdaf1a03b804d4710c28287018ad837dbc687e8b9b1")
    fun getFeaturedCollections(): Call<Array<CollectionModel>>

    @GET("/collections/curated")
    @Headers("Authorization: Client-ID 92658c2466664a2e3f307fdaf1a03b804d4710c28287018ad837dbc687e8b9b1")
    fun getCuratedCollections(): Call<Array<CollectionModel>>

    @GET("/collections")
    @Headers("Authorization: Client-ID 92658c2466664a2e3f307fdaf1a03b804d4710c28287018ad837dbc687e8b9b1")
    fun getCollections(): Call<Array<CollectionModel>>

    @GET("/collections/{id}")
    @Headers("Authorization: Client-ID 92658c2466664a2e3f307fdaf1a03b804d4710c28287018ad837dbc687e8b9b1")
    fun getCollectionInfo(@Path("id") collectionId: Int): Call<CollectionModel>

    @GET("/collections/{id}/photos")
    @Headers("Authorization: Client-ID 92658c2466664a2e3f307fdaf1a03b804d4710c28287018ad837dbc687e8b9b1")
    fun getCollectionPhotos(@Path("id") collectionId: Int): Call<Array<CollectionPhoto>>
}