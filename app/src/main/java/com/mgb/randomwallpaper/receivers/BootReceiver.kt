package com.mgb.randomwallpaper.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.mgb.randomwallpaper.RandomWallpaperApp
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

/**
 * Created by mgradob on 6/2/17.
 */
class BootReceiver : BroadcastReceiver(), AnkoLogger {

    override fun onReceive(context: Context, intent: Intent) {
        info("Received broadcast\n ${intent.action}")

        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED -> RandomWallpaperApp.instance.startDownloadService()
        }
    }
}