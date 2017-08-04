package com.mgb.randomwallpaper.views

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.mgb.randomwallpaper.R
import com.mgb.randomwallpaper.adapters.settings.SettingsAdapter
import com.mgb.randomwallpaper.presenters.SettingsPresenter
import kotlinx.android.synthetic.main.activity_settings.*
import org.jetbrains.anko.AnkoLogger

class SettingsActivity : BaseActivity(), AnkoLogger {

    private val mPresenter: SettingsPresenter by lazy { SettingsPresenter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        mPresenter.start()
    }

    override fun onDestroy() {
        mPresenter.stop()

        super.onDestroy()
    }

    override fun updateUi() {
        mSettingsRV.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mSettingsRV.setHasFixedSize(true)
        mSettingsRV.adapter = SettingsAdapter(mPresenter)
    }
}

// LEFTOVER
//    addChannelButton.setOnClickListener {
//        val dialog = AddChannelDialog()
//        dialog.setOnConfirmListener(object : AddChannelDialog.AddChannelListener {
//            override fun onConfirmClicked(channel: ChannelModel) = mPresenter.addChannel(channel)
//        })
//        dialog.show(supportFragmentManager, "add_channel")
//    }
//
//    fun updateUi(channels: List<ChannelModel>, interval: Long) {
//        intervalRadioGroup.check(when (interval) {
//            AlarmManager.INTERVAL_FIFTEEN_MINUTES -> R.id.intervalFifteenMins
//            AlarmManager.INTERVAL_HALF_HOUR -> R.id.intervalHalfHour
//            AlarmManager.INTERVAL_HOUR -> R.id.intervalHour
//            AlarmManager.INTERVAL_HALF_DAY -> R.id.intervalHalfDay
//            AlarmManager.INTERVAL_DAY -> R.id.intervalDay
//            else -> R.id.intervalDay
//        })
//
//        settingsRecyclerView.layoutManager = LinearLayoutManager(this)
//        settingsRecyclerView.adapter = UpdateIntervalAdapter(this, mPresenter::onIntervalSelectionClicked)
//
//        channelsRecyclerView.layoutManager = LinearLayoutManager(this)
//        channelsRecyclerView.adapter = ChannelsAdapter(channels, mPresenter::selectChannel, this::showDeleteItem)
//    }
//
//    fun showDeleteItem(channel: ChannelModel) {
//        alert("Do you want to delete ${channel.name}?", "Warning") {
//            yesButton { mPresenter.removeChannel(channel) }
//            noButton { }
//        }.show()
//    }