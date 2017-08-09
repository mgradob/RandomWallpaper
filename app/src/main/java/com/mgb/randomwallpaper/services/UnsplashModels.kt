package com.mgb.randomwallpaper.services

/**
 * Created by mgradob on 6/5/17.
 */
//region Collections
data class CollectionModel(
        var id: Int,
        var title: String,
        var description: String?,
        var total_photos: Int,
        var cover_photo: CoverPhoto?,
        var user: User
)

data class CoverPhoto(var urls: Urls)

data class Urls(var raw: String, var full: String, var regular: String, var small: String, var thumb: String)

data class User(
        var id: String,
        var username: String,
        var name: String,
        var bio: String,
        var location: String,
        var total_likes: Int,
        var total_photos: Int,
        var total_collections: Int,
        var profile_image: UserProfileImage,
        var links: UserLinks
)

data class UserProfileImage(
        var small: String,
        var medium: String,
        var large: String
)

data class UserLinks(
        var self: String,
        var photos: String,
        var portfolio: String
)

data class CollectionPhoto(var id: Int, var description: String?, var urls: Urls)
//endregion

//region Wallpapers
data class WallpaperModel(var links: WallpaperLinks)

data class WallpaperLinks(var download: String)
//endregion