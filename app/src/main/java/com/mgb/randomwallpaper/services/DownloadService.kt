package com.mgb.randomwallpaper.services

import android.app.Service
import android.app.WallpaperManager
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.IBinder
import com.mgb.randomwallpaper.database.CollectionsDAO
import com.mgb.randomwallpaper.utils.DelegatesExt
import com.mgb.randomwallpaper.utils.SharedPreferences
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.error
import org.jetbrains.anko.info
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.io.*

/**
 * Created by mgradob on 6/2/17.
 */
class DownloadService : Service(), AnkoLogger {

    companion object {
        val ID_DOWNLOAD_SERVICE = 1001
    }

    private val collectionsDao: CollectionsDAO by lazy { CollectionsDAO(applicationContext) }

    override fun onBind(intent: Intent): IBinder? = null

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        info("Start command, received intent:\n${intent.action}")

        getRandomWallpaper()

        return START_STICKY_COMPATIBILITY
    }

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

    private fun getRandomWallpaper() {
        doAsync {
            val wallpaperClient: WallpaperClient = getRetrofit().create(WallpaperClient::class.java)

            collectionsDao.getSelectedChannels { channels ->
                val call: Call<Array<WallpaperModel>> = wallpaperClient.getNewRandomPhoto(channels)
                call.enqueue(object : Callback<Array<WallpaperModel>> {
                    override fun onResponse(call: Call<Array<WallpaperModel>>, response: Response<Array<WallpaperModel>>) {
                        info("Response: ${response.body()}")

                        response.body()?.first()?.links?.download?.let { saveWallpaper(it) }
                    }

                    override fun onFailure(call: Call<Array<WallpaperModel>>, t: Throwable) {
                        error("Get Random Wallpaper", t)
                    }
                })
            }
        }
    }

    private fun saveWallpaper(url: String) {
        val wallpaperClient: WallpaperClient = getRetrofit().create(WallpaperClient::class.java)

        val call: Call<ResponseBody> = wallpaperClient.downloadPhoto(url)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                info("Success? ${response.isSuccessful}\n\n${response.body()}")

                response.body()?.let { writeWallpaperToDisk(it) }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                error("Download Wallpaper", t)
            }
        })
    }

    private fun writeWallpaperToDisk(body: ResponseBody) {
        doAsync {
            try {
                val file = File("${getExternalFilesDir(null)}${File.separator}wallpaper.png")

                var inputStream: InputStream? = null
                var outputStream: OutputStream? = null

                try {
                    val fileReader: ByteArray = ByteArray(4096)

                    val fileSize = body.contentLength()
                    var fileSizeDownloaded = 0

                    inputStream = body.byteStream()
                    outputStream = FileOutputStream(file)

                    while (true) {
                        val read = inputStream?.read(fileReader)!!

                        if (read == -1) break

                        outputStream.write(fileReader, 0, read)

                        fileSizeDownloaded += read

                        info("File download: $fileSizeDownloaded of $fileSize")
                    }

                    outputStream.flush()

                    setWallpaper()
                } catch (e: IOException) {
                    error("File download", e)
                } finally {
                    inputStream?.close()

                    outputStream?.close()
                }
            } catch (e: IOException) {
                error("File download", e)
            }
        }
    }

    private fun setWallpaper() {
        try {
            val file = File("${getExternalFilesDir(null)}${File.separator}wallpaper.png")
            val path = file.absolutePath

            if (file.exists()) {
                val bmp = BitmapFactory.decodeFile(path)
                val wallpaperManager = WallpaperManager.getInstance(this)

                try {
                    wallpaperManager.setBitmap(bmp)
                    info("Wallpaper set")
                } catch (e: IOException) {
                    error("Set Wallpaper", e)
                }
            }
        } catch (e: IOException) {
            error("Set Wallpaper", e)
        } finally {
            this.stopSelf()
        }
    }

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
}