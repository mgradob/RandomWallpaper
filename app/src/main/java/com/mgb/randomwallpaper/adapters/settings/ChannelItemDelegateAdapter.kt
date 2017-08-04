package com.mgb.randomwallpaper.adapters.settings

import com.mgb.randomwallpaper.utils.inflate
import kotlinx.android.synthetic.main.list_item_setting_text_one_line_checkbox.view.*

/**
 * Created by mgradob on 8/4/17.
 */
class ChannelItemDelegateAdapter : com.mgb.randomwallpaper.adapters.ViewTypeDelegateAdapter {

    override fun onCreateViewHolder(parent: android.view.ViewGroup): android.support.v7.widget.RecyclerView.ViewHolder = com.mgb.randomwallpaper.adapters.settings.ChannelItemDelegateAdapter.ChannelItemViewHolder(parent)

    override fun onBindViewHolder(holder: android.support.v7.widget.RecyclerView.ViewHolder, item: com.mgb.randomwallpaper.adapters.ViewType) = (holder as com.mgb.randomwallpaper.adapters.settings.ChannelItemDelegateAdapter.ChannelItemViewHolder).bind(item as ChannelItem)

    class ChannelItemViewHolder(parent: android.view.ViewGroup) : android.support.v7.widget.RecyclerView.ViewHolder(parent.inflate(com.mgb.randomwallpaper.R.layout.list_item_setting_text_one_line_checkbox)) {

        fun bind(item: ChannelItem) = with(item) {
            itemView.settingTitle.text = item.model.name
            itemView.settingCheck.isChecked = item.model.selected == 1
        }
    }
}