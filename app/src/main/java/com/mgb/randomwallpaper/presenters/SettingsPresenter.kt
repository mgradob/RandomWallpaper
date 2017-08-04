package com.mgb.randomwallpaper.presenters

import android.app.AlarmManager
import com.mgb.randomwallpaper.RandomWallpaperApp
import com.mgb.randomwallpaper.database.ChannelModel
import com.mgb.randomwallpaper.database.CollectionsDAO
import com.mgb.randomwallpaper.utils.Preference
import com.mgb.randomwallpaper.utils.SharedPreferences
import com.mgb.randomwallpaper.views.SettingsActivity
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

/**
 * Created by mgradob on 7/31/17.
 */
class SettingsPresenter(val mView: SettingsActivity) : BasePresenter(mView), AnkoLogger {

    private val collectionsDao: CollectionsDAO by lazy { CollectionsDAO(mView) }

    var interval: Long by Preference(context, SharedPreferences.UPDATE_INTERVAL.value, AlarmManager.INTERVAL_DAY)
    var channels: List<ChannelModel> = emptyList()

    override fun start() {
        info("Starting presenter")
    }

    override fun stop() {
        info("Stopping presenter")
    }

    override fun onDbReload() {
        getChannelsFromDb()
        RandomWallpaperApp.instance.startDownloadService()
    }

    private fun getChannelsFromDb() = collectionsDao.getChannels { channels ->
        this.channels = channels
        mView.updateUi()
    }

    fun addChannel(channelModel: ChannelModel) = collectionsDao.insertChannel(channelModel, this::onDbReload)

    fun selectChannel(channelModel: ChannelModel, checked: Boolean) {
        channelModel.selected = if (checked) 1 else 0

        collectionsDao.updateChannel(channelModel, this::onDbReload)
    }

    fun removeChannel(channelModel: ChannelModel) = collectionsDao.deleteChannel(channelModel, this::onDbReload)
}