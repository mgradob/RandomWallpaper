package com.mgb.randomwallpaper.adapters.main

import com.mgb.randomwallpaper.adapters.ViewType
import com.mgb.randomwallpaper.services.CollectionModel

/**
 * Created by mgradob on 8/8/17.
 */
data class HeaderItem(val title: String) : ViewType {

    override fun getViewType(): Int = MainAdapterConstants.HEADER.value
}

data class CollectionItem(val model: CollectionModel) : ViewType {

    override fun getViewType(): Int = MainAdapterConstants.COLLECTION.value
}