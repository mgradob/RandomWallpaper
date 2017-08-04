package com.mgb.randomwallpaper.adapters

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.mgb.randomwallpaper.R
import com.mgb.randomwallpaper.adapters.settings.HeaderItem
import com.mgb.randomwallpaper.utils.inflate
import kotlinx.android.synthetic.main.list_item_header.view.*

/**
 * Created by mgradob on 8/4/17.
 */
class HeaderDelegateAdapter : ViewTypeDelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder = SettingsHeaderViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) = (holder as SettingsHeaderViewHolder).bind(item as HeaderItem)

    class SettingsHeaderViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.list_item_header)) {

        fun bind(item: HeaderItem) = with(item) {
            itemView.headerTitle.text = item.title
        }
    }
}