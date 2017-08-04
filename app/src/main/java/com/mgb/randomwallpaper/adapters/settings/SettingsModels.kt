package com.mgb.randomwallpaper.adapters.models

/**
 * Created by mgradob on 7/31/17.
 */
data class Settings(val settings: List<ViewType>)

data class HeaderItem(val title: String) : ViewType {

    override fun getViewType(): Int = AdapterConstants.HEADER.value
}

data class SettingItem(val title: String, val value: String) : ViewType {

    override fun getViewType(): Int = AdapterConstants.SETTING.value
}

data class ChannelItem(val title: String, val checked: Boolean) : ViewType {

    override fun getViewType(): Int = AdapterConstants.CHANNEL.value
}

class Divider : ViewType {

    override fun getViewType(): Int = AdapterConstants.DIVIDER.value
}