package ru.practicum.android.diploma.details.domain

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.details.domain.models.VacancyFullInfo
import ru.practicum.android.diploma.util.functional.Either
import ru.practicum.android.diploma.util.functional.Failure

interface DetailsRepository {
    suspend fun getFullVacancyInfo(id: String): Either<Failure, VacancyFullInfo>
    suspend fun removeVacancyFromFavorite(id: String): Flow<Int>
    suspend fun addVacancyToFavorite(vacancy: VacancyFullInfo): Flow<Unit>
    suspend fun showIfInFavourite(id: String): Flow<Boolean>
    suspend fun isVacancyInFavs(id: String): Boolean

}