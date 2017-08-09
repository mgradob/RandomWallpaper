package com.mgb.randomwallpaper.utils

import android.content.Context
import android.content.SharedPreferences
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlin.reflect.KProperty

/**
 * Created by mgradob on 6/5/17.
 */
class DelegatesExt {

    fun preference(context: Context, name: String, default: Long) = Preference(context, name, default)
}

class Preference<T>(val context: Context, val name: String, val default: T) {

    val preferences: SharedPreferences by lazy {
        context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T = findPreference(name, default)

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) = putPreference(name, value)

    @Suppress("UNCHECKED_CAST")
    private fun <T> findPreference(name: String, default: T): T = with(preferences) {
        val res: Any = when (default) {
            is Long -> getLong(name, default)
            is String -> getString(name, default)
            is Int -> getInt(name, default)
            is Boolean -> getBoolean(name, default)
            is Float -> getFloat(name, default)
            else -> throw IllegalArgumentException("This type can't be saved into Preferences")
        }

        res as T
    }

    private fun <T> putPreference(name: String, value: T) = with(preferences.edit()) {
        when (value) {
            is Long -> putLong(name, value)
            is String -> putString(name, value)
            is Int -> putInt(name, value)
            is Boolean -> putBoolean(name, value)
            is Float -> putFloat(name, value)
            else -> throw IllegalArgumentException("This type can't be saved into Preferences")
        }.apply()
    }
}

fun ViewGroup.inflate(layoutId: Int): View = LayoutInflater.from(context).inflate(layoutId, this, false)

fun AppCompatActivity.getString(stringId: Int): String = resources.getString(stringId)

fun AppCompatActivity.getString(stringId: Int, vararg args: Any): String = resources.getString(stringId, args)

fun AppCompatActivity.getColor(colorId: Int): Int = resources.getColor(colorId)
