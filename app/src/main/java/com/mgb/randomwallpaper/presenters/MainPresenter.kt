package com.mgb.randomwallpaper.presenters

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import com.mgb.randomwallpaper.database.ChannelModel
import com.mgb.randomwallpaper.database.CollectionsDAO
import com.mgb.randomwallpaper.services.DownloadService
import com.mgb.randomwallpaper.utils.Preference
import com.mgb.randomwallpaper.utils.SharedPreferences
import com.mgb.randomwallpaper.views.MainActivity
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

/**
 * Created by mgradob on 6/6/17.
 */
class MainPresenter(val mView: MainActivity) : BasePresenter(mView), AnkoLogger {

    override fun start() {
        info("Starting presenter")
    }

    override fun stop() {
        info("Stopping presenter")
    }

    override fun onDbReload() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

