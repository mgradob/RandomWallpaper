package com.mgb.randomwallpaper.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mgb.randomwallpaper.R
import com.mgb.randomwallpaper.database.ChannelModel
import kotlinx.android.synthetic.main.list_item_channel.view.*

/**
 * Created by mgradob on 6/6/17.
 */
class ChannelsAdapter(val mChannelList: List<ChannelModel>, val itemClicked: (ChannelModel, Boolean) -> Unit, val longItemClicked: (ChannelModel) -> Unit) : RecyclerView.Adapter<ChannelsAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(mChannelList[position])

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
            = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_channel, parent, false), itemClicked, longItemClicked)

    override fun getItemCount(): Int = mChannelList.size

    class ViewHolder(view: View, val itemClicked: (ChannelModel, Boolean) -> Unit, val longItemClicked: (ChannelModel) -> Unit) : RecyclerView.ViewHolder(view) {

        fun bind(channel: ChannelModel) {
            with(channel) {
                itemView.mChannelCheckBox.text = name
                itemView.mChannelCheckBox.isChecked = selected == 1

                itemView.mChannelCheckBox.setOnCheckedChangeListener { button, checked ->
                    itemClicked(this, checked)
                }

                itemView.mChannelCheckBox.setOnLongClickListener {
                    longItemClicked(this)
                    true
                }
            }
        }
    }
}