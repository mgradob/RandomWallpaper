package com.mgb.randomwallpaper

import android.app.Application
import org.jetbrains.anko.AnkoLogger

/**
 * Created by mgradob on 7/31/17.
 */
class RandomWallpaperApp : Application(), AnkoLogger {

    companion object {
        val instance: RandomWallpaperApp by lazy { RandomWallpaperApp() }
    }
}