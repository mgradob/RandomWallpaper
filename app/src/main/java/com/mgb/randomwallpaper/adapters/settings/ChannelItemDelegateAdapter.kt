package com.mgb.randomwallpaper.adapters.settings

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.mgb.randomwallpaper.R
import com.mgb.randomwallpaper.adapters.ViewType
import com.mgb.randomwallpaper.adapters.ViewTypeDelegateAdapter
import com.mgb.randomwallpaper.database.ChannelModel
import com.mgb.randomwallpaper.utils.inflate
import kotlinx.android.synthetic.main.list_item_setting_text_one_line_checkbox.view.*

/**
 * Created by mgradob on 8/4/17.
 */
class ChannelItemDelegateAdapter(val onItemClicked: (channel: ChannelModel, checked: Boolean) -> Unit, val onItemLongClicked: (channel: ChannelModel) -> Unit) : ViewTypeDelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder = ChannelItemViewHolder(parent, onItemClicked, onItemLongClicked)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) = (holder as ChannelItemViewHolder).bind(item as ChannelItem)

    class ChannelItemViewHolder(parent: ViewGroup, val onItemClicked: (channel: ChannelModel, checked: Boolean) -> Unit, val onItemLongClicked: (channel: ChannelModel) -> Unit) : RecyclerView.ViewHolder(parent.inflate(R.layout.list_item_setting_text_one_line_checkbox)) {

        fun bind(item: ChannelItem) = with(item) {
            itemView.settingTitle.text = item.model.name
            itemView.settingCheck.isChecked = item.model.selected == 1

            itemView.settingCheck.setOnCheckedChangeListener { button, checked -> onItemClicked(item.model, checked) }

            itemView.setOnLongClickListener { view ->
                onItemLongClicked(item.model)
                true
            }
        }
    }
}