package ru.practicum.android.diploma.details.data.local.model

import ru.practicum.android.diploma.search.domain.models.Vacancy
import javax.inject.Inject

class VacancyConverter @Inject constructor() {
    fun toEntity(vacancy: Vacancy): VacancyEntity {
        return with(vacancy) {
            VacancyEntity(
                id = id,
                iconUri = iconUri,
                title = title,
                company = company,
                salary = salary,
                area = area,
                date = date
            )
        }
    }

    fun mapToVacancies(entities: List<VacancyEntity>): List<Vacancy>{
        return entities.map { toVacancy(it) }
    }
    
    private fun toVacancy(vacancyEntity: VacancyEntity): Vacancy {
        return with(vacancyEntity) {
            Vacancy(
                id = id,
                iconUri = iconUri,
                title = title,
                company = company,
                salary = salary,
                area = area,
                date = date
            )
        }
    }
}