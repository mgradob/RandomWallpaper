package com.mgb.randomwallpaper.adapters.settings

import com.mgb.randomwallpaper.utils.inflate
import kotlinx.android.synthetic.main.list_item_setting_text_two_lines.view.*

/**
 * Created by mgradob on 8/4/17.
 */
class ItemDelegateAdapter(val onItemClicked: () -> Unit) : com.mgb.randomwallpaper.adapters.ViewTypeDelegateAdapter {

    override fun onCreateViewHolder(parent: android.view.ViewGroup): android.support.v7.widget.RecyclerView.ViewHolder = com.mgb.randomwallpaper.adapters.settings.ItemDelegateAdapter.SettingsItemViewHolder(parent, onItemClicked)

    override fun onBindViewHolder(holder: android.support.v7.widget.RecyclerView.ViewHolder, item: com.mgb.randomwallpaper.adapters.ViewType) = (holder as com.mgb.randomwallpaper.adapters.settings.ItemDelegateAdapter.SettingsItemViewHolder).bind(item as SettingItem)

    class SettingsItemViewHolder(parent: android.view.ViewGroup, val onItemClicked: () -> Unit) : android.support.v7.widget.RecyclerView.ViewHolder(parent.inflate(com.mgb.randomwallpaper.R.layout.list_item_setting_text_two_lines)) {

        fun bind(item: SettingItem) = with(item) {
            itemView.settingTitle.text = item.title
            itemView.settingValue.text = item.value

            itemView.setOnClickListener { view -> onItemClicked() }
        }
    }
}