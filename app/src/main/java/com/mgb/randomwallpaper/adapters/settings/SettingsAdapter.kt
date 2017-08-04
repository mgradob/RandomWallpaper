package com.mgb.randomwallpaper.adapters.settings

import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.mgb.randomwallpaper.adapters.DividerDelegateAdapter
import com.mgb.randomwallpaper.adapters.HeaderDelegateAdapter
import com.mgb.randomwallpaper.adapters.ViewType
import com.mgb.randomwallpaper.adapters.ViewTypeDelegateAdapter
import com.mgb.randomwallpaper.database.ChannelModel
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
        delegateAdapters.put(SettingsAdapterConstants.CHANNEL.value, ChannelItemDelegateAdapter())
        delegateAdapters.put(SettingsAdapterConstants.DIVIDER.value, DividerDelegateAdapter())

        items.add(HeaderItem("Settings"))
        items.add(SettingItem("Update Interval", "Half hour"))
        items.add(Divider())
        items.add(HeaderItem("Channels"))
        items.add(ChannelItem(ChannelModel("Channel 1", 1234567, 1)))
        items.add(ChannelItem(ChannelModel("Channel 1", 1234567, 1)))
        items.add(ChannelItem(ChannelModel("Channel 1", 1234567, 1)))
    }

    //region RecyclerView
    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = delegateAdapters.get(getItemViewType(position)).onBindViewHolder(holder, items[position])

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = delegateAdapters.get(viewType).onCreateViewHolder(parent)

    override fun getItemViewType(position: Int): Int = items[position].getViewType()
    //endregion
}