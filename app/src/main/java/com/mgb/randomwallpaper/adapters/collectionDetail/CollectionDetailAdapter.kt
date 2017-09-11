package com.mgb.randomwallpaper.adapters.collectionDetail

import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.mgb.randomwallpaper.adapters.ViewType
import com.mgb.randomwallpaper.adapters.ViewTypeDelegateAdapter
import com.mgb.randomwallpaper.presenters.CollectionDetailPresenter

/**
 * Created by mgradob on 9/11/17.
 */
class CollectionDetailAdapter(val mPresenter: CollectionDetailPresenter) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: ArrayList<ViewType> = ArrayList()
    private var delegateAdapters = SparseArrayCompat<ViewTypeDelegateAdapter>()

    init {
        delegateAdapters.put(CollectionDetailConstants.COLLECTION.value, CollectionPhotoDelegateAdapter())

        mPresenter.mCollectionPhotos.map { collectionPhoto -> items.add(CollectionPhotoItem(collectionPhoto)) }
    }

    //region RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = delegateAdapters.get(viewType).onCreateViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = delegateAdapters.get(getItemViewType(position)).onBindViewHolder(holder, items[position])

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int = items[position].getViewType()
    //endregion
}