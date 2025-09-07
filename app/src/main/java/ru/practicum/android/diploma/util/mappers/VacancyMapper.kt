package ru.practicum.android.diploma.util.mappers

import ru.practicum.android.diploma.data.dto.ContactsDto
import ru.practicum.android.diploma.data.dto.SalaryDto
import ru.practicum.android.diploma.data.dto.VacancyDetailDto
import ru.practicum.android.diploma.domain.models.Contact
import ru.practicum.android.diploma.domain.models.Phone
import ru.practicum.android.diploma.domain.models.Vacancy
import java.text.NumberFormat
import java.util.Locale

class VacancyMapper(
    private val filterAreaMapper: FilterAreaMapper,
    private val filterIndustryMapper: FilterIndustryMapper
) {

    fun map(dto: VacancyDetailDto): Vacancy = Vacancy(
        id = dto.id,
        name = dto.name.orEmpty(),
        description = dto.description.orEmpty(),
        salary = dto.salary.toDomain(),
        city = if (dto.address?.city != null) dto.address.city else dto.area?.name.orEmpty(),
        address = if (dto.address?.raw != null) dto.address.raw else dto.area?.name.orEmpty(),
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
        @Suppress("DEPRECATION")
        val numberFormat = NumberFormat.getNumberInstance(Locale("ru"))

        var salaryString = ""
        if (it.from != null) salaryString += "от ${numberFormat.format(it.from)} "
        if (it.to != null) salaryString += "до ${numberFormat.format(it.to)} "

        if (salaryString.isNotEmpty()) {
            if (!it.currency.isNullOrEmpty()) salaryString += getCurrencySymbol(it.currency)
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
            phones = it.phones.orEmpty().map { Phone(
                comment = it.comment,
                formatted = it.formatted.orEmpty()
            ) }
        )
    } ?: Contact("", "", emptyList())

}
