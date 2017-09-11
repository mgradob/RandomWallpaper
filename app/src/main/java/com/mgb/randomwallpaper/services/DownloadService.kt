package com.mgb.randomwallpaper.services

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.app.WallpaperManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import com.mgb.randomwallpaper.R
import io.reactivex.Observable
import io.reactivex.disposables.Disposables
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
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
        val ID_NOTIFICATION = 2001
        val ACTION_NEW_WALLPAPER = "new_wallpaper"
    }

    private val unsplashService: UnsplashService by lazy { UnsplashService(applicationContext) }

    private var downloadDisposable = Disposables.empty()

    override fun onBind(intent: Intent): IBinder? = null

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        info("Start command, received intent:\n${intent.action}")

        cancelCompleteNotification()
        cancelTryAgainNotification()

        showInProgressNotification()

        downloadDisposable.dispose()
        downloadDisposable = getRandomWallpaper()
                .flatMap { wallpaperUrl -> saveWallpaper(wallpaperUrl) }
                .flatMap { response -> writeWallpaperToDisk(response) }
                .flatMap { setWallpaper() }
                .subscribeOn(Schedulers.io())
                .subscribeBy(
                        onNext = {},
                        onError = {
                            error { it }

                            showTryAgainNotification()
                        },
                        onComplete = {
                            info { "Wallpaper set" }

                            cancelInProgressNotification()
                            showCompleteNotification()

                            this.stopSelf()
                        }
                )

        return START_STICKY_COMPATIBILITY
    }

    private fun getRandomWallpaper() = Observable.create<String> { subscriber ->
        unsplashService.getRandomWallpaper(object : Callback<Array<WallpaperModel>> {
            override fun onResponse(call: Call<Array<WallpaperModel>>, response: Response<Array<WallpaperModel>>) {
                info("Response: ${response.body()}")

                if (!response.isSuccessful) subscriber.onError(Throwable("Service failed"))

                response.body()?.first()?.links?.download?.let {
                    subscriber.onNext(it)
                    subscriber.onComplete()
                }
            }

            override fun onFailure(call: Call<Array<WallpaperModel>>, t: Throwable) = subscriber.onError(t)
        })
    }

    private fun saveWallpaper(url: String) = Observable.create<ResponseBody> { subscriber ->
        unsplashService.saveWallpaper(url, object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                info("Success? ${response.isSuccessful}\n\n${response.body()}")

                if (!response.isSuccessful) subscriber.onError(Throwable("Service failed"))

                response.body()?.let {
                    subscriber.onNext(it)
                    subscriber.onComplete()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) = subscriber.onError(t)
        })
    }

    private fun writeWallpaperToDisk(body: ResponseBody) = Observable.create<Boolean> { subscriber ->
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
                }

                outputStream.flush()

                subscriber.onNext(true)
            } catch (e: IOException) {
                subscriber.onError(e)
            } finally {
                inputStream?.close()

                outputStream?.close()

                subscriber.onComplete()
            }
        } catch (e: IOException) {
            subscriber.onError(e)
        }
    }

    private fun setWallpaper() = Observable.create<Boolean> { subscriber ->
        info { "Setting wallpaper..." }

        try {
            val file = File("${getExternalFilesDir(null)}${File.separator}wallpaper.png")

            if (file.exists()) {
                val wallpaperManager = WallpaperManager.getInstance(this)
                var fileInputStream: InputStream? = null

                try {
                    fileInputStream = BufferedInputStream(FileInputStream(file))

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) wallpaperManager.setStream(fileInputStream, null, true, (WallpaperManager.FLAG_LOCK or WallpaperManager.FLAG_SYSTEM))
                    else wallpaperManager.setStream(fileInputStream)

                    subscriber.onNext(true)
                } catch (e: IOException) {
                    subscriber.onError(e)
                } finally {
                    fileInputStream?.close()

                    subscriber.onComplete()
                }
            }
        } catch (e: IOException) {
            subscriber.onError(e)
        }
    }

    private fun showInProgressNotification() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationBuilder = NotificationCompat.Builder(applicationContext)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(applicationContext.resources.getText(R.string.notification_in_progress_title))
                .setProgress(0, 0, true)
                .setOngoing(true)
                .setAutoCancel(false)

        notificationManager.notify(ID_NOTIFICATION, notificationBuilder.build())
    }

    private fun cancelInProgressNotification() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(ID_NOTIFICATION)
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
                .setOngoing(true)
                .setAutoCancel(false)

        notificationManager.notify(ID_NOTIFICATION, notificationBuilder.build())
    }

    private fun cancelCompleteNotification() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(ID_NOTIFICATION)
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
                .setOngoing(true)
                .setAutoCancel(false)

        notificationManager.notify(ID_NOTIFICATION, notificationBuilder.build())
    }

    private fun cancelTryAgainNotification() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(ID_NOTIFICATION)
    }
}