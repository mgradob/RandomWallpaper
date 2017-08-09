package com.mgb.randomwallpaper.presenters

import com.mgb.randomwallpaper.services.CollectionModel
import com.mgb.randomwallpaper.services.UnsplashService
import com.mgb.randomwallpaper.views.MainActivity
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.info
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by mgradob on 6/6/17.
 */
class MainPresenter(val mView: MainActivity) : BasePresenter(mView), AnkoLogger {

    private val unsplashService: UnsplashService by lazy { UnsplashService(mView.applicationContext) }

    var featuredCollections: Array<CollectionModel> = emptyArray()
    var curatedCollections: Array<CollectionModel> = emptyArray()
    var collections: Array<CollectionModel> = emptyArray()

    override fun start() {
        info("Starting")

        unsplashService.getFeaturedCollections(object : Callback<Array<CollectionModel>> {
            override fun onResponse(call: Call<Array<CollectionModel>>, response: Response<Array<CollectionModel>>) {
                info("Response: ${response.body()}")

                response.body()?.let {
                    featuredCollections = it

                    mView.updateUi()
                }
            }

            override fun onFailure(call: Call<Array<CollectionModel>>, t: Throwable?) {
                error("Get featured collections", t)
            }
        })

        unsplashService.getCuratedCollections(object : Callback<Array<CollectionModel>> {
            override fun onResponse(call: Call<Array<CollectionModel>>, response: Response<Array<CollectionModel>>) {
                info("Response: ${response.body()}")

                response.body()?.let {
                    curatedCollections = it

                    mView.updateUi()
                }
            }

            override fun onFailure(call: Call<Array<CollectionModel>>, t: Throwable?) {
                error("Get curated collections", t)
            }
        })

        unsplashService.getCollections(object : Callback<Array<CollectionModel>> {
            override fun onResponse(call: Call<Array<CollectionModel>>, response: Response<Array<CollectionModel>>) {
                info("Response: ${response.body()}")

                response.body()?.let {
                    collections = it

                    mView.updateUi()
                }
            }

            override fun onFailure(call: Call<Array<CollectionModel>>, t: Throwable?) {
                error("Get collections", t)
            }
        })
    }

    override fun stop() {
        info("Stopping")
    }

    override fun onDbReload() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun onCollectionClicked(collection: CollectionModel) {
        info("Collection clicked, $collection")
    }
}

