package com.mgb.randomwallpaper.adapters.collectionDetail

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.mgb.randomwallpaper.R
import com.mgb.randomwallpaper.adapters.ViewType
import com.mgb.randomwallpaper.adapters.ViewTypeDelegateAdapter
import com.mgb.randomwallpaper.utils.inflate
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item_image.view.*

/**
 * Created by mgradob on 9/11/17.
 */
class CollectionPhotoDelegateAdapter : ViewTypeDelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder = CollectionPhotoViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) = (holder as CollectionPhotoViewHolder).bind(item as CollectionPhotoItem)

    class CollectionPhotoViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.list_item_image)) {

        fun bind(item: CollectionPhotoItem) = with(item) {
            Picasso.with(itemView.context).load(item.model.urls.small).into(itemView.photo)
        }
    }
}