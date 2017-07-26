package com.mgb.randomwallpaper.presenters

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import com.mgb.randomwallpaper.database.ChannelModel
import com.mgb.randomwallpaper.database.CollectionsDAO
import com.mgb.randomwallpaper.services.DownloadService
import com.mgb.randomwallpaper.utils.Preference
import com.mgb.randomwallpaper.utils.SharedPreferences
import com.mgb.randomwallpaper.views.MainActivity
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

/**
 * Created by mgradob on 6/6/17.
 */
class MainPresenter(val mView: MainActivity, val context: Context = mView.applicationContext) : AnkoLogger {

    private var interval: Long by Preference(context, SharedPreferences.UPDATE_INTERVAL.value, AlarmManager.INTERVAL_DAY)

    private val collectionsDao: CollectionsDAO by lazy { CollectionsDAO(mView) }

    fun start() {
        info("Starting presenter")

        getChannelsFromDb()
        scheduleAlarm()
    }

    fun stop() {
        info("Stopping presenter")
    }

    fun updateInterval(period: Long) {
        interval = period
    }

    private fun getChannelsFromDb() = collectionsDao.getChannels { channels -> mView.updateUi(channels, interval) }

    fun addChannel(channel: ChannelModel) = collectionsDao.insertChannel(channel) {
        getChannelsFromDb()
        scheduleAlarm()
    }

    fun selectChannel(channel: ChannelModel, checked: Boolean) {
        channel.selected = if (checked) 1 else 0

        collectionsDao.updateChannel(channel) { getChannelsFromDb() }

        scheduleAlarm()
    }

    fun removeChannel(channel: ChannelModel) = collectionsDao.deleteChannel(channel) {
        getChannelsFromDb()
        scheduleAlarm()
    }

    private fun scheduleAlarm() {
        val alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val pendingIntent = PendingIntent.getService(context, DownloadService.ID_DOWNLOAD_SERVICE, Intent(context, DownloadService::class.java), 0)

        alarmManager.cancel(pendingIntent)

        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), interval, pendingIntent)

        info("Scheduled alarm")
    }
}

