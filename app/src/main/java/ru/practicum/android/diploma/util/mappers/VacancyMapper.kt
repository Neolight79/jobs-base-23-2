package ru.practicum.android.diploma.util.mappers

import ru.practicum.android.diploma.data.dto.AddressDto
import ru.practicum.android.diploma.data.dto.ContactsDto
import ru.practicum.android.diploma.data.dto.EmployerDto
import ru.practicum.android.diploma.data.dto.EmploymentDto
import ru.practicum.android.diploma.data.dto.ExperienceDto
import ru.practicum.android.diploma.data.dto.PhoneDto
import ru.practicum.android.diploma.data.dto.SalaryDto
import ru.practicum.android.diploma.data.dto.ScheduleDto
import ru.practicum.android.diploma.data.dto.VacancyDetailDto
import ru.practicum.android.diploma.domain.models.Address
import ru.practicum.android.diploma.domain.models.Contacts
import ru.practicum.android.diploma.domain.models.Employer
import ru.practicum.android.diploma.domain.models.Employment
import ru.practicum.android.diploma.domain.models.Experience
import ru.practicum.android.diploma.domain.models.FilterArea
import ru.practicum.android.diploma.domain.models.FilterIndustry
import ru.practicum.android.diploma.domain.models.Phone
import ru.practicum.android.diploma.domain.models.Salary
import ru.practicum.android.diploma.domain.models.Schedule
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
        address = dto.address.toDomain(),
        experience = dto.experience.toDomain(),
        schedule = dto.schedule.toDomain(),
        employment = dto.employment.toDomain(),
        contacts = dto.contacts.toDomain(),
        employer = dto.employer.toDomain(),
        area = dto.area?.let { filterAreaMapper.map(it) } ?: FilterArea(0, "", null, emptyList()),
        skills = dto.skills.orEmpty(),
        url = dto.url.orEmpty(),
        industry = dto.industry?.let { filterIndustryMapper.map(it) } ?: FilterIndustry(0, "")
    )

    private fun SalaryDto?.toDomain(): Salary = this?.let {
        Salary(id = it.id, currency = it.currency ?: "", from = it.from, to = it.to)
    } ?: Salary("", "", null, null)

    private fun AddressDto?.toDomain(): Address = this?.let {
        Address(
            id = it.id,
            city = it.city.orEmpty(),
            street = it.street.orEmpty(),
            building = it.building.orEmpty(),
            fullAddress = it.raw.orEmpty()
        )
    } ?: Address("", "", "", "", "")

    private fun ExperienceDto?.toDomain(): Experience = this?.let {
        Experience(id = it.id, name = it.name.orEmpty())
    } ?: Experience("", "")

    private fun ScheduleDto?.toDomain(): Schedule = this?.let {
        Schedule(id = it.id, name = it.name.orEmpty())
    } ?: Schedule("", "")

    private fun EmploymentDto?.toDomain(): Employment = this?.let {
        Employment(id = it.id, name = it.name.orEmpty())
    } ?: Employment("", "")

    private fun ContactsDto?.toDomain(): Contacts = this?.let {
        Contacts(
            id = it.id,
            name = it.name.orEmpty(),
            email = it.email.orEmpty(),
            phones = it.phones.orEmpty().map { it.toDomain() })
    } ?: Contacts("", "", "", emptyList())

    private fun PhoneDto?.toDomain(): Phone = this?.let {
        Phone(comment = it.comment, formatted = it.formatted.orEmpty())
    } ?: Phone(null, "")

    private fun EmployerDto?.toDomain(): Employer = this?.let {
        Employer(id = it.id, name = it.name.orEmpty(), logoUrl = it.logo.orEmpty())
    } ?: Employer("", "", "")
}
