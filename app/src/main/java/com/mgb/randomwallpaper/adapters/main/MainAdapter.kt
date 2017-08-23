package com.mgb.randomwallpaper.adapters.main

import android.graphics.Rect
import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.mgb.randomwallpaper.R
import com.mgb.randomwallpaper.adapters.*
import com.mgb.randomwallpaper.presenters.MainPresenter

/**
 * Created by mgradob on 8/8/17.
 */
class MainAdapter(val mPresenter: MainPresenter) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: ArrayList<ViewType> = ArrayList()
    private var delegateAdapters = SparseArrayCompat<ViewTypeDelegateAdapter>()

    init {
        delegateAdapters.put(CommonConstants.HEADER.value, HeaderDelegateAdapter())
        delegateAdapters.put(MainAdapterConstants.COLLECTION.value, CollectionDelegateAdapter(mPresenter::onCollectionClicked))

        setupCollections()
    }

    private fun setupCollections() {
        items.add(HeaderItem(mPresenter.mView.getString(R.string.title_featured_collections)))
        mPresenter.featuredCollections.map { collection -> items.add(CollectionItem(collection)) }

        items.add(HeaderItem(mPresenter.mView.getString(R.string.title_curated_collections)))
        mPresenter.curatedCollections.map { collection -> items.add(CollectionItem(collection)) }

        items.add(HeaderItem(mPresenter.mView.getString(R.string.title_collections)))
        mPresenter.collections.map { collection -> items.add(CollectionItem(collection)) }
    }

    //region RecyclerView
    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = delegateAdapters.get(getItemViewType(position)).onBindViewHolder(holder, items[position])

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = delegateAdapters.get(viewType).onCreateViewHolder(parent)

    override fun getItemViewType(position: Int): Int = items[position].getViewType()
    //endregion

    //region SpanSize
    class MainSpanSizeLookup(val adapter: MainAdapter) : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            return when (adapter.getItemViewType(position)) {
                CommonConstants.HEADER.value -> 2
                MainAdapterConstants.COLLECTION.value -> 1
                else -> 1
            }
        }
    }
    //endregion

    //region ItemDecorator
    class MainItemDecorator(val space: Int) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            val position = parent.getChildLayoutPosition(view)

            when ((parent.adapter as MainAdapter).getItemViewType(position)) {
                CommonConstants.HEADER.value -> if (position == 0) outRect.top = space
                MainAdapterConstants.COLLECTION.value -> {
                    outRect.top = space
                    outRect.left = space
                    outRect.right = space
                    outRect.bottom = space
                }
            }
        }
    }
    //endregion
}