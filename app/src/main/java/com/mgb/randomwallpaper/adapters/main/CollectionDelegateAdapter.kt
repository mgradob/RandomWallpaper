package com.mgb.randomwallpaper.adapters.main

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.mgb.randomwallpaper.R
import com.mgb.randomwallpaper.adapters.ViewType
import com.mgb.randomwallpaper.adapters.ViewTypeDelegateAdapter
import com.mgb.randomwallpaper.services.CollectionModel
import com.mgb.randomwallpaper.utils.inflate
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item_grid.view.*

/**
 * Created by mgradob on 8/8/17.
 */
class CollectionDelegateAdapter(val onItemClicked: (collection: CollectionModel) -> Unit) : ViewTypeDelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder = CollectionViewHolder(parent, onItemClicked)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) = (holder as CollectionViewHolder).bind(item as CollectionItem)

    class CollectionViewHolder(parent: ViewGroup, val onItemClicked: (collection: CollectionModel) -> Unit) : RecyclerView.ViewHolder(parent.inflate(R.layout.list_item_grid)) {

        fun bind(item: CollectionItem) = with(item) {
            itemView.collectionName.text = item.model.title
            itemView.collectionArtist.text = item.model.user.name

            Picasso.with(itemView.context).load(item.model.cover_photo?.urls?.small).into(itemView.collectionThumb)

            itemView.setOnClickListener { view -> onItemClicked(item.model) }
        }
    }
}