package com.mgb.randomwallpaper.services

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.app.WallpaperManager
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import com.mgb.randomwallpaper.R
import okhttp3.ResponseBody
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.error
import org.jetbrains.anko.info
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*

/**
 * Created by mgradob on 6/2/17.
 */
class DownloadService : Service(), AnkoLogger {

    companion object {
        val ID_DOWNLOAD_SERVICE = 1001
        val ID_NOTIFICATION_COMPLETE = 2001
        val ID_NOTIFICATION_IN_PROGRESS = 2002
        val ACTION_NEW_WALLPAPER = "new_wallpaper"
    }

    private val unsplashService: UnsplashService by lazy { UnsplashService(applicationContext) }

    override fun onBind(intent: Intent): IBinder? = null

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        info("Start command, received intent:\n${intent.action}")

        cancelCompleteNotification()
        cancelTryAgainNotification()

        showInProgressNotification()

        getRandomWallpaper()

        return START_STICKY_COMPATIBILITY
    }

    private fun getRandomWallpaper() {
        doAsync {
            unsplashService.getRandomWallpaper(object : Callback<Array<WallpaperModel>> {
                override fun onResponse(call: Call<Array<WallpaperModel>>, response: Response<Array<WallpaperModel>>) {
                    info("Response: ${response.body()}")

                    response.body()?.first()?.links?.download?.let { saveWallpaper(it) }
                }

                override fun onFailure(call: Call<Array<WallpaperModel>>, t: Throwable) {
                    error("Get Random Wallpaper", t)
                    showTryAgainNotification()
                }
            })
        }
    }

    private fun saveWallpaper(url: String) {
        doAsync {
            unsplashService.saveWallpaper(url, object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    info("Success? ${response.isSuccessful}\n\n${response.body()}")

                    response.body()?.let { writeWallpaperToDisk(it) }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    error("Download Wallpaper", t)
                    showTryAgainNotification()
                }
            })
        }
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
                    showTryAgainNotification()
                } finally {
                    inputStream?.close()

                    outputStream?.close()
                }
            } catch (e: IOException) {
                error("File download", e)
                showTryAgainNotification()
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
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) wallpaperManager.setBitmap(bmp, null, true, (WallpaperManager.FLAG_LOCK or WallpaperManager.FLAG_SYSTEM))
                    else wallpaperManager.setBitmap(bmp)

                    cancelInProgressNotification()

                    showCompleteNotification()

                    info("Wallpaper set")
                } catch (e: IOException) {
                    error("Set Wallpaper", e)
                    showTryAgainNotification()
                }
            }
        } catch (e: IOException) {
            error("Set Wallpaper", e)
            showTryAgainNotification()
        } finally {
            this.stopSelf()
        }
    }

    private fun showInProgressNotification() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationBuilder = NotificationCompat.Builder(applicationContext)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(applicationContext.resources.getText(R.string.notification_in_progress_title))
                .setProgress(0, 0, true)

        notificationManager.notify(ID_NOTIFICATION_IN_PROGRESS, notificationBuilder.build())
    }

    private fun cancelInProgressNotification() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(ID_NOTIFICATION_IN_PROGRESS)
    }

    private fun showCompleteNotification() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val newActionIntent = Intent(applicationContext, DownloadService::class.java)
        newActionIntent.action = ACTION_NEW_WALLPAPER

        val newAction = NotificationCompat.Action(0, applicationContext.resources.getText(R.string.notification_new),
                PendingIntent.getService(applicationContext, DownloadService.ID_DOWNLOAD_SERVICE, newActionIntent, 0))

        val notificationBuilder = NotificationCompat.Builder(applicationContext)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(applicationContext.resources.getText(R.string.notification_complete_title))
                .setContentText(applicationContext.resources.getText(R.string.notification_complete_text))
                .addAction(newAction)

        notificationManager.notify(ID_NOTIFICATION_COMPLETE, notificationBuilder.build())
    }

    private fun cancelCompleteNotification() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(ID_NOTIFICATION_COMPLETE)
    }

    private fun showTryAgainNotification() {
        cancelInProgressNotification()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val newActionIntent = Intent(applicationContext, DownloadService::class.java)
        newActionIntent.action = ACTION_NEW_WALLPAPER

        val newAction = NotificationCompat.Action(0, applicationContext.resources.getText(R.string.notification_try_again),
                PendingIntent.getService(applicationContext, DownloadService.ID_DOWNLOAD_SERVICE, newActionIntent, 0))

        val notificationBuilder = NotificationCompat.Builder(applicationContext)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(applicationContext.resources.getText(R.string.notification_try_again_title))
                .setContentText(applicationContext.resources.getText(R.string.notification_try_again_text))
                .addAction(newAction)

        notificationManager.notify(ID_NOTIFICATION_COMPLETE, notificationBuilder.build())
    }

    private fun cancelTryAgainNotification() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(ID_NOTIFICATION_COMPLETE)
    }
}