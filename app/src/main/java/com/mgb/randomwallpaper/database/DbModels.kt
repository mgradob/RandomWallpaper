package com.mgb.randomwallpaper.database

/**
 * Created by mgradob on 6/5/17.
 */
data class ChannelModel(var map: MutableMap<String, Any?>) {

    var _id: Long by map
    var name: String by map
    var channelId: Long by map
    var selected: Int by map

    constructor(name: String, channelId: Long, selected: Int) : this(HashMap()) {
        this.name = name
        this.channelId = channelId
        this.selected = selected
    }
}