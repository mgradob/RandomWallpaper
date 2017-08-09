package com.mgb.randomwallpaper.views

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.support.v7.app.AppCompatActivity
import com.mgb.randomwallpaper.services.DownloadService
import com.mgb.randomwallpaper.utils.DelegatesExt
import com.mgb.randomwallpaper.utils.SharedPreferences
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

/**
 * Created by mgradob on 7/31/17.
 */
abstract class BaseActivity : AppCompatActivity(), AnkoLogger {

    abstract fun updateUi()

    fun startDownloadService() {
        val mIntervalPref: Long by DelegatesExt().preference(this, SharedPreferences.UPDATE_INTERVAL.value, AlarmManager.INTERVAL_DAY)

        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setInexactRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime(),
                mIntervalPref,
                PendingIntent.getService(this, DownloadService.ID_DOWNLOAD_SERVICE, Intent(this, DownloadService::class.java), 0))
                .also { info { "Started download service" } }
    }
}