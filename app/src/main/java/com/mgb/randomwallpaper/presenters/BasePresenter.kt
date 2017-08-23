package com.mgb.randomwallpaper.presenters

import android.content.Context

/**
 * Created by mgradob on 7/31/17.
 */
abstract class BasePresenter(val context: Context) {

    abstract fun start()

    abstract fun stop()

    abstract fun reloadInfo()
}