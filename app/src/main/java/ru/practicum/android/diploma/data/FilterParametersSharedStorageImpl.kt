package ru.practicum.android.diploma.data

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import ru.practicum.android.diploma.data.dto.FilterParametersDto

class FilterParametersSharedStorageImpl(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) : SharedStorage {
    override fun putData(data: FilterParametersDto) {
        sharedPreferences.edit {
            putString(FILTERS_SHARED_KEY, gson.toJson(data))
        }
    }

    override fun getData(defaultData: FilterParametersDto): FilterParametersDto {
        val json = sharedPreferences.getString(FILTERS_SHARED_KEY, null) ?: return defaultData
        return gson.fromJson(json, FilterParametersDto::class.java) ?: defaultData
    }

    private companion object {
        const val FILTERS_SHARED_KEY = "FILTERS_SHARED_KEY"
    }
}
