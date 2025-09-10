package ru.practicum.android.diploma.data

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson

class FilterParametersSharedStorageImpl(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) : SharedStorage {

    override fun <T> putData(key: Key<T>, value: T) {
        val json = gson.toJson(value, key.type)
        sharedPreferences.edit { putString(key.name, json) }
    }

    override fun <T> getData(key: Key<T>, default: T?): T? {
        val json = sharedPreferences.getString(key.name, null) ?: return default
        return runCatching { gson.fromJson<T>(json, key.type) }.getOrDefault(default)
    }

}
