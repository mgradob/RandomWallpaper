package com.mgb.randomwallpaper.adapters.models

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.mgb.randomwallpaper.R
import com.mgb.randomwallpaper.utils.inflate
import kotlinx.android.synthetic.main.list_item_setting_text_two_lines.view.*

/**
 * Created by mgradob on 8/4/17.
 */
class SettingsItemDelegateAdapter : ViewTypeDelegateAdapter{

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder = SettingsItemViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) = (holder as SettingsItemViewHolder).bind(item as SettingItem)

    class SettingsItemViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.list_item_setting_text_two_lines)) {

        fun bind(item: SettingItem) = with(item) {
            itemView.settingTitle.text= item.title
            itemView.settingValue.text = item.value
        }
    }
}