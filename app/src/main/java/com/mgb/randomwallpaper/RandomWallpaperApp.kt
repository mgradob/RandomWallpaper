package com.mgb.randomwallpaper

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import com.mgb.randomwallpaper.services.DownloadService
import com.mgb.randomwallpaper.utils.DelegatesExt
import com.mgb.randomwallpaper.utils.SharedPreferences

/**
 * Created by mgradob on 7/31/17.
 */
class RandomWallpaperApp : Application() {

    companion object {
        val instance: RandomWallpaperApp by lazy { RandomWallpaperApp() }
    }

    private val mIntervalPref: Long by DelegatesExt().preference(this, SharedPreferences.UPDATE_INTERVAL.value, AlarmManager.INTERVAL_DAY)

    fun startDownloadService() {
        val alarmManager: AlarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.setInexactRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime(),
                mIntervalPref,
                PendingIntent.getBroadcast(this, DownloadService.ID_DOWNLOAD_SERVICE, Intent(this, DownloadService::class.java), 0))
    }
}