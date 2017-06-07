package com.mgb.randomwallpaper.customs

import android.content.Context
import android.widget.RadioButton
import com.mgb.randomwallpaper.database.ChannelModel

/**
 * Created by mgradob on 6/6/17.
 */
class RWRadioButton(context: Context) : RadioButton(context) {

    var channel: ChannelModel? = null
}