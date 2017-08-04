package com.mgb.randomwallpaper.adapters

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

/**
 * Created by mgradob on 8/4/17.
 */
interface ViewTypeDelegateAdapter {

    fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder

    fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType)
}