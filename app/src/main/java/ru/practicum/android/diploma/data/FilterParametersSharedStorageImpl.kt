package ru.practicum.android.diploma.data

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import ru.practicum.android.diploma.data.dto.FilterParametersDto


class FilterParametersSharedStorageImpl(private val sharedPreferences: SharedPreferences,
                                        private val gson: Gson) : SharedStorage {

    override fun putData(data: Any) {
        sharedPreferences.edit {
            putString(FILTERS_SHARED_KEY, gson.toJson(data))
        }
    }

    override fun getData(defaultData: Any?): Any {
        return gson.fromJson(
            sharedPreferences.getString(FILTERS_SHARED_KEY, defaultData as String?),
            FilterParametersDto::class.java)
    }

    companion object {
        const val FILTERS_SHARED_KEY = "FILTERS_SHARED_KEY"
    }

}
