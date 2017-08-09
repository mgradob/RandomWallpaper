package com.mgb.randomwallpaper.views

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.mgb.randomwallpaper.R
import com.mgb.randomwallpaper.adapters.settings.SettingsAdapter
import com.mgb.randomwallpaper.presenters.SettingsPresenter
import kotlinx.android.synthetic.main.activity_settings.*
import org.jetbrains.anko.AnkoLogger

class SettingsActivity : BaseActivity(), AnkoLogger {

    private val mPresenter: SettingsPresenter by lazy { SettingsPresenter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        mPresenter.start()

        settingsFab.setOnClickListener { mPresenter.showAddChannelDialog() }
    }

    override fun onDestroy() {
        mPresenter.stop()

        super.onDestroy()
    }

    override fun updateUi() {
        settingsRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        settingsRecyclerView.setHasFixedSize(true)
        settingsRecyclerView.adapter = SettingsAdapter(mPresenter)
    }
}