package ru.practicum.android.diploma.util.mappers

import ru.practicum.android.diploma.data.dto.FilterAreaDto
import ru.practicum.android.diploma.domain.models.FilterArea

class FilterAreaMapper {

    fun map(area: FilterArea): FilterAreaDto {
        return FilterAreaDto(
            id = area.id,
            name = area.name,
            parentId = area.parentId,
            areas = area.areas?.map {
                map(it)
            }
        )
    }

    fun map(area: FilterAreaDto): FilterArea {
        return FilterArea(
            id = area.id,
            name = area.name,
            parentId = area.parentId,
            areas = area.areas?.map {
                map(it)
            }
        )
    }

}
