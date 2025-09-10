package ru.practicum.android.diploma.util.mappers

import ru.practicum.android.diploma.data.dto.ContactsDto
import ru.practicum.android.diploma.data.dto.SalaryDto
import ru.practicum.android.diploma.data.dto.VacancyDetailDto
import ru.practicum.android.diploma.data.dto.VacancyDetailResponse
import ru.practicum.android.diploma.domain.models.Contact
import ru.practicum.android.diploma.domain.models.Phone
import ru.practicum.android.diploma.domain.models.Vacancy
import java.text.NumberFormat
import java.util.Locale

class VacancyMapper(
    private val filterAreaMapper: FilterAreaMapper,
    private val filterIndustryMapper: FilterIndustryMapper,
    private val salaryNotSpecifiedText: String,
) {

    fun map(dto: VacancyDetailDto): Vacancy = Vacancy(
        id = dto.id,
        name = dto.name.orEmpty(),
        description = dto.description.orEmpty(),
        salary = dto.salary.toDomain(),
        city = dto.address?.city ?: dto.area?.name.orEmpty(),
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

    fun detailResponseToDto(response: VacancyDetailResponse): VacancyDetailDto {
        return VacancyDetailDto(
            id = response.id,
            name = response.name,
            description = response.description,
            salary = response.salary,
            address = response.address,
            experience = response.experience,
            schedule = response.schedule,
            employment = response.employment,
            contacts = response.contacts,
            employer = response.employer,
            area = response.area,
            skills = response.skills,
            url = response.url,
            industry = response.industry
        )
    }

    private fun SalaryDto?.toDomain(): String {
        if (this == null) return ""

        val numberFormat = NumberFormat.getNumberInstance(Locale("ru", "RU")).apply {
            isGroupingUsed = true
        }

        val sb = StringBuilder()
        from?.let { sb.append("от ").append(numberFormat.format(it)).append(' ') }
        to?.let { sb.append("до ").append(numberFormat.format(it)).append(' ') }

        return if (sb.isNotEmpty()) {
            currency?.takeIf { it.isNotBlank() }?.let {
                if (sb.last() != ' ') sb.append(' ')
                sb.append(getCurrencySymbol(it))
            }
            sb.toString().trim()
        } else {
            salaryNotSpecifiedText
        }
    }

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
