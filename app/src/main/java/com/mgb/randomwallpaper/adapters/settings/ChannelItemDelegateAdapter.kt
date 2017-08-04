package com.mgb.randomwallpaper.adapters.models

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.mgb.randomwallpaper.R
import com.mgb.randomwallpaper.utils.inflate
import kotlinx.android.synthetic.main.list_item_setting_text_one_line_checkbox.view.*

/**
 * Created by mgradob on 8/4/17.
 */
class ChannelItemDelegateAdapter : ViewTypeDelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder = ChannelItemViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) = (holder as ChannelItemViewHolder).bind(item as ChannelItem)

    class ChannelItemViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.list_item_setting_text_one_line_checkbox)) {

        fun bind(item: ChannelItem) = with(item) {
            itemView.settingTitle.text = item.title
            itemView.settingCheck.isChecked = item.checked
        }
    }
}