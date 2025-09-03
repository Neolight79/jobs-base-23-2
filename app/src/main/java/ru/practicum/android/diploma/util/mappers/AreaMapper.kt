package ru.practicum.android.diploma.util.mappers

import ru.practicum.android.diploma.data.dto.AreaDto
import ru.practicum.android.diploma.domain.models.Area

class AreaMapper {

    fun map(area: Area) : AreaDto {
        return AreaDto(
            id = area.id,
            name = area.name,
            parentId = area.parentId,
            areas = area.areas?.map {
                map(it)
            }
        )
    }

    fun map(area: AreaDto) : Area {
        return Area(
            id = area.id,
            name = area.name,
            parentId = area.parentId,
            areas = area.areas?.map {
                map(it)
            }
        )
    }

}
