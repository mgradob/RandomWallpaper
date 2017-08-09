package com.mgb.randomwallpaper.presenters

import android.app.AlarmManager
import com.mgb.randomwallpaper.R
import com.mgb.randomwallpaper.RandomWallpaperApp
import com.mgb.randomwallpaper.database.ChannelModel
import com.mgb.randomwallpaper.database.CollectionsDAO
import com.mgb.randomwallpaper.dialogs.AddChannelDialog
import com.mgb.randomwallpaper.dialogs.SelectIntervalDialog
import com.mgb.randomwallpaper.utils.Preference
import com.mgb.randomwallpaper.utils.SharedPreferences
import com.mgb.randomwallpaper.views.SettingsActivity
import org.jetbrains.anko.*

/**
 * Created by mgradob on 7/31/17.
 */
class SettingsPresenter(val mView: SettingsActivity) : BasePresenter(mView), AnkoLogger {

    private val collectionsDao: CollectionsDAO by lazy { CollectionsDAO(mView) }

    var interval: Long by Preference(context, SharedPreferences.UPDATE_INTERVAL.value, AlarmManager.INTERVAL_DAY)
    var channels: List<ChannelModel> = emptyList()

    override fun start() {
        info("Starting")

        getChannelsFromDb()
    }

    override fun stop() {
        info("Stopping")
    }

    override fun onDbReload() {
        getChannelsFromDb()
        mView.startDownloadService()
    }

    private fun getChannelsFromDb() = collectionsDao.getChannels { channels ->
        this.channels = channels
        mView.updateUi()
    }

    fun showIntervalDialog() = SelectIntervalDialog(interval, this::onIntervalChanged).show(mView.supportFragmentManager, "update_interval")

    fun onIntervalChanged(interval: Long) {
        info("Selected interval $interval")
        this.interval = interval

        onDbReload()
    }

    fun showAddChannelDialog() = AddChannelDialog(this::addChannel).show(mView.supportFragmentManager, "add_channel")

    fun addChannel(channelModel: ChannelModel) = collectionsDao.insertChannel(channelModel, this::onDbReload)

    fun selectChannel(channelModel: ChannelModel, checked: Boolean) {
        channelModel.selected = if (checked) 1 else 0

        collectionsDao.updateChannel(channelModel, this::onDbReload)
    }

    fun showDeleteChannelDialog(channel: ChannelModel) {
        mView.alert(mView.getString(R.string.delete_channel_warning_body, channel.name), mView.getString(R.string.delete_channel_warning_title)) {
            yesButton { removeChannel(channel) }
            noButton { }
        }.show()
    }

    fun removeChannel(channelModel: ChannelModel) = collectionsDao.deleteChannel(channelModel, this::onDbReload)
}