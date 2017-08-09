package com.mgb.randomwallpaper.clients

import com.mgb.randomwallpaper.services.WallpaperModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by mgradob on 8/8/17.
 */
interface WallpaperClient {

    @GET("photos/random")
    @Headers("Authorization: Client-ID 92658c2466664a2e3f307fdaf1a03b804d4710c28287018ad837dbc687e8b9b1")
    fun getNewRandomPhoto(@Query("collections") ids: String,
                          @Query("count") count: Int = 1): Call<Array<WallpaperModel>>

    @GET
    @Streaming
    @Headers("Authorization: Client-ID 92658c2466664a2e3f307fdaf1a03b804d4710c28287018ad837dbc687e8b9b1")
    fun downloadPhoto(@Url fileUrl: String): Call<ResponseBody>
}