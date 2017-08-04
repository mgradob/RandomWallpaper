package com.mgb.randomwallpaper.adapters.settings

import com.mgb.randomwallpaper.adapters.ViewType
import com.mgb.randomwallpaper.database.ChannelModel

/**
 * Created by mgradob on 7/31/17.
 */
data class Settings(val settings: List<ViewType>)

data class HeaderItem(val title: String) : ViewType {

    override fun getViewType(): Int = SettingsAdapterConstants.HEADER.value
}

data class SettingItem(val title: String, val value: String) : ViewType {

    override fun getViewType(): Int = SettingsAdapterConstants.SETTING.value
}

data class ChannelItem(val model: ChannelModel) : ViewType {

    override fun getViewType(): Int = SettingsAdapterConstants.CHANNEL.value
}

class Divider : ViewType {

    override fun getViewType(): Int = SettingsAdapterConstants.DIVIDER.value
}