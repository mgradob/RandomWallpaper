package com.mgb.randomwallpaper.adapters

import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.mgb.randomwallpaper.adapters.models.*
import com.mgb.randomwallpaper.presenters.SettingsPresenter

/**
 * Created by mgradob on 7/31/17.
 */
class SettingsAdapter(val mPresenter: SettingsPresenter) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: ArrayList<ViewType> = ArrayList()
    private var delegateAdapters = SparseArrayCompat<ViewTypeDelegateAdapter>()

    init {
        delegateAdapters.put(AdapterConstants.HEADER.value, SettingsHeaderDelegateAdapter())
        delegateAdapters.put(AdapterConstants.SETTING.value, SettingsItemDelegateAdapter())
        delegateAdapters.put(AdapterConstants.CHANNEL.value, ChannelItemDelegateAdapter())
        delegateAdapters.put(AdapterConstants.DIVIDER.value, DividerDelegateAdapter())

        items.add(HeaderItem("Settings"))
        items.add(SettingItem("Update Interval", "Half hour"))
        items.add(Divider())
        items.add(HeaderItem("Channels"))
        items.add(ChannelItem("Channel 1", true))
        items.add(ChannelItem("Channel 2", false))
        items.add(ChannelItem("Channel 3", false))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = delegateAdapters.get(getItemViewType(position)).onBindViewHolder(holder, items[position])

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = delegateAdapters.get(viewType).onCreateViewHolder(parent)

    override fun getItemViewType(position: Int): Int = items[position].getViewType()
}