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


    fun map(dto: VacancyDetailDto): Vacancy {
        return Vacancy(
            id = dto.id,
            name = dto.name.orEmpty(),
            description = dto.description.orEmpty(),
            salary = dto.salary?.let { mapSalary(it) } ?: Salary("", "", null, null),
            address = dto.address?.let { mapAddress(it) }
                ?: Address("", "", "", "", ""),
            experience = dto.experience?.let { mapExperience(it) }
                ?: Experience("", ""),
            schedule = dto.schedule?.let { mapSchedule(it) }
                ?: Schedule("", ""),
            employment = dto.employment?.let { mapEmployment(it) }
                ?: Employment("", ""),
            contacts = dto.contacts?.let { mapContacts(it) }
                ?: Contacts("", "", "", emptyList()),
            employer = dto.employer?.let { mapEmployer(it) }
                ?: Employer("", "", ""),
            area = dto.area?.let { filterAreaMapper.map(it) }
                ?: FilterArea(0, "", null, emptyList()),
            skills = dto.skills ?: emptyList(),
            url = dto.url.orEmpty(),
            industry = dto.industry?.let { filterIndustryMapper.map(it) }
                ?: FilterIndustry(0, "")
        )
    }

    private fun mapSalary(dto: SalaryDto): Salary {
        return Salary(
            id = dto.id,
            currency = dto.currency ?: "",
            from = dto.from,
            to = dto.to
        )
    }

    private fun mapAddress(dto: AddressDto): Address {
        return Address(
            id = dto.id,
            city = dto.city ?: "",
            street = dto.street ?: "",
            building = dto.building ?: "",
            fullAddress = dto.raw ?: ""
        )
    }

    private fun mapExperience(dto: ExperienceDto): Experience {
        return Experience(
            id = dto.id,
            name = dto.name ?: ""
        )
    }

    private fun mapSchedule(dto: ScheduleDto): Schedule {
        return Schedule(
            id = dto.id,
            name = dto.name ?: ""
        )
    }

    private fun mapEmployment(dto: EmploymentDto): Employment {
        return Employment(
            id = dto.id,
            name = dto.name ?: ""
        )
    }

    private fun mapContacts(dto: ContactsDto): Contacts {
        return Contacts(
            id = dto.id,
            name = dto.name ?: "",
            email = dto.email ?: "",
            phones = dto.phones?.map { mapPhone(it) } ?: emptyList()
        )
    }

    private fun mapPhone(dto: PhoneDto): Phone {
        return Phone(
            comment = dto.comment,
            formatted = dto.formatted ?: ""
        )
    }

    private fun mapEmployer(dto: EmployerDto): Employer {
        return Employer(
            id = dto.id,
            name = dto.name ?: "",
            logoUrl = dto.logo ?: ""
        )
    }
}
