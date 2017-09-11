package com.mgb.randomwallpaper.views

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import com.mgb.randomwallpaper.R
import com.mgb.randomwallpaper.adapters.collectionDetail.CollectionDetailAdapter
import com.mgb.randomwallpaper.presenters.CollectionDetailPresenter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_collection_detail.*
import org.jetbrains.anko.AnkoLogger

class CollectionDetailActivity : BaseActivity(), AnkoLogger {

    val mPresenter: CollectionDetailPresenter by lazy { CollectionDetailPresenter(this) }
    val mCollectionId: Int by lazy { intent.getIntExtra("id", 0) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collection_detail)

        mPresenter.start()
    }

    override fun onDestroy() {
        mPresenter.stop()

        super.onDestroy()
    }

    override fun updateUi() {
        collectionToolbar.title = mPresenter.mCollectionInfo?.title
        collectionToolbar.subtitle = mPresenter.mCollectionInfo?.description

        Picasso.with(this).load(mPresenter.mCollectionInfo?.cover_photo?.urls?.regular).into(collectionCoverPhoto)

        collectionPhotos.layoutManager = GridLayoutManager(this, 2)
        collectionPhotos.setHasFixedSize(true)
        collectionPhotos.adapter = CollectionDetailAdapter(mPresenter)
    }
}
