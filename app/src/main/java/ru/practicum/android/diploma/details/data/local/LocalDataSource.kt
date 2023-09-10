package ru.practicum.android.diploma.details.data.local

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.details.domain.models.VacancyFullInfo
import ru.practicum.android.diploma.search.domain.models.Vacancy

interface LocalDataSource {
    suspend fun removeVacancyFromFavorite(id: String): Flow<Int>
    suspend fun addVacancyToFavorite(vacancy: VacancyFullInfo): Flow<Unit>
    suspend fun getFavoriteVacancyById(id: String): Flow<Boolean>

}