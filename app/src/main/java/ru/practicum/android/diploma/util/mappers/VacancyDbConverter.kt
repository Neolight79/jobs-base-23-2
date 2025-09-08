package ru.practicum.android.diploma.util.mappers

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.practicum.android.diploma.data.dto.VacancyFavoriteEntity
import ru.practicum.android.diploma.domain.models.Contact
import ru.practicum.android.diploma.domain.models.Vacancy

class VacancyDbConverter {

    fun map(vacancy: Vacancy): VacancyFavoriteEntity {
        return VacancyFavoriteEntity(
            id = 0,
            vacancyId = vacancy.id,
            vacancyName = vacancy.name,
            vacancyDescription = vacancy.description,
            salary = vacancy.salary,
            city = vacancy.city,
            address = vacancy.address,
            experience = vacancy.experience,
            conditions = vacancy.conditions,
            contact = Gson().toJson(vacancy.contact),
            employerName = vacancy.employerName,
            employerLogo = vacancy.employerLogo,
            skills = Gson().toJson(vacancy.skills),
            url = vacancy.url
        )
    }

    fun map(vacancy: VacancyFavoriteEntity): Vacancy {
        return Vacancy(
            id = vacancy.vacancyId,
            name = vacancy.vacancyName.orEmpty(),
            description = vacancy.vacancyDescription.orEmpty(),
            salary = vacancy.salary.orEmpty(),
            city = vacancy.city.orEmpty(),
            address = vacancy.address.orEmpty(),
            experience = vacancy.experience.orEmpty(),
            conditions = vacancy.conditions.orEmpty(),
            contact = Gson().fromJson(vacancy.contact, object : TypeToken<Contact>() {}.type),
            employerName = vacancy.employerName.orEmpty(),
            employerLogo = vacancy.employerLogo.orEmpty(),
            skills = Gson().fromJson(vacancy.skills, object : TypeToken<List<String>>() {}.type),
            url = vacancy.url.orEmpty(),
            isFavorite = true
        )
    }

}
