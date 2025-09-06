package ru.practicum.android.diploma.util.mappers

import android.icu.text.DecimalFormat
import ru.practicum.android.diploma.data.dto.ContactsDto
import ru.practicum.android.diploma.data.dto.SalaryDto
import ru.practicum.android.diploma.data.dto.VacancyDetailDto
import ru.practicum.android.diploma.domain.models.Contact
import ru.practicum.android.diploma.domain.models.Vacancy

class VacancyMapper(
    private val filterAreaMapper: FilterAreaMapper,
    private val filterIndustryMapper: FilterIndustryMapper
) {

    fun map(dto: VacancyDetailDto): Vacancy = Vacancy(
        id = dto.id,
        name = dto.name.orEmpty(),
        description = dto.description.orEmpty(),
        salary = dto.salary.toDomain(),
        city = dto.address?.city.orEmpty(),
        address = dto.address?.raw.orEmpty(),
        experience = dto.experience?.name.orEmpty(),
        conditions = getConditions(dto.employment?.name, dto.schedule?.name),
        contact = dto.contacts.toDomain(),
        employerName = dto.employer?.name.orEmpty(),
        employerLogo = dto.employer?.logo.orEmpty(),
        skills = dto.skills.orEmpty(),
        url = dto.url.orEmpty(),
        isFavorite = false
    )

    private fun SalaryDto?.toDomain(): String = this?.let {
        var salaryString = ""
        if (it.from != null) salaryString.plus("от ${DecimalFormat("#,###").format(it.from).replace(',', ' ')} ")
        if (it.to != null) salaryString.plus("до ${DecimalFormat("#,###").format(it.to).replace(',', ' ')} ")
        if (salaryString.isNotEmpty()) {
            if (it.currency != null) salaryString.plus(getCurrencySymbol(it.currency))
        } else {
            salaryString = "Уровень зарплаты не указан"
        }
        salaryString
    } ?: ""

    private fun getCurrencySymbol(currencyCode: String): String {
        return when (currencyCode) {
            "RUR" -> "₽"
            "RUB" -> "₽"
            "USD" -> "$"
            "EUR" -> "€"
            "KZT" -> "₸"
            "UAH" -> "₴"
            "AZN" -> "₼"
            "GEL" -> "₾"
            else -> currencyCode
        }
    }

    private fun getConditions(employment: String?, schedule: String?): String {
        val separator = if (employment != null && schedule != null) ", " else ""
        return (employment ?: "") + separator + (schedule ?: "")
    }

    private fun ContactsDto?.toDomain(): Contact = this?.let {
        Contact(
            name = it.name.orEmpty(),
            email = it.email.orEmpty(),
            phones = it.phones.orEmpty().map { it.formatted ?: "" })
    } ?: Contact("", "", emptyList())

}
