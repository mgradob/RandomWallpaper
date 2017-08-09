package com.mgb.randomwallpaper.receivers

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import com.mgb.randomwallpaper.services.DownloadService
import com.mgb.randomwallpaper.utils.DelegatesExt
import com.mgb.randomwallpaper.utils.SharedPreferences
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

/**
 * Created by mgradob on 6/2/17.
 */
class BootReceiver : BroadcastReceiver(), AnkoLogger {

    override fun onReceive(context: Context, intent: Intent) {
        info("Received broadcast\n ${intent.action}")

        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED -> startDownloadService(context)
        }
    }

    fun startDownloadService(context: Context) {
        val mIntervalPref: Long by DelegatesExt().preference(context, SharedPreferences.UPDATE_INTERVAL.value, AlarmManager.INTERVAL_DAY)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setInexactRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime(),
                mIntervalPref,
                PendingIntent.getBroadcast(context, DownloadService.ID_DOWNLOAD_SERVICE, Intent(context, DownloadService::class.java), 0))
                .also { info { "Started download service" } }
    }
}