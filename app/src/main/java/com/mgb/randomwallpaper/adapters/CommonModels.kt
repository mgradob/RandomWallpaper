package com.mgb.randomwallpaper.adapters

/**
 * Created by mgradob on 8/22/17.
 */
data class HeaderItem(val title: String) : ViewType {

    override fun getViewType(): Int = CommonConstants.HEADER.value
}