package ru.practicum.android.diploma.data

interface SharedStorage {

    fun putData(data: Any)

    fun getData(defaultData: Any? = null): Any

}
