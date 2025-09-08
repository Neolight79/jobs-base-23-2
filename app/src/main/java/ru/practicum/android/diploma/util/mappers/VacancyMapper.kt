package ru.practicum.android.diploma.util.mappers

import ru.practicum.android.diploma.data.dto.ContactsDto
import ru.practicum.android.diploma.data.dto.SalaryDto
import ru.practicum.android.diploma.data.dto.VacancyDetailDto
import ru.practicum.android.diploma.data.dto.VacancyDetailResponse
import ru.practicum.android.diploma.data.dto.VacancyFavoriteEntity
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

    fun toFavoriteEntity(v: Vacancy): VacancyFavoriteEntity {
        return VacancyFavoriteEntity(
            id = 0,
            vacancyId = v.id,
            vacancyName = v.name,
            employerName = v.employerName,
            employerLogo = v.employerLogo,
            salaryFrom = null,
            salaryTo = null,
            salaryCurrency = null
        )
    }

    private fun SalaryDto?.toDisplayString(): String = this.toDomain()

    fun fromFavoriteEntity(e: VacancyFavoriteEntity): Vacancy {
        val salaryDisplay = SalaryDto(
            id = "",
            from = e.salaryFrom?.toIntOrNull(),
            to = e.salaryTo?.toIntOrNull(),
            currency = e.salaryCurrency
        ).toDisplayString()

        return Vacancy(
            id = e.vacancyId,
            name = e.vacancyName.orEmpty(),
            description = "",
            salary = salaryDisplay,
            city = "",
            address = "",
            experience = "",
            conditions = "",
            contact = Contact("", "", emptyList()),
            employerName = e.employerName.orEmpty(),
            employerLogo = e.employerLogo.orEmpty(),
            skills = emptyList(),
            url = "",
            isFavorite = true
        )
    }
}
