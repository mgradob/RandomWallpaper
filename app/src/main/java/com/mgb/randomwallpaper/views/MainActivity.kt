package com.mgb.randomwallpaper.views

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.mgb.randomwallpaper.R
import com.mgb.randomwallpaper.adapters.main.MainAdapter
import com.mgb.randomwallpaper.presenters.MainPresenter
import com.mgb.randomwallpaper.services.CollectionModel
import kotlinx.android.synthetic.main.activity_main.*
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
        val adapter = MainAdapter(mPresenter)

        val layoutManager = GridLayoutManager(this, 2)
        layoutManager.spanSizeLookup = MainAdapter.MainSpanSizeLookup(adapter)

        collectionsRecyclerView.addItemDecoration(MainAdapter.MainItemDecorator(resources.getDimensionPixelSize(R.dimen.grid_spacing)))
        collectionsRecyclerView.layoutManager = layoutManager
        collectionsRecyclerView.setHasFixedSize(true)
        collectionsRecyclerView.adapter = adapter
    }

    fun goToCollectionDetail(collection: CollectionModel) = startActivity<CollectionDetailActivity>("id" to collection.id)
}