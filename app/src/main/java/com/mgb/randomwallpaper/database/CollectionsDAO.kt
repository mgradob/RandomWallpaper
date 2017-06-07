package com.mgb.randomwallpaper.database

import android.content.Context
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.db.*
import org.jetbrains.anko.info

/**
 * Created by mgradob on 6/6/17.
 */
class CollectionsDAO(val context: Context) : AnkoLogger {

    fun getChannels(listener: (channels: List<ChannelModel>) -> Unit) {
        context.database.use {
            val channels = select(DatabaseHelper.TABLE_CHANNELS)
                    .parseList { ChannelModel(HashMap(it)) }

            listener(channels)
        }
    }

    fun updateChannel(channel: ChannelModel, listener: () -> Unit) {
        context.database.use {
            val res = update(DatabaseHelper.TABLE_CHANNELS, "selected" to channel.selected)
                    .whereSimple("_id = ?", channel._id.toString())
                    .exec()

            info("Updated $res rows")
        }

        listener()
    }

    fun insertChannel(channel: ChannelModel, listener: () -> Unit) {
        context.database.use {
            val res = insert(DatabaseHelper.TABLE_CHANNELS,
                    "name" to channel.name,
                    "channelId" to channel.channelId,
                    "selected" to channel.selected
            )

            info("Inserted $res rows")
        }

        listener()
    }

    fun deleteChannel(channel: ChannelModel, listener: () -> Unit) {
        context.database.use {
            val res = delete(DatabaseHelper.TABLE_CHANNELS, "_id=?", arrayOf(channel._id.toString()))
            info("Deleted $res rows")
        }

        listener()
    }

    fun getSelectedChannels(listener: (channels: String) -> Unit) {
        context.database.use {
            val channelsIds = select(DatabaseHelper.TABLE_CHANNELS)
                    .column("channelId")
                    .whereArgs("selected = 1")
                    .parseList(LongParser)

            val str = StringBuilder()
             for (item in channelsIds) str.append("$item,")

            if (str.isNotEmpty()) str.delete(str.length-1, str.length)

            listener(str.toString())
        }
    }
}