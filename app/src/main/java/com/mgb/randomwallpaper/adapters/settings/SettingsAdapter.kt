package com.mgb.randomwallpaper.adapters.settings

import android.app.AlarmManager
import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.mgb.randomwallpaper.R
import com.mgb.randomwallpaper.adapters.DividerDelegateAdapter
import com.mgb.randomwallpaper.adapters.HeaderDelegateAdapter
import com.mgb.randomwallpaper.adapters.ViewType
import com.mgb.randomwallpaper.adapters.ViewTypeDelegateAdapter
import com.mgb.randomwallpaper.presenters.SettingsPresenter

/**
 * Created by mgradob on 7/31/17.
 */
class SettingsAdapter(val mPresenter: SettingsPresenter) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: ArrayList<ViewType> = ArrayList()
    private var delegateAdapters = SparseArrayCompat<ViewTypeDelegateAdapter>()

    init {
        delegateAdapters.put(SettingsAdapterConstants.HEADER.value, HeaderDelegateAdapter())
        delegateAdapters.put(SettingsAdapterConstants.SETTING.value, ItemDelegateAdapter(mPresenter::showIntervalDialog))
        delegateAdapters.put(SettingsAdapterConstants.CHANNEL.value, ChannelItemDelegateAdapter(mPresenter::selectChannel, mPresenter::showDeleteChannelDialog))
        delegateAdapters.put(SettingsAdapterConstants.DIVIDER.value, DividerDelegateAdapter())

        setupSettingsItems()
        setupChannelItems()
    }

    private fun setupSettingsItems() {
        items.add(HeaderItem(mPresenter.mView.getString(R.string.title_settings)))
        items.add(SettingItem(R.string.setting_update_interval, when (mPresenter.interval) {
            AlarmManager.INTERVAL_FIFTEEN_MINUTES -> R.string.interval_15_mins
            AlarmManager.INTERVAL_HALF_HOUR -> R.string.interval_half_hour
            AlarmManager.INTERVAL_HOUR -> R.string.interval_hour
            AlarmManager.INTERVAL_HALF_DAY -> R.string.interval_half_day
            AlarmManager.INTERVAL_DAY -> R.string.interval_day
            else -> R.string.interval_15_mins
        }))
    }

    private fun setupChannelItems() {
        if (mPresenter.channels.isEmpty()) return

        items.add(Divider())
        items.add(HeaderItem(mPresenter.mView.getString(R.string.title_channels)))
        mPresenter.channels.map { channelModel -> items.add(ChannelItem(channelModel)) }
    }

    //region RecyclerView
    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = delegateAdapters.get(getItemViewType(position)).onBindViewHolder(holder, items[position])

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = delegateAdapters.get(viewType).onCreateViewHolder(parent)

    override fun getItemViewType(position: Int): Int = items[position].getViewType()
    //endregion
}