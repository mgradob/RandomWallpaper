package com.mgb.randomwallpaper.views

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.mgb.randomwallpaper.R
import com.mgb.randomwallpaper.presenters.MainPresenter
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.startActivity

class MainActivity : BaseActivity(), AnkoLogger {

    val mPresenter: MainPresenter by lazy { MainPresenter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mPresenter.start()
    }

    override fun onDestroy() {
        mPresenter.stop()

        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_main_settings -> {
                startActivity<SettingsActivity>()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun updateUi() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}