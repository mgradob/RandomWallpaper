package com.mgb.randomwallpaper.views

import android.app.AlarmManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.mgb.randomwallpaper.R
import com.mgb.randomwallpaper.adapters.ChannelsAdapter
import com.mgb.randomwallpaper.database.ChannelModel
import com.mgb.randomwallpaper.dialogs.AddChannelDialog
import com.mgb.randomwallpaper.presenters.MainPresenter
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton

class MainActivity : AppCompatActivity(), AnkoLogger {

    val mPresenter: MainPresenter by lazy { MainPresenter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mPresenter.start()

        addChannelButton.setOnClickListener {
            val dialog = AddChannelDialog()
            dialog.setOnConfirmListener(object : AddChannelDialog.AddChannelListener {
                override fun onConfirmClicked(channel: ChannelModel) = mPresenter.addChannel(channel)
            })
            dialog.show(supportFragmentManager, "add_channel")
        }

        intervalRadioGroup.setOnCheckedChangeListener { radioGroup, id ->
            mPresenter.updateInterval(when (id) {
                R.id.intervalFifteenMins -> AlarmManager.INTERVAL_FIFTEEN_MINUTES
                R.id.intervalHalfHour -> AlarmManager.INTERVAL_HALF_HOUR
                R.id.intervalHour -> AlarmManager.INTERVAL_HOUR
                R.id.intervalHalfDay -> AlarmManager.INTERVAL_HALF_DAY
                R.id.intervalDay -> AlarmManager.INTERVAL_DAY
                else -> AlarmManager.INTERVAL_DAY
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.stop()
    }

    fun updateUi(channels: List<ChannelModel>, interval: Long) {
        intervalRadioGroup.check(when (interval) {
            AlarmManager.INTERVAL_FIFTEEN_MINUTES -> R.id.intervalFifteenMins
            AlarmManager.INTERVAL_HALF_HOUR -> R.id.intervalHalfHour
            AlarmManager.INTERVAL_HOUR -> R.id.intervalHour
            AlarmManager.INTERVAL_HALF_DAY -> R.id.intervalHalfDay
            AlarmManager.INTERVAL_DAY -> R.id.intervalDay
            else -> R.id.intervalDay
        })

        channelsRecyclerView.layoutManager = LinearLayoutManager(this)
        channelsRecyclerView.adapter = ChannelsAdapter(channels, mPresenter::selectChannel, this::showDeleteItem)
    }

    fun showDeleteItem(channel: ChannelModel) {
        alert("Do you want to delete ${channel.name}?", "Warning") {
            yesButton { mPresenter.removeChannel(channel) }
            noButton { }
        }.show()
    }
}