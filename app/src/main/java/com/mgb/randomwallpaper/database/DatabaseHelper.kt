package com.mgb.randomwallpaper.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

/**
 * Created by mgradob on 6/5/17.
 */
class DatabaseHelper(context: Context) : ManagedSQLiteOpenHelper(context, "RandomWallpaperDb", null, 1) {

    companion object {
        private var instance: DatabaseHelper? = null

        val TABLE_CHANNELS = "Channels"

        fun getInstance(context: Context): DatabaseHelper {
            if (instance == null) instance = DatabaseHelper(context)

            return instance!!
        }
    }


    override fun onCreate(db: SQLiteDatabase?) {
        db?.createTable(TABLE_CHANNELS, true,
                "_id" to INTEGER + PRIMARY_KEY,
                "name" to TEXT,
                "channelId" to INTEGER,
                "selected" to INTEGER
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.dropTable(TABLE_CHANNELS, true)

        onCreate(db)
    }
}

val Context.database: DatabaseHelper
    get() = DatabaseHelper.getInstance(applicationContext)

fun <T: Any> SelectQueryBuilder.parseList(parser: (Map<String, Any?>) -> T): List<T> =
        parseList(object : MapRowParser<T> {
            override fun parseRow(columns: Map<String, Any?>): T = parser(columns)
        })