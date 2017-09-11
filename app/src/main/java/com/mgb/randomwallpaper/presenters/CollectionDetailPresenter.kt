package com.mgb.randomwallpaper.presenters

import com.mgb.randomwallpaper.services.CollectionModel
import com.mgb.randomwallpaper.services.CollectionPhoto
import com.mgb.randomwallpaper.services.UnsplashService
import com.mgb.randomwallpaper.views.CollectionDetailActivity
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.info
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by mgradob on 9/11/17.
 */
class CollectionDetailPresenter(val mView: CollectionDetailActivity) : BasePresenter(mView), AnkoLogger {

    private val unsplashService: UnsplashService by lazy { UnsplashService(mView.applicationContext) }
    var mCollectionInfo: CollectionModel? = null
    var mCollectionPhotos: Array<CollectionPhoto> = emptyArray()

    override fun start() {
        info { "Starting" }

        reloadInfo()
    }

    override fun stop() {
        info { "Stopping" }
    }

    override fun reloadInfo() {
        getCollection(mView.mCollectionId)
        getCollectionPhotos(mView.mCollectionId)
    }

    fun getCollection(collectionId: Int) {
        unsplashService.getCollectionInfo(collectionId, object : Callback<CollectionModel> {
            override fun onResponse(call: Call<CollectionModel>, response: Response<CollectionModel>) {
                info("Response: ${response.body()}")

                response.body()?.let {
                    mCollectionInfo = it

                    mView.updateUi()
                }
            }

            override fun onFailure(call: Call<CollectionModel>, t: Throwable) = error("Get collection", t)
        })
    }

    fun getCollectionPhotos(collectionId: Int) {
        unsplashService.getCollectionPhotos(collectionId, object : Callback<Array<CollectionPhoto>> {
            override fun onResponse(call: Call<Array<CollectionPhoto>>, response: Response<Array<CollectionPhoto>>) {
                info("Response: ${response.body()}")

                response.body()?.let {
                    mCollectionPhotos = it

                    mView.updateUi()
                }
            }

            override fun onFailure(call: Call<Array<CollectionPhoto>>, t: Throwable) = error("Get collection photos", t)
        })
    }
}