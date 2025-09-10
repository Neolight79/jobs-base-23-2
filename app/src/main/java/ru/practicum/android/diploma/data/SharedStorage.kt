package ru.practicum.android.diploma.data

import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

data class Key<T>(val name: String, val type: Type)

inline fun <reified T> key(name: String): Key<T> =
    Key(name, object : TypeToken<T>() {}.type)

interface SharedStorage {
    fun <T> putData(key: Key<T>, value: T)
    fun <T> getData(key: Key<T>, default: T? = null): T?
}
