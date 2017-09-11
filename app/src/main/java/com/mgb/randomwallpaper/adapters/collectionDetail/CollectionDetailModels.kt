package com.mgb.randomwallpaper.adapters.collectionDetail

import com.mgb.randomwallpaper.adapters.ViewType
import com.mgb.randomwallpaper.services.CollectionPhoto

/**
 * Created by mgradob on 9/11/17.
 */
data class CollectionPhotoItem(val model: CollectionPhoto) : ViewType {

    override fun getViewType(): Int = CollectionDetailConstants.COLLECTION.value
}